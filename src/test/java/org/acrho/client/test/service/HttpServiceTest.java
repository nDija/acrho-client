package org.acrho.client.test.service;

import lombok.extern.log4j.Log4j2;
import org.acrho.client.model.property.AcrhoProperties;
import org.acrho.client.service.HttpService;
import org.acrho.client.service.PropertyService;
import org.acrho.client.test.TimingExtension;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@Log4j2
class HttpServiceTest {

    private static AcrhoProperties ap = PropertyService.getInstance().getAcrhoProperties();
    private static HttpService httpService = new HttpService();

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void getTest() {
        try(InputStream is = httpService.get("http://www.acrho.org", null)) {
            log.debug(IOUtils.toString(is, UTF_8.name()));
        } catch (IOException e) {
            log.error(e);
            fail();
        }
        assertTrue(true);
    }

    @Test
    @DisplayName("When I request years I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void getYears() {
        try(InputStream is = httpService.get(ap.getBaseUrl() + "/"+ ap.getYears().getUri(), ap.getYears().getParameters())) {
            log.debug(IOUtils.toString(is, UTF_8.name()));
        } catch (IOException e) {
            log.error(e);
            fail();
        }
        assertTrue(true);
    }

    @Test
    @DisplayName("When I request runs from 2016 I get a 200 status code and the HTML page of 2016's runs")
    @ExtendWith(TimingExtension.class)
    void getRuns() {
        ap.getRuns().getParameters().put("ant_filter_value", "2016");
        try(InputStream is = httpService.post("http://www.acrho.org" + "/"+ ap.getRuns().getUri(), ap.getRuns().getParameters(), ap.getPostRequest().getHeaders())) {
            log.debug(IOUtils.toString(is, UTF_8.name()));
        } catch (IOException e) {
            log.error(e);
            fail();
        }
        assertTrue(true);
    }

    @Test
    @DisplayName("When I request runs from 2016 I get a 200 status code and the HTML page of 2016's runs")
    @ExtendWith(TimingExtension.class)
    void getResults() {
        ap.getRuns().getParameters().put("ant_filter_value", "2408");
        try(InputStream is = httpService.post("http://www.acrho.org" + "/"+ ap.getRuns().getUri(), ap.getRuns().getParameters(), ap.getPostRequest().getHeaders())) {
            log.debug(IOUtils.toString(is, UTF_8.name()));
        } catch (IOException e) {
            log.error(e);
            fail();
        }
        assertTrue(true);
    }
}
