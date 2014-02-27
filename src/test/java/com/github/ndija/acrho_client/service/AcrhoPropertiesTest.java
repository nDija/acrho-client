package com.github.ndija.acrho_client.service;

import org.junit.Assert;
import org.junit.Test;

public class AcrhoPropertiesTest {

    @Test
    public void getTest() {
        // Given
        String key = AcrhoProperties.URL;
        // When
        String url = AcrhoProperties.get(key);
        // Then
        Assert.assertEquals("http://www.acrho.org", url);
    }
}
