package com.github.mfatihercik.dsb.json

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mfatihercik.dsb.DSM
import com.github.mfatihercik.dsb.DSMBuilder
import com.github.mfatihercik.dsb.TestMemoryLog
import com.github.mfatihercik.dsb.TestUtils
import com.github.mfatihercik.dsb.model.Pet
import com.github.mfatihercik.dsb.xml.StaxParserSpec
import com.github.mfatihercik.dsb.xml.StaxParserTest
import org.junit.Test
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class JsonParserSpec extends Specification {

    private static final String pathPlace = "/configs/parsing"
    private static final Path rootPath = Paths.get (TestUtils.getTestResourcePath (), pathPlace)

    def "pet Store Json parsing"() throws IOException {

        TestMemoryLog memoryLog = new TestMemoryLog ()
        memoryLog.logBefore ()

        Path rootPath = Paths.get (TestUtils.getTestResourcePath (), pathPlace)

        DSMBuilder builder = new DSMBuilder (rootPath.resolve ("pet-store.yaml").toFile (), rootPath.toString ())
        DSM dsm = builder.create ()


        when:
        def object = dsm.toObject (rootPath.resolve ("swagger-pet-store.json").toFile ())
//
        memoryLog.logAfter ()
        memoryLog.print ()
        memoryLog.gc ()

        then:
        StaxParserSpec.petStoreTest (object)

    }

    @Test
    void test() throws IOException {

        DSMBuilder builder = new DSMBuilder (rootPath.resolve ("SaxParsingHandlerTest.yaml").toFile (), rootPath.toString ())

        builder.setType (DSMBuilder.TYPE.JSON)
        DSM dsm = builder.create ()
        Object data = dsm.toObject (rootPath.resolve ("google-merchant-review.json").toFile ())


        StaxParserTest.googleMerchantTEst (data)
    }

    @Test
    void petStoreJaksonJson() throws IOException {
        TestMemoryLog memoryLog = new TestMemoryLog ()
        memoryLog.logBefore ()
        long strat = System.currentTimeMillis ()
        // for (int i = 0; i < 1; i++) {
        ObjectMapper mapper = new ObjectMapper ()
        List<Pet> value = mapper.readValue (new File (TestUtils.getTestResourcePath () + pathPlace + "/" + "swagger-pet-store.json"), new TypeReference<List<Pet>> () {
        })
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
        memoryLog.logAfter ()
        memoryLog.print ()
        // }
        // System.out.println("parse jackson end:" + (System.currentTimeMillis() -
        // strat));
    }

}
