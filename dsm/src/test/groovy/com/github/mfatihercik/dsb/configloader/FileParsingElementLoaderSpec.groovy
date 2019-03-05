package com.github.mfatihercik.dsb.configloader

import com.github.mfatihercik.dsb.ParsingElement
import com.github.mfatihercik.dsb.TestUtils
import com.github.mfatihercik.dsb.function.FunctionContext
import com.github.mfatihercik.dsb.function.FunctionExecutor
import com.github.mfatihercik.dsb.transformation.FileValueTransformer
import com.github.mfatihercik.dsb.transformation.TransformationElement
import com.github.mfatihercik.dsb.typeadapter.ListTypeAdapter
import com.github.mfatihercik.dsb.typeadapter.MapTypeAdapter
import com.github.mfatihercik.dsb.typeadapter.StdTypeAdapter
import org.junit.Test
import spock.lang.Specification

import java.security.InvalidParameterException

class FileParsingElementLoaderSpec extends Specification {
    private static final String pathPlace = "/configs/loader"

    def "test required field"() {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile (pathPlace, "requiredFieldTest.yaml")
        when:
        parsingElementLoader.load (true)
        then:
        thrown (InvalidParameterException.class)
    }

    def "test params field"() {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile (pathPlace, "paramsTest.yaml")
        when:
        parsingElementLoader.load (true)
        Map<String, Object> params = parsingElementLoader.getParams ()
        then:
        params.size () > 0
        params.containsKey ("dateFormat")
        params ["dateFormat"] instanceof String

        params ["dateFormat"] == "yyyyMMdd"

        params ["testTrue"] instanceof Boolean
        params ["testTrue"]

        params ["testFalse"] instanceof Boolean
        !params ["testFalse"]

    }

    def "test functions field"() {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile (pathPlace, "paramsTest.yaml")
        when:
        parsingElementLoader.load (true)
        FunctionContext context = parsingElementLoader.getFunctionContext ()
        Map<String, FunctionExecutor> functions = context.getAll ()
        then:
        functions.size () > 0
        functions ["insertProduct"]
        functions ["insertProduct"] instanceof FunctionExecutor
    }

    def "test transformation field"() {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile (pathPlace, "paramsTest.yaml")

        when:

        parsingElementLoader.load (true)
        FileValueTransformer transformer = (FileValueTransformer) parsingElementLoader.getValueTransformer ()
        Map<String, TransformationElement> transformationElementMap = transformer.getTransformationElementMap ()

        then:

        transformationElementMap.size () > 0

        when:

        TransformationElement productType = transformationElementMap.get ("PRODUCT_TYPE")

        then:

        "PRODUCT_TYPE" == productType.transformationCode
        productType.isOnlyIfExist ()
        productType.getTransformationMap ()
        !productType.transformationMap.isEmpty ()
        "BND" == productType.transformationMap ["DEFAULT"]
        null == productType.transformationMap ["NULL"]

        when:

        TransformationElement productCategory = transformationElementMap.get ("PRODUCT_CATEGORY")

        then:

        "PRODUCT_CATEGORY" == productCategory.transformationCode
        !productCategory.isOnlyIfExist ()
        productCategory.transformationMap
        !productCategory.transformationMap.isEmpty ()
        "Software" == productCategory.transformationMap ["Operation Systems"]
        null == productCategory.transformationMap ["NULL"]


    }

    def "test reviews loading"() {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile (pathPlace, "parsingElementTest.yaml")
        List<ParsingElement> load = parsingElementLoader.load (true)
        when:
        TestUtils.initXmlParsingAbsolutePath (load)
        ParsingElement root = load.get (0)

        then:
        root.getChildren ().size () == 3
        "object" == root.tagType
        MapTypeAdapter.class == root.tagTypeAdapter.class
        when:
        ParsingElement reviews = root.children [2]
        then:
        testReview (reviews)
    }


    private static void testReview(ParsingElement reviews) {
        assert "array" == reviews.tagType
        assert ListTypeAdapter.class == reviews.tagTypeAdapter.class
        assert "reviews" == reviews.fieldName
        assert "review" == reviews.tagPath
        assert "/feed/reviews" == reviews.tagAbsolutePath
        assert "insertReview" == reviews.function


        int idIndex = 0
        ParsingElement id = reviews.children [idIndex]
        assert "STD" == id.tagType
        assert StdTypeAdapter.class == id.tagTypeAdapter.class
        assert "id" == id.fieldName
        assert "id" == id.tagPath
        assert "id" == id.uniqueKey
        assert "/feed/reviews/review" == id.tagAbsolutePath


        int titleIndex = 2
        ParsingElement title = reviews.children [titleIndex]
        assert "STD" == title.tagType
        assert StdTypeAdapter.class == title.tagTypeAdapter.class
        assert "title" == title.fieldName
        assert "title" == title.tagPath
        assert "title" == title.uniqueKey
        assert "/feed/reviews/review" == title.tagAbsolutePath


        int countryIndex = 4
        ParsingElement country = reviews.getChildren ().get (countryIndex)
        assert "STD" == country.tagType
        assert StdTypeAdapter.class == country.tagTypeAdapter.class
        assert "country" == country.fieldName
        assert "country_code" == country.tagPath
        assert "country" == country.uniqueKey
        assert "/feed/reviews/review" == country.tagAbsolutePath
        assert country.transformEnabled
        assert "COUNTRIES" == country.transformationCode

    }

