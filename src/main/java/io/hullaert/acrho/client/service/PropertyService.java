package io.hullaert.acrho.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.hullaert.acrho.client.model.property.AcrhoProperties;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

@Getter
public final class PropertyService {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyService.class);
    private static PropertyService instance;
    private AcrhoProperties acrhoProperties;

    private PropertyService() {
        this.load();
    }

    public static PropertyService getInstance() {
        if(instance == null) {
            instance = new PropertyService();
        }
        return instance;
    }

    private void load() {
        var mapper = new ObjectMapper(new YAMLFactory());
        try(InputStream propertyFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("acrho.yml")){
            acrhoProperties = mapper.readValue(propertyFile, AcrhoProperties.class);
        } catch (IOException e) {
            LOG.error("Can't load property file acrho.yml", e);
        }
    }
}
