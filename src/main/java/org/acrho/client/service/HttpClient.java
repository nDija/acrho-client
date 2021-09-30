package org.acrho.client.service;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class HttpClient {

    public static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    private static final java.net.http.HttpClient _httpClient = java.net.http.HttpClient.newBuilder()
            .version(java.net.http.HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static String get(String url, Map<String, String> headers, List<NameValuePair> parameters, Charset charset) throws IOException, InterruptedException, URISyntaxException {
        URI uri = getUri(url, parameters);

        HttpRequest.Builder request = HttpRequest.newBuilder()
                .GET()
                .uri(uri);

        if(headers != null)
            request.headers(getHeaders(headers));

        HttpResponse<String> response = _httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString(charset));

        if (log.isDebugEnabled()) {
            response.headers().map().forEach((k, v) -> log.debug(MessageFormat.format("{0} : {1}", k, v)));
            log.debug(MessageFormat.format("status: {0}", response.statusCode()));
            log.debug(response.body());
        }

        return response.body();
    }

    public static String post(String url, Map<String, String> headers, List<NameValuePair> parameters, Map<String, String> body, Charset charset) throws IOException, InterruptedException, URISyntaxException {

        URI uri = getUri(url, parameters);

        String requestBody = body.entrySet()
                .stream()
                .toString();

        HttpRequest.Builder request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(uri);

        if(headers != null)
            request.headers(getHeaders(headers));

        HttpResponse<String> response = _httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString(charset));

        if (log.isDebugEnabled()) {
            response.headers().map().forEach((k, v) -> log.debug(MessageFormat.format("{0} : {1}", k, v)));
            log.debug(MessageFormat.format("status: {0}", response.statusCode()));
            log.debug(response.body());
        }

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
                flatMap(entry ->Stream.of(entry.getKey(),entry.getValue())).
                toArray(String[]::new);
    }
}