    def "test vendor"() {
        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile (pathPlace, "parsingElementTest.yaml")
        List<ParsingElement> load = parsingElementLoader.load (true)
        when:
        TestUtils.initXmlParsingAbsolutePath (load)
        ParsingElement root = load.get (0)
        then:
        assert root.getChildren ().size () == 3
        assert root.order == 0

        assert root.tagType == "object"
        assert root.tagTypeAdapter.class == MapTypeAdapter
        when:
        ParsingElement vendor = root.children [0]
        then:
        testFirstVendor (vendor)

        when:
        // deleted vendors
        vendor = root.children [1]
        then:
        assert vendor.order == 2
        assert "array" == vendor.tagType
        assert ListTypeAdapter.class == vendor.tagTypeAdapter.class
        assert "vendor" == vendor.fieldName
        assert "deleted_merchant" == vendor.tagPath
        assert "/feed/deleted_merchants" == vendor.tagAbsolutePath
        assert "insertVendor" == vendor.function
    }

    @Test
    def "test deleted vendor"() {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile (pathPlace, "parsingElementTest.yaml")
        List<ParsingElement> load = parsingElementLoader.load (true)
        when:
        TestUtils.initXmlParsingAbsolutePath (load)
        ParsingElement root = load.get (0)
        then:
        assert 3 == root.children.size ()
        assert 0 == root.order

        assert "object" == root.tagType
        assert MapTypeAdapter == root.tagTypeAdapter.class
        when:
        ParsingElement vendor = root.children [0]
        then:
        testFirstVendor (vendor)

        // deleted vendors
        when:
        vendor = root.children [1]
        then:
        assert 2 == vendor.order
        assert "array" == vendor.tagType
        assert ListTypeAdapter == vendor.tagTypeAdapter.class
        assert "vendor" == vendor.fieldName
        assert "deleted_merchant" == vendor.tagPath
        assert "/feed/deleted_merchants" == vendor.tagAbsolutePath
        assert "insertVendor" == vendor.function

    }

    private static void testFirstVendor(ParsingElement vendor) {
        assert 1 == vendor.order
        assert "array" == vendor.tagType
        assert ListTypeAdapter == vendor.tagTypeAdapter.class
        assert "vendor" == vendor.fieldName
        assert "merchant" == vendor.tagPath
        assert "/feed/merchants" == vendor.tagAbsolutePath
        assert "insertVendor" == vendor.function

        int idIndex = 0
        ParsingElement id = vendor.children [idIndex]
        assert 11 == id.order
        assert ("std".equalsIgnoreCase (id.getTagType ()))
        assert StdTypeAdapter == id.tagTypeAdapter.class
        assert "id" == id.fieldName
        assert "id" == id.tagPath
        assert "id" == id.uniqueKey
        assert "/feed/merchants/merchant" == id.tagAbsolutePath
        assert id.attribute
        int vendorUrlIndex = 1
        ParsingElement vendorUrl = vendor.children [vendorUrlIndex]
        assert ("std".equalsIgnoreCase (vendorUrl.tagType))
        assert StdTypeAdapter == vendorUrl.tagTypeAdapter.class
        assert "vendorUrl" == vendorUrl.fieldName
        assert "merchant_url" == vendorUrl.tagPath
        assert "vendorUrl" == vendorUrl.uniqueKey
        assert "/feed/merchants/merchant" == vendorUrl.tagAbsolutePath

        int isDeletedIndex = 5
        ParsingElement isDeleted = vendor.children [isDeletedIndex]
        assert ("std".equalsIgnoreCase (isDeleted.tagType))
        assert StdTypeAdapter.class == isDeleted.tagTypeAdapter.getClass ()
        assert "isDeleted" == isDeleted.fieldName
        assert "isDeleted" == isDeleted.tagPath
        assert "isDeleted" == isDeleted.uniqueKey
        assert "/feed/merchants/merchant" == isDeleted.tagAbsolutePath
        assert isDeleted.default
        assert "false" == isDeleted.defaultValue

        int createTimeIndex = 3
        ParsingElement createTime = vendor.children [createTimeIndex]
        assert ("std".equalsIgnoreCase (createTime.tagType))
        assert StdTypeAdapter == createTime.tagTypeAdapter.class
        assert "createTime" == createTime.fieldName
        assert "create_timestamp" == createTime.tagPath
        assert "createTime" == createTime.uniqueKey
        assert "dd.MM.yyyy" == createTime.typeParameters ["dateFormat"]
        assert "/feed/merchants/merchant" == createTime.tagAbsolutePath

        int modifiedTimeIndex = 4
        ParsingElement lastModifiedTime = vendor.children [modifiedTimeIndex]
        assert ("std".equalsIgnoreCase (lastModifiedTime.tagType))
        assert StdTypeAdapter.class == lastModifiedTime.tagTypeAdapter.class
        assert "lastModifiedTime" == lastModifiedTime.fieldName
        assert "last_update_timestamp" == lastModifiedTime.tagPath
        assert "lastModifiedTime" == lastModifiedTime.uniqueKey
        assert "yyyy-MM-dd" == lastModifiedTime.typeParameters ["dateFormat"]
        assert "/feed/merchants/merchant" == createTime.tagAbsolutePath
    }


