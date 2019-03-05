package com.github.mfatihercik.dsb.configloader;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlConfigLoaderStrategy extends AbstractConfigLoader {

    public YamlConfigLoaderStrategy(InputStream inputStream, String rootDirectory) {
        super(inputStream, rootDirectory);
    }

    public YamlConfigLoaderStrategy(String configContent, String rootDirectory) {
        super(configContent, rootDirectory);
    }

    public YamlConfigLoaderStrategy(String configContent) {
        super(configContent, null);
    }

    @Override
    public Map<String, Object> readConfiguration(InputStream inputStream) {
        return new Yaml().load(inputStream);
    }

}
