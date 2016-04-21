package org.acrho.test.acrho_client.service;

import java.io.IOException;
import java.io.InputStream;

import org.acrho.acrho_client.exception.AcrhoConnectionException;
import org.acrho.acrho_client.service.AcrhoProperties;
import org.acrho.acrho_client.service.HttpService;
import org.acrho.acrho_client.service.IAcrhoProperties;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class HttpServiceTest {

    @BeforeClass
    public static void init() {

    }
    @Test
    public void getTest() throws AcrhoConnectionException, IOException {
        // Given
        String url = AcrhoProperties.get(IAcrhoProperties.URL) + "/Resultats_Bury_220214.pdf";
        // When
        InputStream is = HttpService.get(url);
        // Then
        Assert.assertNotEquals(-1, is.read());
    }
}
