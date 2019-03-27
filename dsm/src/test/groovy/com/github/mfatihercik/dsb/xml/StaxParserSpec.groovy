package com.github.mfatihercik.dsb.xml

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.github.mfatihercik.dsb.DSM
import com.github.mfatihercik.dsb.DSMBuilder
import com.github.mfatihercik.dsb.TestUtils
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class StaxParserSpec extends Specification {
    private static final String pathPlace = "/configs/parsing"
    private static final Path rootPath = Paths.get (TestUtils.getTestResourcePath (), pathPlace)

    def "google merchant review test()"() {


        DSMBuilder builder = new DSMBuilder (rootPath.resolve ("SaxParsingHandlerTest.yaml").toFile (), rootPath.toString ())
        builder.setType (DSMBuilder.TYPE.XML)
        DSM dsm = builder.create ()
        when:
        Object data = dsm.toObject (rootPath.resolve ("google-merchant-review.xml").toFile ())

        then:
        googleMerchantTest (data)

    }


    private static void googleMerchantTest(Object data) {

        assert data instanceof Map

        Map<String, Object> map = (Map<String, Object>) data

        assert true
        assert map.size () == 3


        List<Map<String, Object>> vendorList = (List<Map<String, Object>>) map.get ("vendor")


        assert (vendorList.size () == 3)

        int googleIndex = 0
        Map<String, Object> googleStore = vendorList.get (googleIndex)

        assert googleStore ["isDeleted"] instanceof Boolean
        assert !(googleStore ["isDeleted"] as boolean)


        assert googleStore ["createTime"] instanceof Date
        assert "2739|Google Store" == googleStore ["vendorUniqueId"]
        assert "executed" == googleStore ["function"]
        assert "Ada" == googleStore ["reviewerName"]
        assert "true" == googleStore ["hasReview"]

        int deletedIndex = 2
        Map<String, Object> deletedStore = vendorList.get (deletedIndex)

        deletedStore ["isDeleted"]
        deletedStore ["isDeleted"] as boolean
        assert deletedStore ["lastModifiedTime"] instanceof Date

        List<Map<String, Object>> reviewsList = (List<Map<String, Object>>) map.get ("reviews")
        assert reviewsList.size () == 2

        int review1Index = 0
        Map<String, Object> review1 = reviewsList.get (review1Index)

        review1 ["isDeleted"]
        review1 ["isDeleted"].asBoolean ()


        assert review1 ["lastModifiedTime"] instanceof Date
        assert review1 ["country"].toString () == "United States"
        assert review1 ["rating"].toString ().equals ("5")
        assert review1 ["collectionMethod"].toString ().equals ("After")
        assert "Google Store" == review1 ["vendorName"].toString ()
        assert "Gold" == review1 ["importance"].toString ()

        List<String> collectionMethod = (List<String>) map.get ("collectionMethod")

        assert 3 == collectionMethod.size ()
        assert map ["vendor"]

        List<Map<String, Object>> vendors = (List<Map<String, Object>>) map.get ("vendor")

        assert 3 == vendors.size ()

        Map<String, Object> googleStoreMap = vendors.get (googleIndex)


        assert googleStoreMap ["isDeleted"] instanceof Boolean
        assert !googleStoreMap ["isDeleted"]


        assert googleStoreMap.get ("createTime") instanceof Date
        assert "2739|Google Store" == googleStoreMap ["vendorUniqueId"]
        assert map ["collectionMethod"]

        List<String> collectionMethodMap = (List<String>) map.get ("collectionMethod")
        assert "unsolicited" == collectionMethodMap [0]

    }


    def "test odoo Sale Order"() {


        DSMBuilder builder = new DSMBuilder (rootPath.resolve ("odoo-sale.yaml").toFile (), rootPath.toString ())

        builder.setType (DSMBuilder.TYPE.XML)
        DSM dsm = builder.create ()
        when:
        Map<String, Object> data = (Map<String, Object>) dsm.toObject (rootPath.resolve ("odoo-sale.xml").toFile ())

        then:
        assert (data.size () == 2)
        assert (data.containsKey ("saleLines"))
        List<Map<String, Object>> saleLines = (List<Map<String, Object>>) data.get ("saleLines")
        assert (2 == saleLines.size ())
        Map<String, Object> first = saleLines [0]
        assert "sale.order.line" == first ["model"]
        assert "sale_order_1" == first ["orderId"]
        assert "Laptop E5023" == first ["name"]
        assert "product.product_product_25" == first ["productId"]
        assert "3" == first ["productUomQty"]
        assert 2950.0 == first ["priceUnit"]

        Map<String, Object> second = saleLines.get (1)
        assert "sale.order.line" == second ["model"]
        assert "sale_order_1" == second ["orderId"]
        assert "Pen drive, 16GB" == second ["name"]
        assert "product.product_product_30" == second ["productId"]
        assert "5" == second ["productUomQty"]
        assert 145.0 == second ["priceUnit"]

        assert data ["order"]
        Map<String, Object> order = (Map<String, Object>) data.get ("order")

        assert "sale.order" == order ["model"]
        assert "base.res_partner_2" == order ["partnerId"]
        assert "base.res_partner_2" == order ["partnerInvoiceId"]

    }


    def "pet store example xml "() {

        DSMBuilder builder = new DSMBuilder (rootPath.resolve ("pet-store.yaml").toFile (), rootPath.toString ())
        DSM dsm = builder.setType (DSMBuilder.TYPE.XML).create ()

        when:
        Object object = dsm.toObject (rootPath.resolve ("swagger-pet-store.xml").toFile ())

        then:
        petStoreTest (object)


    }


    def "pet Store Jackson Xml"() {
        when:
        ObjectMapper mapper = new ObjectMapper (new XmlFactory ())
        List readValue = mapper.readValue (new File (TestUtils.getTestResourcePath () + pathPlace + "/" + "swagger-pet-store.xml"), List.class)
        then:
        assert readValue
    }

    static void petStoreTest(Object object) {
        assert object instanceof List
        List<Map<String, Object>> list = (List<Map<String, Object>>) object
        Map<String, Object> petStore = list.get (0)
        assert 8L == petStore ["id"]
        assert "Lion 2" == petStore ["name"]
        assert "available" == petStore ["status"]

        assert petStore ["category"] instanceof Map
        Map<String, Object> category = (Map<String, Object>) petStore.get ("category")
        assert "Lions" == category ["name"]
        assert 4L == category ["id"]


        assert petStore ["tags"] instanceof List
        List<Map<String, Object>> tags = (List<Map<String, Object>>) petStore.get ("tags")
        assert 4 == tags.size ()
        assert 1 == tags [0] ["id"]
        assert "tag2" == tags [0] ["name"]

        assert petStore ["tagIds"] instanceof List
        List<Object> tagIds = (List<Object>) petStore.get ("tagIds")
        assert 4 == tagIds.size ()
        assert 1L == tagIds.get (0)
        assert 2L == tagIds.get (1)
    }
}
