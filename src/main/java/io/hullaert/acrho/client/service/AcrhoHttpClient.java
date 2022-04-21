package io.hullaert.acrho.client.service;

import io.hullaert.acrho.client.AcrhoClientException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class AcrhoHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(AcrhoHttpClient.class);

    private AcrhoHttpClient() {}

    public static String get(String url, Map<String, String> headers, List<NameValuePair> parameters, Charset charset)
            throws AcrhoClientException {


        HttpResponse<String> response = getResponse(url, headers, parameters, charset);

        return response.body();
    }

    public static HttpResponse<String> getResponse(
            String url,
            Map<String, String> headers,
            List<NameValuePair> parameters,
            Charset charset)
            throws AcrhoClientException {

        var request = buildRequest(url, headers, parameters);

        return sendRequest(request, charset);
    }

    public static String getPHPSessId(
            String url,
            Map<String, String> headers,
            List<NameValuePair> parameters,
            Charset charset)
            throws AcrhoClientException {

        if(headers.get("Cookie") != null)
            return headers.get("Cookie");
        var request = buildRequest(url, headers, parameters);
        var response = sendRequest(request, charset);
        if(!response.headers().map().get("Set-Cookie").isEmpty()) {
            List<String> cookies = Arrays.asList(
                    response.headers().map().get("Set-Cookie").get(0).split(";"));
            for (String cookie : cookies) {
                if(cookie.startsWith("PHPSESSID"))
                    return cookie;
            }
        }
        throw new AcrhoClientException(new Exception("no phpsessid"));
    }

    public static HttpRequest.Builder buildRequest(String url,
                                                   Map<String, String> headers,
                                                   List<NameValuePair> parameters) {
        URI uri;
        try {
            uri = getUri(url, parameters);
        } catch (URISyntaxException e) {
            throw new AcrhoClientException(e);
        }

        var request = HttpRequest.newBuilder()
                .GET()
                .uri(uri);

        if (headers != null)
            request.headers(getHeaders(headers));
        return request;
    }

    public static HttpResponse<String> sendRequest(HttpRequest.Builder request, Charset charset) {
        HttpResponse<String> response;
        var httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        try {
            response = httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString(charset));
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AcrhoClientException(e);
        }

        if (LOG.isDebugEnabled()) {
            response.headers().map().forEach((k, v) -> LOG.debug(MessageFormat.format("{0} : {1}", k, v)));
            LOG.debug(MessageFormat.format("status: {0}", response.statusCode()));
            LOG.debug(response.body());
        }
        return response;
    }

    public static String post(String url,
                              Map<String, String> headers,
                              List<NameValuePair> parameters,
                              Map<String, String> body, Charset charset) throws AcrhoClientException {

        URI uri;
        try {
            uri = getUri(url, parameters);
        } catch (URISyntaxException e) {
            throw new AcrhoClientException(e);
        }

        var publisher = new MultiPartBodyPublisher();
        body.forEach(publisher::addPart);

        var request = HttpRequest.newBuilder();
        if (headers != null)
            request.headers(getHeaders(headers));

        request.POST(publisher.build())
                .header("Content-Type", "multipart/form-data; boundary=" + publisher.getBoundary())
                .uri(uri);

        HttpResponse<String> response = sendRequest(request, charset);
        return response.body();
    }

    private static URI getUri(String url, List<NameValuePair> parameters) throws URISyntaxException {
        if (parameters == null)
            parameters = new ArrayList<>();
        return new URIBuilder(url)
                .addParameters(parameters)
                .build();
    }

    private static String[] getHeaders(Map<String, String> headers) {
        return headers.entrySet().
                stream().
                flatMap(entry -> Stream.of(entry.getKey(),entry.getValue())).
                toArray(String[]::new);
    }
}
