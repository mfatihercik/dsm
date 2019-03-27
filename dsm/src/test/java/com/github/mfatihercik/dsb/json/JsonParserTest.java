package com.github.mfatihercik.dsb.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mfatihercik.dsb.DCM;
import com.github.mfatihercik.dsb.DCMBuilder;
import com.github.mfatihercik.dsb.TestMemoryLog;
import com.github.mfatihercik.dsb.TestUtils;
import com.github.mfatihercik.dsb.model.Pet;
import com.github.mfatihercik.dsb.xml.StaxParserTest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JsonParserTest {

    private static final String pathPlace = "/configs/parsing";
    private static final Path rootPath = Paths.get(TestUtils.getTestResourcePath(), pathPlace);

    @Test
    public void petStoreJson() throws IOException {


        TestMemoryLog memoryLog = new TestMemoryLog();
        memoryLog.logBefore();
        Path rootPath = Paths.get(TestUtils.getTestResourcePath(), pathPlace);

        DCMBuilder builder = new DCMBuilder(rootPath.resolve("pet-store.yaml").toFile(), rootPath.toString());

        builder.setType(DCMBuilder.TYPE.JSON);
        DCM dsm = builder.create(Pet.class);


        Object object = dsm.toObject(rootPath.resolve("swagger-pet-store.json").toFile());

        memoryLog.logAfter();
        memoryLog.print();
        TestMemoryLog.gc();
//
//		List<Object> list = (List<Object>) object;
//		Object petStore = list.get(0);
//		System.out.println(TestUtils.toJson(petStore));
        //
        // StaxParserTest.petStoreTest(object);

    }

    @Test
    public void test() throws IOException {

        DCMBuilder builder = new DCMBuilder(rootPath.resolve("SaxParsingHandlerTest.yaml").toFile(), rootPath.toString());

        builder.setType(DCMBuilder.TYPE.JSON);
        DCM dsm = builder.create();
        Object data = dsm.toObject(rootPath.resolve("google-merchant-review.json").toFile());

        System.out.println(TestUtils.toJson(data));

        StaxParserTest.googleMerchantTEst(data);
    }

    @Test
    public void petStoreJaksonJson() throws IOException {
        TestMemoryLog memoryLog = new TestMemoryLog();
        memoryLog.logBefore();
        long strat = System.currentTimeMillis();
        // for (int i = 0; i < 1; i++) {
        ObjectMapper mapper = new ObjectMapper();
        List<Pet> value = mapper.readValue(new File(TestUtils.getTestResourcePath() + pathPlace + "/" + "swagger-pet-store.json"), new TypeReference<List<Pet>>() {
        });
        // List value = mapper.readValue(new File(TestUtils.getTestResourcePath() +
        // pathPlace + "/" + "swagger-pet-store.json"), List.class);

        // int j = 0;
        // for (Iterator iterator = value.iterator(); iterator.hasNext();) {
        // iterator.next();
        // if (j % 3 == 0) {
        // iterator.remove();
        // }
        //
        // }
        // TestUtils.logMemoryUsage();
        memoryLog.logAfter();
        memoryLog.print();
        // }
        // System.out.println("parse jackson end:" + (System.currentTimeMillis() -
        // strat));
    }

}
