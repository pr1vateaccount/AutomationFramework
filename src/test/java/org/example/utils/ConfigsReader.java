package org.example.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigsReader {

    public static Properties prop;

    /**
     * will read properties file
     * @param filepath
     */
    public static void readProperties(String filepath){
        try {
            FileInputStream fis = new FileInputStream(filepath);
            prop = new Properties();
            prop.load(fis);
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * returns the value for a specific key
     * @param key
     * @return String value
     */
    public static String getProperty(String key){
        return prop.getProperty(key);
    }
}
