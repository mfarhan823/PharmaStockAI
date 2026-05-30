package pharmastock.service;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                System.err.println(" config.properties file not found!");
            } else {
                properties.load(input);
                System.out.println(" Config loaded!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getApiKey() {
        return properties.getProperty("api.key");
    }

    public static String getApiUrl() {
        return properties.getProperty("api.url");
    }

    public static String getModelName() {
        return properties.getProperty("model.name");
    }
}