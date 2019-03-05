package com.github.mfatihercik.dsb.configloader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class JsonConfigLoaderStrategy extends AbstractConfigLoader {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonConfigLoaderStrategy(InputStream inputStream, String rootDirectory) {
        super(inputStream, rootDirectory);
    }

    public JsonConfigLoaderStrategy(String configContent, String rootDirectory) {
        super(configContent, rootDirectory);
    }

    public JsonConfigLoaderStrategy(String configContent) {
        super(configContent, null);
    }

    @Override
    public Map<String, Object> readConfiguration(InputStream inputStream) {
        TypeReference<Map<String, Object>> token = new TypeReference<Map<String, Object>>() {
        };
        try {
            return mapper.readValue(inputStream, token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
