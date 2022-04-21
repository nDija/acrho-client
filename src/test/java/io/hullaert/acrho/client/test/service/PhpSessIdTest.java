package io.hullaert.acrho.client.test.service;

import io.hullaert.acrho.client.AcrhoClientException;
import io.hullaert.acrho.client.service.AcrhoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PhpSessIdTest {
    private static final Logger LOG = LoggerFactory.getLogger(AcrhoServiceTest.class);

    private final AcrhoService as = new AcrhoService();

    @Test
    @DisplayName("It should return a phpsessid")
    void testGetSessionId() throws AcrhoClientException {
        String phpSessId = as.getPHPSessId();
        LOG.debug(phpSessId);
        Assertions.assertNotNull(phpSessId, "No phpSessId");
    }
}
