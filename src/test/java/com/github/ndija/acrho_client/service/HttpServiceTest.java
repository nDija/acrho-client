package com.github.ndija.acrho_client.service;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.ndija.acrho_client.exception.AcrhoConnectionException;

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
