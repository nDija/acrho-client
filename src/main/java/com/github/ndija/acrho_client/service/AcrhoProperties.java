package com.github.ndija.acrho_client.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class AcrhoProperties {
    private static Properties prop = null;

    private static final Logger log = Logger.getLogger(AcrhoProperties.class);

    public static String URL = "url";
    public static String PROXY_ADRESS = "proxy.adress";
    public static String PROXY_PORT = "proxy.port";
    public static String URL_RESULT_RUNS = "url.result.runs";
    
    public static void load() {
        String filename = "acrho.properties";

        InputStream input = AcrhoProperties.class.getClassLoader().getResourceAsStream(filename);
        if (input == null) {
            log.error("Sorry, unable to find " + filename);
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
