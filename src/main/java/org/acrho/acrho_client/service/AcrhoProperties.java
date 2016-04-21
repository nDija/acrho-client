package org.acrho.acrho_client.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class AcrhoProperties implements IAcrhoProperties {
    private static Properties prop = null;

    private static final Logger log = Logger.getLogger(AcrhoProperties.class);

    public static void load() {

        InputStream input = AcrhoProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
        if (input == null) {
            log.error("Sorry, unable to find " + PROPERTIES_FILENAME);
            return;
        }

        // load a properties file from class path, inside static method
        prop = new Properties();
        try {
            prop.load(input);
        } catch (IOException e) {
            prop = null;
            log.error(e);
        }
    }

    public static String get(String key) {
        if (prop == null)
            load();
        if (prop == null)
            return null;
        return prop.getProperty(key);
    }

}
