package com.github.ndija.acrho_client.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.github.ndija.acrho_client.exception.AcrhoConnectionException;

public class HttpService {

	private static final Logger log = Logger.getLogger(HttpService.class);

	private static String proxy_adress = null;

	private static Integer proxy_port = 80;

	private static String USER_AGENT = "Mozilla/5.0";

	static {
		proxy_adress = AcrhoProperties.get(IAcrhoProperties.PROXY_ADRESS);
		String port = AcrhoProperties.get(IAcrhoProperties.PROXY_PORT);
		if (!StringUtils.isEmpty(port)) {
			proxy_port = Integer.valueOf(port);
		}
	}

	/**
	 * Return the inputstream for the given url
	 * 
	 * @param url
	 *            The url to reach
	 * @return An {@link java.io.InputStream}
	 * @throws AcrhoConnectionException
	 *             if can't connect to the url specified
	 */
	public static InputStream get(String url) throws AcrhoConnectionException {
		log.debug("calling: " + url);
		Proxy proxy = null;
		if (!StringUtils.isEmpty(proxy_adress)) {
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy_adress, proxy_port));
		}
		try {
			HttpURLConnection connection = null;
			if (proxy != null)
				connection = (HttpURLConnection) new URL(url).openConnection(proxy);
			else
				connection = (HttpURLConnection) new URL(url).openConnection();

			connection.setDoOutput(true);
			connection.setDoInput(true);
			return connection.getInputStream();
		} catch (MalformedURLException e) {
			log.error("Can't read url: " + url, e);
			throw new AcrhoConnectionException("Can't read url: " + url, e);
		} catch (IOException e) {
			log.error("Can't read url: " + url, e);
			throw new AcrhoConnectionException("Can't read url: " + url, e);
		}
	}

	public static InputStream post(String url, String data) throws AcrhoConnectionException {
		Proxy proxy = null;
		try {
			if (!StringUtils.isEmpty(proxy_adress)) {
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy_adress, proxy_port));
			}
			HttpURLConnection connection = null;
			if (proxy != null)
				connection = (HttpURLConnection) new URL(url).openConnection(proxy);
			else
				connection = (HttpURLConnection) new URL(url).openConnection();

			// add request header
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4");
			connection.setRequestProperty("Origin", "http://www.acrho.org");

			// Send post request
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			wr.close();

			int responseCode = connection.getResponseCode();
			log.debug("\nSending 'POST' request to URL : " + url);
			log.debug("Post parameters : " + data);
			log.debug("Response Code : " + responseCode);

			return connection.getInputStream();
		} catch (MalformedURLException e) {
			log.error("Can't read url: " + url, e);
			throw new AcrhoConnectionException("Can't read url: " + url, e);
		} catch (IOException e) {
			log.error("Can't read url: " + url, e);
			throw new AcrhoConnectionException("Can't read url: " + url, e);
		}

	}

}
