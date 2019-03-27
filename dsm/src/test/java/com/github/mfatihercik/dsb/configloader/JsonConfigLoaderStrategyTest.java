package com.github.mfatihercik.dsb.configloader;

import com.github.mfatihercik.dsb.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class JsonConfigLoaderStrategyTest {
    @Test
    public void jacksonConfigClassLoader() throws IOException {
        Path filePath = Paths.get(TestUtils.getTestResourcePath(), "configs/loader", "yamlConfigLoaderTest.json");
        JsonConfigLoaderStrategy jsonConfigLoaderStrategy = new JsonConfigLoaderStrategy(new FileInputStream(filePath.toString()), TestUtils.getTestResourcePath());
        Map<String, Object> map = jsonConfigLoaderStrategy.readConfiguration();
        configLoaderAssert(map);
    }


    @Test
    public void yamlConfigClassLoader() throws IOException {
        Path filePath = Paths.get(TestUtils.getTestResourcePath(), "configs/loader", "yamlConfigLoaderTest.yaml");
        YamlConfigLoaderStrategy jsonConfigLoaderStrategy = new YamlConfigLoaderStrategy(new FileInputStream(filePath.toString()), TestUtils.getTestResourcePath());
        Map<String, Object> map = jsonConfigLoaderStrategy.readConfiguration();
        configLoaderAssert(map);
    }

    private void configLoaderAssert(Map<String, Object> map) {
        assertTrue(map.containsKey("sourceSystem"));
        assertTrue(map.containsKey("destinationSystem"));
        assertTrue(map.containsKey("params"));
        assertTrue(map.get("params") instanceof Map);
        assertTrue(map.containsKey("functions"));
        assertTrue(map.get("functions") instanceof Map);
        assertTrue(map.get("result") instanceof Map);
    }
}
