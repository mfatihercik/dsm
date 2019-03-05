package com.github.mfatihercik.dsb.configloader

import com.github.mfatihercik.dsb.TestUtils
import com.github.mfatihercik.dsb.configloader.GsonConfigLoaderStrategy
import com.github.mfatihercik.dsb.configloader.JsonConfigLoaderStrategy
import com.github.mfatihercik.dsb.configloader.YamlConfigLoaderStrategy
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class JsonConfigLoaderStrategySpec extends Specification {

    def "jackson Config Class Loader"() {


        Path filePath = Paths.get (TestUtils.getTestResourcePath (), "configs/loader", "yamlConfigLoaderTest.json")
        JsonConfigLoaderStrategy jsonConfigLoaderStrategy = new JsonConfigLoaderStrategy (new FileInputStream (filePath.toString ()), TestUtils.getTestResourcePath ())
        when:
        Map<String, Object> map = jsonConfigLoaderStrategy.readConfiguration ()
        then:
        configLoaderAssert (map)
    }

    def "gson Config Class Loader"() {

        when:
        Path filePath = Paths.get (TestUtils.getTestResourcePath (), "configs/loader", "yamlConfigLoaderTest.json")
        GsonConfigLoaderStrategy jsonConfigLoaderStrategy = new GsonConfigLoaderStrategy (new FileInputStream (filePath.toString ()), TestUtils.getTestResourcePath ())
        Map<String, Object> map = jsonConfigLoaderStrategy.readConfiguration ()
        then:
        configLoaderAssert (map)
    }

    def "yaml Config Class Loader"() throws URISyntaxException, IOException {
        when:
        Path filePath = Paths.get (TestUtils.getTestResourcePath (), "configs/loader", "yamlConfigLoaderTest.yaml")
        YamlConfigLoaderStrategy jsonConfigLoaderStrategy = new YamlConfigLoaderStrategy (new FileInputStream (filePath.toString ()), TestUtils.getTestResourcePath ())
        Map<String, Object> map = jsonConfigLoaderStrategy.readConfiguration ()
        then:
        configLoaderAssert (map)
    }

    private static void configLoaderAssert(Map<String, Object> map) {
        assert map.containsKey ("sourceSystem")
        assert (map.containsKey ("destinationSystem"))
        assert (map.containsKey ("params"))
        assert (map.containsKey ("functions"))
        assert (map ["params"] instanceof Map)
        assert (map ["functions"] instanceof Map)
        assert (map ["result"] instanceof Map)
    }
}
