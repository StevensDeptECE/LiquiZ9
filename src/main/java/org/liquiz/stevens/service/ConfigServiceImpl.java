package org.liquiz.stevens.service;

import edu.ksu.lti.launch.service.ConfigService;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
/**
 * This service is able to look up arbitrary config values stored as a
 * key/value pair.
 * <p>
 * These values could come from a database or application properties file
 */
public class ConfigServiceImpl implements ConfigService{
    private final Map<String,String> properties;

    @Value("${config.property.oauth_client_id}")
    private String oauthClientId;

    @Value("${config.property.oauth_client_secret}")
    private String oauthClientSecret;

    @Value("${config.property.canvasUrl}")
    private String canvasUrl;


    public ConfigServiceImpl() {
        properties = new HashMap<>();
    }

    @Override
    public String getConfigValue(String key) {
        if("oauth_client_id".equals(key)){
            return oauthClientId;
        }
        if("oauth_client_secret".equals(key)){
            return oauthClientSecret;
        }
        if("canvas-url".equals(key)){
            return canvasUrl;
        }
        return properties.get(key);
    }

    public void setConfigValue(String key, String value) {
        properties.put(key, value);
    }
}
