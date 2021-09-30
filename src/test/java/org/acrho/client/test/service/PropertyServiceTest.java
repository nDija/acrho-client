package org.acrho.client.test.service;

import org.acrho.client.service.PropertyService;
import org.acrho.client.test.TimingExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PropertyServiceTest {

    @Test
    @DisplayName("When I load values from yaml file I retrieve an object with values set")
    @ExtendWith(TimingExtension.class)
    void propertyLoadTest() {
        assertNotNull(PropertyService.getInstance().getAcrhoProperties());
    }
}
