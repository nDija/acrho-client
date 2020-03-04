package org.acrho.client.service;

import lombok.extern.log4j.Log4j2;
import org.acrho.client.model.property.AcrhoProperties;
import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

@Log4j2
public class HttpService {

	private static Proxy proxy;
	private final static AcrhoProperties acrhoProperties = PropertyService.getInstance().getAcrhoProperties();

	static {
		if (acrhoProperties.getProxy() != null) {
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(acrhoProperties.getProxy().getHost(), acrhoProperties.getProxy().getPort()));
		}
	}

	/**
	 * Return the inputstream for the given url
	 * 
	 * @param url The url to reach
	 * @param parameters The map of request parameters
	 * @throws IOException when error occurs {@link IOException}
	 * @return An {@link InputStream}
	 */
	public String get(String url, Map<String, String> parameters) throws IOException {
		String queryParameters = null;
		if(parameters != null)
			queryParameters = buildQueryParameters(parameters);
		if(queryParameters != null)
			url += queryParameters;
		log.debug("calling: " + url);
		HttpURLConnection connection = createConnection(url);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		String response = IOUtils.toString(connection.getInputStream(), ISO_8859_1.name());
		closeConnection(connection);
		return response;
	}

	public InputStream post(Map<String,String> parameters, Map<String, String> headers, HttpURLConnection connection) throws IOException {

		String data = buildPostBodyString(parameters);
		// add request header
		connection.setRequestMethod("POST");
		headers.forEach(connection::setRequestProperty);
		// Send post request
		connection.setDoOutput(true);
		final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();

		//log.debug("\nSending 'POST' request to URL : " + url);
		log.debug("Post parameters : " + data);
		log.debug("Response Code : " + connection.getResponseCode());

		return connection.getInputStream();
	}

	public String post(String url, Map<String,String> parameters, Map<String, String> headers) throws IOException {
		HttpURLConnection connection = createConnection(url);
		InputStream is = post(parameters, headers, connection);
		String responseBody = IOUtils.toString(is, ISO_8859_1.name());
		closeConnection(connection);
		return responseBody;
	}

	public String post(HttpURLConnection connection, Map<String,String> parameters, Map<String, String> headers) throws IOException {
		InputStream is = post(parameters, headers, connection);
		String responseBody = IOUtils.toString(is, ISO_8859_1.name());
		return responseBody;
	}

	public static HttpURLConnection createConnection(String url) throws IOException {
		if(proxy == null) {
			return (HttpURLConnection) new URL(url).openConnection();
		} else {
			return (HttpURLConnection) new URL(url).openConnection(proxy);
		}
	}

	public static void closeConnection(HttpURLConnection connection) {
		connection.disconnect();
	}

	public static String buildQueryParameters(Map<String, String> parameters) {
		StringBuilder sb = new StringBuilder("?");
		parameters.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
		return sb.toString().substring(0, sb.length() - 1);
	}

	public static String buildPostBodyString(Map<String, String> parameters) {
		return buildQueryParameters(parameters).substring(1);
	}
}
