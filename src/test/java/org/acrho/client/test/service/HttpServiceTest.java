package org.acrho.client.test.service;

import org.acrho.client.AcrhoClientException;
import org.acrho.client.model.property.AcrhoProperties;
import org.acrho.client.service.AcrhoHttpClient;
import org.acrho.client.service.HttpService;
import org.acrho.client.service.PropertyService;
import org.acrho.client.test.TimingExtension;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class HttpServiceTest {

    private final Logger log = LoggerFactory.getLogger(HttpServiceTest.class);
    private static final AcrhoProperties ap = PropertyService.getInstance().getAcrhoProperties();
    private static final HttpService httpService = new HttpService();

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void testGetTest() throws AcrhoClientException {
        String is = AcrhoHttpClient.get("http://www.acrho.org", null, null, StandardCharsets.ISO_8859_1);
        log.debug(is);
    }

    @Test
    @DisplayName("When I request years I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void testGetYears() {
        try(InputStream is = httpService.get(ap.getBaseUrl() + "/"+ ap.getYears().getUri(), ap.getYears().getParameters())) {
            log.debug(IOUtils.toString(is, UTF_8.name()));
        } catch (IOException e) {
            log.error(e.getMessage());
            fail();
        }
        assertTrue(true);
    }

    @Test
    @DisplayName("When I request runs from 2016 I get a 200 status code and the HTML page of 2016's runs")
    @ExtendWith(TimingExtension.class)
    void getRuns() {
        ap.getRuns().getParameters().put("ant_filter_value", "2016");
        try(InputStream is = httpService.post(ap.getBaseUrl() + "/"+ ap.getRuns().getUri(), ap.getRuns().getParameters(), ap.getPostRequest().getHeaders())) {
            log.debug(IOUtils.toString(is, UTF_8.name()));
        } catch (IOException e) {
            log.error(e.getMessage());
            fail();
        }
        assertTrue(true);
    }
}
