package com.github.mfatihercik.dsb.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.github.mfatihercik.dsb.DSM;
import com.github.mfatihercik.dsb.DSMBuilder;
import com.github.mfatihercik.dsb.TestUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StaxParserTest {
    private static final String pathPlace = "/configs/parsing";
    private static final Path rootPath = Paths.get(TestUtils.getTestResourcePath(), pathPlace);

    public static void googleMerchantTEst(Object data) {

        assertTrue(data instanceof Map);
        Map<String, Object> map = (Map<String, Object>) data;
        assertEquals(3, map.size());

        int vendorIndex = 0;
        List<Map<String, Object>> vendorList = (List<Map<String, Object>>) map.get("vendor");
        assertEquals(3, vendorList.size());

        int googleIndex = 0;
        Map<String, Object> googleStore = vendorList.get(googleIndex);
        assertTrue(!Boolean.valueOf(googleStore.get("isDeleted").toString()));
        assertTrue(googleStore.get("createTime") instanceof Date);
        assertEquals("2739|Google Store", googleStore.get("vendorUniqueId").toString());
        assertEquals("executed", googleStore.get("function").toString());
        assertEquals("Ada", googleStore.get("reviewerName").toString());
        assertEquals("true", googleStore.get("hasReview").toString());
        assertEquals("Google Store", googleStore.get("name").toString());

        int deletedIndex = 2;
        Map<String, Object> deletedStore = vendorList.get(deletedIndex);
        assertTrue(Boolean.valueOf(deletedStore.get("isDeleted").toString()));
        assertTrue(deletedStore.get("lastModifiedTime") instanceof Date);

        int reviewIndex = 1;
        List<Map<String, Object>> reviewsList = (List<Map<String, Object>>) map.get("reviews");
        assertEquals(2, reviewsList.size());

        int review1Index = 0;
        Map<String, Object> review1 = reviewsList.get(review1Index);
        assertTrue(!Boolean.valueOf(review1.get("isDeleted").toString()));
        assertTrue(review1.get("lastModifiedTime") instanceof Date);
        assertEquals("United States", review1.get("country").toString());
        assertEquals("5", review1.get("rating").toString());
        assertEquals("After", review1.get("collectionMethod").toString());
        assertEquals("Google Store", review1.get("vendorName").toString());
        assertEquals("Gold", review1.get("importance").toString());
        assertEquals("2738", review1.get("deferedVendorId").toString());

        List<String> collectionMethod = (List<String>) map.get("collectionMethod");
        assertEquals(3, collectionMethod.size());

        assertTrue(map.containsKey("vendor"));
        List<Map<String, Object>> vendors = (List<Map<String, Object>>) map.get("vendor");
        assertEquals(3, vendors.size());
        Map<String, Object> googleStoreMap = vendors.get(googleIndex);
        assertTrue(!(Boolean) googleStoreMap.get("isDeleted"));
        assertTrue(googleStoreMap.get("createTime") instanceof Date);
        assertEquals("2739|Google Store", googleStoreMap.get("vendorUniqueId").toString());

        assertTrue(map.containsKey("collectionMethod"));
        List<String> collectionMethodMap = (List<String>) map.get("collectionMethod");
        assertEquals("unsolicited", collectionMethodMap.get(0));

    }

    public static void petStoreTest(Object object) {
        assertTrue(object instanceof List);
        List<Map<String, Object>> list = (List<Map<String, Object>>) object;
        Map<String, Object> petStore = list.get(0);
        assertEquals(8L, petStore.get("id"));
        assertEquals("Lion 2", petStore.get("name"));
        assertEquals("available", petStore.get("status"));

        assertTrue(petStore.get("category") instanceof Map);
        Map<String, Object> category = (Map<String, Object>) petStore.get("category");
        assertEquals("Lions", category.get("name"));
        assertEquals(4L, category.get("id"));

        assertTrue(petStore.get("tags") instanceof List);
        List<Map<String, Object>> tags = (List<Map<String, Object>>) petStore.get("tags");
        assertEquals(2, tags.size());
        assertEquals(1, tags.get(0).get("id"));
        assertEquals("tag2", tags.get(0).get("name"));

//		assertTrue(petStore.get("tagIds") instanceof List);
//		List<Object> tagIds = (List<Object>) petStore.get("tagIds");
//		assertEquals(2, tagIds.size());
//		assertEquals(1L, tagIds.get(0));
//		assertEquals(2L, tagIds.get(1));
    }

    @Test
    public void test() throws IOException {


        DSMBuilder builder = new DSMBuilder(rootPath.resolve("SaxParsingHandlerTest.yaml").toFile(), rootPath.toString());

        builder.setType(DSMBuilder.TYPE.XML);
        DSM dsm = builder.create();
        Object data = dsm.toObject(rootPath.resolve("google-merchant-review.xml").toFile());

        System.out.println(TestUtils.toJson(data));

        googleMerchantTEst(data);

    }

    @Test
    public void odooSaleOrder() throws IOException {


        DSMBuilder builder = new DSMBuilder(rootPath.resolve("odoo-sale.yaml").toFile(), rootPath.toString());

        builder.setType(DSMBuilder.TYPE.XML);
        DSM dsm = builder.create();
        Map<String, Object> data = (Map<String, Object>) dsm.toObject(rootPath.resolve("odoo-sale.xml").toFile());

        System.out.println(TestUtils.toJson(data));
        assertEquals(2, data.size());
        assertTrue(data.containsKey("saleLines"));
        List<Map<String, Object>> saleLines = (List<Map<String, Object>>) data.get("saleLines");
        assertEquals(2, saleLines.size());
        Map<String, Object> first = saleLines.get(0);
        assertEquals("sale.order.line", first.get("model").toString());
        assertEquals("sale_order_1", first.get("orderId").toString());
        assertEquals("Laptop E5023", first.get("name").toString());
        assertEquals("product.product_product_25", first.get("productId").toString());
        assertEquals("3", first.get("productUomQty").toString());
        assertEquals("2950.0", first.get("priceUnit").toString());

        Map<String, Object> second = saleLines.get(1);
        assertEquals("sale.order.line", second.get("model").toString());
        assertEquals("sale_order_1", second.get("orderId").toString());
        assertEquals("Pen drive, 16GB", second.get("name").toString());
        assertEquals("product.product_product_30", second.get("productId").toString());
        assertEquals("5", second.get("productUomQty").toString());
        assertEquals("145.0", second.get("priceUnit").toString());

        assertTrue(data.containsKey("order"));
        Map<String, Object> order = (Map<String, Object>) data.get("order");

        assertEquals("sale.order", order.get("model").toString());
        assertEquals("base.res_partner_2", order.get("partnerId").toString());
        assertEquals("base.res_partner_2", order.get("partnerInvoiceId").toString());

    }

    @Test
    public void petStoreXml() throws IOException {

        DSMBuilder builder = new DSMBuilder(rootPath.resolve("pet-store.yaml").toFile(), rootPath.toString());


        builder.setType(DSMBuilder.TYPE.XML);
        DSM dsm = builder.create();
        Object object = dsm.toObject(rootPath.resolve("swagger-pet-store.xml").toFile());

        List<Map<String, Object>> list = (List<Map<String, Object>>) object;
        Map<String, Object> petStore = list.get(0);
//		System.out.println(TestUtils.toJson(petStore));
//		petStoreTest(object);
//		// Get the Java runtime
//
//		TestUtils.logMemoryUsage();

    }

    @Test
    public void petStoreJaksonXml() throws IOException {
        for (int i = 0; i < 1; i++) {
            ObjectMapper mapper = new ObjectMapper(new XmlFactory());

            List readValue = mapper.readValue(new File(TestUtils.getTestResourcePath() + pathPlace + "/" + "swagger-pet-store.xml"), List.class);
//			 TestUtils.logMemoryUsage();
        }
        // System.out.println(readValue);
    }

}
