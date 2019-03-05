package com.github.mfatihercik.dsb.configloader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class GsonConfigLoaderStrategy extends AbstractConfigLoader {

    private final Gson gson = new GsonBuilder().create();

    public GsonConfigLoaderStrategy(InputStream inputStream, String rootDirectory) {
        super(inputStream, rootDirectory);
    }

    public GsonConfigLoaderStrategy(String configContent, String rootDirectory) {
        super(configContent, rootDirectory);
    }

    public GsonConfigLoaderStrategy(String configContent) {
        super(configContent, null);
    }

    @Override
    public Map<String, Object> readConfiguration(InputStream inputStream) {
        TypeToken<Map<String, Object>> token = new TypeToken<Map<String, Object>>() {
        };
        return gson.fromJson(new InputStreamReader(inputStream), token.getType());
    }

}