    def "test Import external config "() {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile (pathPlace, "importYamlMain.yaml")

        List<ParsingElement> load = parsingElementLoader.load (true)
        when:
        TestUtils.initXmlParsingAbsolutePath (load)
        ParsingElement root = load.get (0)
        Map<String, Object> params = parsingElementLoader.getParams ()
        then:
        assert "yyyy-MM-dd" == params ["dateFormat"]

        when:
        FileValueTransformer transformer = (FileValueTransformer) parsingElementLoader.getValueTransformer ()
        Map<String, TransformationElement> transformationElementMap = transformer.getTransformationElementMap ()

        TransformationElement collectionMethod = transformationElementMap.get ("COLLECTION_METHOD")
        then:
        assert "COLLECTION_METHOD" == collectionMethod.transformationCode
        assert "After" == collectionMethod.transformationMap ["after_fulfillment"]
        assert "After" == collectionMethod.transformationMap ["before_fulfillment"]

        when:
        TransformationElement countries = transformationElementMap.get ("COUNTRIES")
        then:
        assert "COUNTRIES" == countries.transformationCode
        assert "Turkiye" == countries.transformationMap ["TR"]
        assert "United States" == countries.transformationMap ["US"]

        when:
        int vendorIndex = 0
        ParsingElement vendor = root.getChildren ().get (vendorIndex)
        then:
        testFirstVendor (vendor)

        when:
        ParsingElement isDeletedOverride = vendor.getChildren ().get (vendor.getChildren ().size () - 1)
        then:
        assert ("std".equalsIgnoreCase (isDeletedOverride.tagType))
        assert StdTypeAdapter == isDeletedOverride.tagTypeAdapter.class
        assert "isDeleted" == isDeletedOverride.fieldName
        assert "isDeleted" == isDeletedOverride.tagPath
        assert "isDeleted" == isDeletedOverride.uniqueKey
        assert "/feed/merchants/merchant" == isDeletedOverride.tagAbsolutePath


        testReview (root.getChildren ().get (2))
        when:
        int deletedReviewIndex = 3
        ParsingElement deletedReview = root.getChildren ().get (deletedReviewIndex)
        then:
        assert "array" == deletedReview.tagType
        assert (ListTypeAdapter == deletedReview.tagTypeAdapter.class)
        assert "reviews" == deletedReview.fieldName
        assert "deleted_review" == deletedReview.tagPath
        assert "/feed/deleted_reviews" == deletedReview.tagAbsolutePath
        assert "insertReview" == deletedReview.function

        when:
        int idIndex = 0
        ParsingElement id = deletedReview.getChildren ().get (idIndex)

        then:
        assert ("std".equalsIgnoreCase (id.getTagType ()))
        assert StdTypeAdapter.class == id.tagTypeAdapter.class
        assert "id" == id.fieldName
        assert "id" == id.tagPath
        assert "id" == id.uniqueKey
        assert "/feed/deleted_reviews/deleted_review" == id.tagAbsolutePath

        when:
        int isDeletedIndex = 1
        ParsingElement deleted = deletedReview.getChildren ().get (isDeletedIndex)
        then:
        assert ("std".equalsIgnoreCase (deleted.getTagType ()))
        assert StdTypeAdapter == deleted.tagTypeAdapter.class
        assert "isDeleted" == deleted.fieldName
        assert "isDeleted" == deleted.tagPath
        assert "isDeleted" == deleted.uniqueKey
        assert "/feed/deleted_reviews/deleted_review" == deleted.tagAbsolutePath


    }


}
