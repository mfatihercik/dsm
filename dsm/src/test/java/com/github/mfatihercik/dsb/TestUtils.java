package com.github.mfatihercik.dsb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mfatihercik.dsb.configloader.FileParsingElementLoader;
import com.github.mfatihercik.dsb.configloader.YamlConfigLoaderStrategy;
import com.github.mfatihercik.dsb.xml.AbsoluteXmlPathGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TestUtils {
    public static FileParsingElementLoader prepareParsingElementFile(String pathPlace, String filePathName) throws FileNotFoundException {
        String rootPath = getTestResourcePath();
        Path filePath = Paths.get(rootPath, pathPlace, filePathName);
        YamlConfigLoaderStrategy jsonConfigLoaderStrategy = new YamlConfigLoaderStrategy(new FileInputStream(filePath.toString()), rootPath);
        FileParsingElementLoader parsingElementLoader = new FileParsingElementLoader(jsonConfigLoaderStrategy);
        return parsingElementLoader;
    }

    public static void initXmlParsingAbsolutePath(List<ParsingElement> parsingElements) {
        PathGenerator pathGenerator = new AbsoluteXmlPathGenerator();
        for (ParsingElement parsingElement : parsingElements) {
            String generatePath = pathGenerator.generatePath(parsingElement);
            parsingElement.setTagAbsolutePath(generatePath);

        }
    }

    public static String getTestResourcePath() {
        return new File("src/test/resources").getAbsolutePath();
    }


    public static void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Before Free memory is megabytes: "
                + bytesToMegabytes(memory));

        runtime.gc();
        // Calculate the used memory
        memory = runtime.freeMemory();
        System.out.println("Used Free memory is megabytes: "
                + bytesToMegabytes(memory));
        runtime.gc();

    }

    public static String bytesToMegabytes(long memory) {
        return String.valueOf((memory / (1024 * 1024)));
    }

    public static String toJson(Object data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
