package com.epam.esm.propReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private FileReader fileReader;
    private Properties properties;
    private final String PROPERTY_FILE_NAME = "api/src/main/resources/config.properties";

    public PropertiesReader() throws IOException {
        fileReader = new FileReader(PROPERTY_FILE_NAME);
        properties = new Properties();
        properties.load(fileReader);
    }

    public String readProperty(String propName){
        return properties.getProperty(propName);
    }
}
