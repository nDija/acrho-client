package org.acrho.test.acrho_client.service;

import org.acrho.acrho_client.service.AcrhoProperties;
import org.acrho.acrho_client.service.IAcrhoProperties;
import org.junit.Assert;
import org.junit.Test;

public class AcrhoPropertiesTest {

    @Test
    public void getTest() {
        // Given
        String key = IAcrhoProperties.URL;
        // When
        String url = AcrhoProperties.get(key);
        // Then
        Assert.assertEquals("http://www.acrho.org", url);
    }
}
