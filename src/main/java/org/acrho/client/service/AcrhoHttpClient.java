package org.acrho.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.acrho.client.AcrhoClientException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class AcrhoHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(AcrhoHttpClient.class);

    /**private static final HttpClient _httpClient = HttpClient.newBuilder()
     .version(HttpClient.Version.HTTP_1_1)
     .connectTimeout(Duration.ofSeconds(10))
     .build();**/

    private AcrhoHttpClient() {}

    public static String get(String url, Map<String, String> headers, List<NameValuePair> parameters, Charset charset)
            throws AcrhoClientException {
        HttpClient _httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        URI uri = null;
        try {
            uri = getUri(url, parameters);
        } catch (URISyntaxException e) {
            LOG.error("Cannot create URI: url");
            throw new AcrhoClientException(e);
        }

        var request = HttpRequest.newBuilder()
                .GET()
                .uri(uri);

        if(headers != null)
            request.headers(getHeaders(headers));

        HttpResponse<String> response = null;
        try {
            response = _httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString(charset));
        } catch (IOException | InterruptedException e) {
            LOG.error(MessageFormat.format("Cannot send or get response: {0}", url));
            Thread.currentThread().interrupt();
            throw new AcrhoClientException(e);
        }

        if (LOG.isDebugEnabled()) {
            response.headers().map().forEach((k, v) -> LOG.debug(MessageFormat.format("{0} : {1}", k, v)));
            LOG.debug(MessageFormat.format("status: {0}", response.statusCode()));
            LOG.debug(response.body());
        }

        return response.body();
    }

    public static String post(String url,
                              Map<String, String> headers,
                              List<NameValuePair> parameters,
                              Map<String, String> body, Charset charset) throws AcrhoClientException {
        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "Connection,Host,Content-Length");


        HttpClient _httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                //.followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        URI uri = null;
        try {
            uri = getUri(url, parameters);
        } catch (URISyntaxException e) {
            LOG.error("Cannot create URI: url");
            throw new AcrhoClientException(e);
        }

        var objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new AcrhoClientException(e);
        }

        requestBody = requestBody.substring(1).substring(0, requestBody.length() - 1); // index starts at zero
        requestBody = requestBody.replaceAll(",", "\n");

        MultiPartBodyPublisher publisher = new MultiPartBodyPublisher();
        body.forEach((k,v) -> publisher.addPart(k, v));

        HttpRequest.Builder request = HttpRequest.newBuilder();
        if (headers != null)
            request.headers(getHeaders(headers));

        request.POST(publisher.build())
                .header("Content-Type", "multipart/form-data; boundary=" + publisher.getBoundary())
                .uri(uri);




        /**if (headers != null)
         request.headers(getHeaders(headers));
         request.version(HttpClient.Version.HTTP_1_1);**/
        HttpResponse<String> response = null;
        try {
            response = _httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString(charset));
        } catch (IOException | InterruptedException e) {
            LOG.error(MessageFormat.format("Cannot send or get response: {0}", url));
            Thread.currentThread().interrupt();
            throw new AcrhoClientException(e);
        }

        if (LOG.isDebugEnabled()) {
            response.headers().map().forEach((k, v) -> LOG.debug(MessageFormat.format("{0} : {1}", k, v)));
            LOG.debug(MessageFormat.format("status: {0}", response.statusCode()));
            LOG.debug(response.body().toString());
        }
        if(response.statusCode() != 200)
            throw new AcrhoClientException(new Exception("status not 200 is " + response.statusCode()));
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

    public static HttpRequest.BodyPublisher ofFormData(Map<String, String> data, String contentType) {
        var builder = new StringBuilder(contentType + System.lineSeparator());
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append(System.lineSeparator());
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append(": ");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
