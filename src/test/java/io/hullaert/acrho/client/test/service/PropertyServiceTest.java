package io.hullaert.acrho.client.test.service;

import io.hullaert.acrho.client.AcrhoClientException;
import io.hullaert.acrho.client.model.AcrhoRun;
import io.hullaert.acrho.client.service.AcrhoService;
import io.hullaert.acrho.client.service.PropertyService;
import io.hullaert.acrho.client.test.TimingExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PropertyServiceTest {

    private final AcrhoService as = new AcrhoService();

    private static final Logger LOG = LoggerFactory.getLogger(AcrhoServiceTest.class);

    @Test
    @DisplayName("When I load values from yaml file I retrieve an object with values set")
    @ExtendWith(TimingExtension.class)
    void propertyLoadTest() {
        assertNotNull(PropertyService.getInstance().getAcrhoProperties());
    }

    @Test
    @DisplayName("When I request an url I get a 200 status code")
    @ExtendWith(TimingExtension.class)
    void testGetRuns() throws AcrhoClientException {
        List<AcrhoRun> runs = as.getRuns("2017");
        runs.forEach(r -> LOG.debug(r.toString()));
        assertEquals(40, runs.size(), "Test if number of runs is equals as expected");
    }
}
