package com.github.mfatihercik.dsb;

import java.io.InputStream;
import java.util.Map;

public interface ConfigLoaderStrategy {
    Map<String, Object> readConfiguration();

    Map<String, Object> readConfiguration(InputStream inputStream);

    Map<String, Object> readConfiguration(String content);

    Map<String, Object> readExtendConfiguration(String content);


}
