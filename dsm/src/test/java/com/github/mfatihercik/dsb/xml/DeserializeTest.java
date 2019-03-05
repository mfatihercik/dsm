package com.github.mfatihercik.dsb.xml;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.github.mfatihercik.dsb.DCM;
import com.github.mfatihercik.dsb.DCMBuilder;
import com.github.mfatihercik.dsb.TestUtils;
import com.github.mfatihercik.dsb.model.Product;
import com.github.mfatihercik.dsb.model.SimpleOrder;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DeserializeTest {
    private static final String pathPlace = "/configs/parsing";
    private static final Path rootPath = Paths.get(TestUtils.getTestResourcePath(), pathPlace);

    @Test
    public void deserializeProductList() throws Exception {


        DCMBuilder builder = new DCMBuilder(rootPath.resolve("simpleProductDeserializeList.yaml").toFile(), rootPath.toString());

        builder.setType(DCMBuilder.TYPE.XML);
        DCM dsm = builder.create(Product.class);

        TestUtils.toJson(dsm.toObject(rootPath.resolve("simpleProduct.xml").toFile()));


        List<Product> products = (List<Product>) dsm.toObject(rootPath.resolve("simpleProduct.xml").toFile());

        assertEquals(2, products.size());
        assertEquals(Product.class, products.get(0).getClass());
        assertEquals("1234", products.get(0).getId());

    }

    @Test
    public void deserializeSingleProduct() throws IOException {


        DCMBuilder builder = new DCMBuilder(rootPath.resolve("simpleProductDeserializeSingle.yaml").toFile(), rootPath.toString());
        builder.setType(DCMBuilder.TYPE.XML);
        DCM dsm = builder.create(Product.class);

        TestUtils.toJson(dsm.toObject(rootPath.resolve("simpleProduct.xml").toFile()));

        Product product = (Product) dsm.toObject(rootPath.resolve("simpleProduct.xml").toFile());

        assertEquals(Product.class, product.getClass());

    }

    @Test
    public void deserializeSingleOrder() throws IOException {
        DCMBuilder builder = new DCMBuilder(rootPath.resolve("simpleOrder.yaml").toFile(), rootPath.toString());

        builder.setType(DCMBuilder.TYPE.XML);
        DCM dsm = builder.create(SimpleOrder.class);

        // System.out.println(TestUtils.toJson(staxParser.getRoot().toObject()));

        TypeReference<List<SimpleOrder>> type = new TypeReference<List<SimpleOrder>>() {
        };
        List<SimpleOrder> simpleOrder = (List<SimpleOrder>) dsm.toObject(rootPath.resolve("simpleOrder.xml").toFile());
        System.out.println(TestUtils.toJson(dsm.toObject(rootPath.resolve("simpleOrder.xml").toFile())));

        // assertEquals(SimpleOrder.class, simpleOrder.getClass());
        // assertTrue(simpleOrder.getMainProduct()!=null);
        // assertTrue(simpleOrder.getMainProduct().getId()!="1234");
        // assertTrue(simpleOrder.getProductList().size()==2);
        // assertTrue(simpleOrder.getProductList().get(0).getId()!="1234");

    }

    @Test
    public void deserializeSingleOrderJackson() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new XmlFactory());
        TypeReference<List<SimpleOrder>> type = new TypeReference<List<SimpleOrder>>() {
        };
        mapper.readValue(new File(TestUtils.getTestResourcePath() + pathPlace + "/" + "simpleOrder.xml"), type);
    }

}
