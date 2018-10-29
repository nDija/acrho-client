package org.acrho.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.acrho.client.model.property.AcrhoProperties;

import java.io.IOException;
import java.io.InputStream;

@Log4j2
@Getter
public class PropertyService {

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
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try( InputStream propertyFile = PropertyService.class.getClassLoader().getResourceAsStream("acrho.yml")){
            acrhoProperties = mapper.readValue(propertyFile, AcrhoProperties.class);
        } catch (IOException e) {
            log.error("Can't load property file acrho.yml", e);
        }
    }
}
