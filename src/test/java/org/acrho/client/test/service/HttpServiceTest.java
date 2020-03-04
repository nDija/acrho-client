package org.acrho.client.test.service;

import lombok.extern.log4j.Log4j2;
import org.acrho.client.model.property.AcrhoProperties;
import org.acrho.client.service.HttpService;
import org.acrho.client.service.PropertyService;
import org.acrho.client.test.TimingExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@Disabled
class HttpServiceTest {

    private static AcrhoProperties ap = PropertyService.getInstance().getAcrhoProperties();
    private static HttpService httpService = new HttpService();

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void getTest() throws IOException{
        String response = httpService.get("http://www.acrho.org", null);
        log.debug(response, UTF_8.name());
        assertNotNull(response);
    }

    @Test
    @DisplayName("When I request years I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void getYears() throws IOException{
        String response = httpService.get(ap.getBaseUrl() + "/"+ ap.getYears().getUri(), ap.getYears().getParameters());
        log.debug(response, UTF_8.name());
        assertNotNull(response);
    }

    @Test
    @DisplayName("When I request runs from 2016 I get a 200 status code and the HTML page of 2016's runs")
    @ExtendWith(TimingExtension.class)
    void getRuns() throws IOException{
        ap.getRuns().getParameters().put("ant_filter_value", "2016");
        String response = httpService.post("http://www.acrho.org" + "/"+ ap.getRuns().getUri(), ap.getRuns().getParameters(), ap.getPostRequest().getHeaders());
        log.debug(response);
        assertNotNull(response);
    }

    @Test
    @DisplayName("When I request runs from 2016 I get a 200 status code and the HTML page of 2016's runs")
    @ExtendWith(TimingExtension.class)
    void getResults() throws IOException{
        ap.getRuns().getParameters().put("ant_filter_value", "2408");
        String response = httpService.post("http://www.acrho.org" + "/"+ ap.getRuns().getUri(), ap.getRuns().getParameters(), ap.getPostRequest().getHeaders());
        log.debug(response);
        assertNotNull(response);
    }
}
