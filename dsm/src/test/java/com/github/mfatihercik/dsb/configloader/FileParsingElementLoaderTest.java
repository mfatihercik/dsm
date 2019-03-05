package com.github.mfatihercik.dsb.configloader;

import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.TestUtils;
import com.github.mfatihercik.dsb.function.FunctionContext;
import com.github.mfatihercik.dsb.function.FunctionExecutor;
import com.github.mfatihercik.dsb.transformation.FileValueTransformer;
import com.github.mfatihercik.dsb.transformation.TransformationElement;
import com.github.mfatihercik.dsb.typeadapter.ListTypeAdapter;
import com.github.mfatihercik.dsb.typeadapter.MapTypeAdapter;
import com.github.mfatihercik.dsb.typeadapter.StdTypeAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FileParsingElementLoaderTest {
    private static final String pathPlace = "/configs/loader";

    @Test
    public void testRequiredFields() throws IOException {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile(pathPlace, "requiredFieldTest.yaml");
        try {
            parsingElementLoader.load(true);
        } catch (InvalidParameterException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testParamsTestFields() throws IOException {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile(pathPlace, "paramsTest.yaml");
        parsingElementLoader.load(true);
        Map<String, Object> params = parsingElementLoader.getParams();
        assertTrue(params.size() > 0);
        assertTrue(params.containsKey("dateFormat"));
        assertTrue(params.get("dateFormat") instanceof String);
        assertTrue(params.get("testTrue") instanceof Boolean);
        assertTrue(params.get("testFalse") instanceof Boolean);
        assertTrue((Boolean) params.get("testTrue"));
        assertTrue(!(Boolean) params.get("testFalse"));
    }

    @Test
    public void testFunctionsTestFields()
            throws IOException {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile(pathPlace, "paramsTest.yaml");
        parsingElementLoader.load(true);
        FunctionContext context = parsingElementLoader.getFunctionContext();
        Map<String, FunctionExecutor> functions = context.getAll();
        assertTrue(functions.size() > 0);
        assertTrue(functions.containsKey("insertProduct"));
        assertNotNull(functions.get("insertProduct"));
    }

    @Test
    public void testTransformationTestFields()
            throws IOException {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile(pathPlace, "paramsTest.yaml");
        parsingElementLoader.load(true);
        FileValueTransformer transformer = (FileValueTransformer) parsingElementLoader.getValueTransformer();
        Map<String, TransformationElement> transformationElementMap = transformer.getTransformationElementMap();
        assertTrue(transformationElementMap.size() > 0);
        TransformationElement productType = transformationElementMap.get("PRODUCT_TYPE");
        assertEquals("PRODUCT_TYPE", productType.getTransformationCode());
        assertTrue(productType.isOnlyIfExist());
        assertNotNull(productType.getTransformationMap());
        assertTrue(!productType.getTransformationMap().isEmpty());
        assertEquals("BND", productType.getTransformationMap().get("DEFAULT"));
        assertNull(productType.getTransformationMap().get("NULL"));

        TransformationElement productCategory = transformationElementMap.get("PRODUCT_CATEGORY");
        assertEquals("PRODUCT_CATEGORY", productCategory.getTransformationCode());
        assertTrue(!productCategory.isOnlyIfExist());
        assertNotNull(productCategory.getTransformationMap());
        assertTrue(!productCategory.getTransformationMap().isEmpty());
        assertEquals("Software", productCategory.getTransformationMap().get("Operation Systems"));
        assertNull(productCategory.getTransformationMap().get("NULL"));

    }

    @Test
    public void testReviewLoading() throws IOException {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile(pathPlace, "parsingElementTest.yaml");
        List<ParsingElement> load = parsingElementLoader.load(true);
        TestUtils.initXmlParsingAbsolutePath(load);
        ParsingElement root = load.get(0);
        assertEquals(3, root.getChildren().size());
        assertEquals("object", root.getTagType());
        assertEquals(MapTypeAdapter.class, root.getTagTypeAdapter().getClass());
        ParsingElement reviews = root.getChildren().get(2);

        testReview(reviews);
    }

    private void testReview(ParsingElement reviews) {
        assertEquals("array", reviews.getTagType());
        assertEquals(ListTypeAdapter.class, reviews.getTagTypeAdapter().getClass());
        assertEquals("reviews", reviews.getFieldName());
        assertEquals("review", reviews.getTagPath());
        assertEquals("/feed/reviews", reviews.getTagAbsolutePath());
        assertEquals("insertReview", reviews.getFunction());

        int idIndex = 0;
        ParsingElement id = reviews.getChildren().get(idIndex);
        assertTrue("std".equalsIgnoreCase(id.getTagType()));
        assertEquals(StdTypeAdapter.class, id.getTagTypeAdapter().getClass());
        assertEquals("id", id.getFieldName());
        assertEquals("id", id.getTagPath());
        assertEquals("id", id.getUniqueKey());
        assertEquals("/feed/reviews/review", id.getTagAbsolutePath());

        int titleIndex = 2;
        ParsingElement title = reviews.getChildren().get(titleIndex);
        assertTrue("std".equalsIgnoreCase(title.getTagType()));
        assertEquals(StdTypeAdapter.class, title.getTagTypeAdapter().getClass());
        assertEquals("title", title.getFieldName());
        assertEquals("title", title.getTagPath());
        assertEquals("title", title.getUniqueKey());
        assertEquals("/feed/reviews/review", title.getTagAbsolutePath());
        int countryIndex = 4;
        ParsingElement country = reviews.getChildren().get(countryIndex);
        assertTrue("std".equalsIgnoreCase(country.getTagType()));
        assertEquals(StdTypeAdapter.class, country.getTagTypeAdapter().getClass());
        assertEquals("country", country.getFieldName());
        assertEquals("country_code", country.getTagPath());
        assertEquals("country", country.getUniqueKey());
        assertEquals("/feed/reviews/review", country.getTagAbsolutePath());
        assertTrue(country.isTransformEnabled());
        assertEquals("COUNTRIES", country.getTransformationCode());
    }

    @Test
    public void testElementLoading() throws IOException {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile(pathPlace, "parsingElementTest.yaml");
        List<ParsingElement> load = parsingElementLoader.load(true);
        TestUtils.initXmlParsingAbsolutePath(load);
        ParsingElement root = load.get(0);
        assertEquals(3, root.getChildren().size());
        assertEquals(0, root.getOrder());

        assertEquals("object", root.getTagType());
        assertEquals(MapTypeAdapter.class, root.getTagTypeAdapter().getClass());

        ParsingElement vendor = root.getChildren().get(0);
        testFirstVendor(vendor);

        // deleted vendors
        vendor = root.getChildren().get(1);
        assertEquals(2, vendor.getOrder());
        assertEquals("array", vendor.getTagType());
        assertEquals(ListTypeAdapter.class, vendor.getTagTypeAdapter().getClass());
        assertEquals("vendor", vendor.getFieldName());
        assertEquals("deleted_merchant", vendor.getTagPath());
        assertEquals("/feed/deleted_merchants", vendor.getTagAbsolutePath());
        assertEquals("insertVendor", vendor.getFunction());

    }

    private void testFirstVendor(ParsingElement vendor) {
        assertEquals(1, vendor.getOrder());
        assertEquals("array", vendor.getTagType());
        assertEquals(ListTypeAdapter.class, vendor.getTagTypeAdapter().getClass());
        assertEquals("vendor", vendor.getFieldName());
        assertEquals("merchant", vendor.getTagPath());
        assertEquals("/feed/merchants", vendor.getTagAbsolutePath());
        assertEquals("insertVendor", vendor.getFunction());

        int idIndex = 0;
        ParsingElement id = vendor.getChildren().get(idIndex);
        assertEquals(11, id.getOrder());
        assertTrue("std".equalsIgnoreCase(id.getTagType()));
        assertEquals(StdTypeAdapter.class, id.getTagTypeAdapter().getClass());
        assertEquals("id", id.getFieldName());
        assertEquals("id", id.getTagPath());
        assertEquals("id", id.getUniqueKey());
        assertEquals("/feed/merchants/merchant", id.getTagAbsolutePath());
        assertTrue(id.isAttribute());
        int vendorUrlIndex = 1;
        ParsingElement vendorUrl = vendor.getChildren().get(vendorUrlIndex);
        assertTrue("std".equalsIgnoreCase(vendorUrl.getTagType()));
        assertEquals(StdTypeAdapter.class, vendorUrl.getTagTypeAdapter().getClass());
        assertEquals("vendorUrl", vendorUrl.getFieldName());
        assertEquals("merchant_url", vendorUrl.getTagPath());
        assertEquals("vendorUrl", vendorUrl.getUniqueKey());
        assertEquals("/feed/merchants/merchant", vendorUrl.getTagAbsolutePath());

        int isDeletedIndex = 5;
        ParsingElement isDeleted = vendor.getChildren().get(isDeletedIndex);
        assertTrue("std".equalsIgnoreCase(isDeleted.getTagType()));
        assertEquals("isDeleted", isDeleted.getFieldName());
        assertEquals(StdTypeAdapter.class, isDeleted.getTagTypeAdapter().getClass());
        assertEquals("isDeleted", isDeleted.getTagPath());
        assertEquals("isDeleted", isDeleted.getUniqueKey());
        assertEquals("/feed/merchants/merchant", isDeleted.getTagAbsolutePath());
        assertTrue(isDeleted.isDefault());
        assertEquals("false", isDeleted.getDefaultValue());

        int createTimeIndex = 3;
        ParsingElement createTime = vendor.getChildren().get(createTimeIndex);
        assertTrue("std".equalsIgnoreCase(createTime.getTagType()));
        assertEquals(StdTypeAdapter.class, createTime.getTagTypeAdapter().getClass());
        assertEquals("createTime", createTime.getFieldName());
        assertEquals("create_timestamp", createTime.getTagPath());
        assertEquals("createTime", createTime.getUniqueKey());
        assertEquals("dd.MM.yyyy", createTime.getTypeParameters().get("dateFormat"));
        assertEquals("/feed/merchants/merchant", createTime.getTagAbsolutePath());

        int modifiedTimeIndex = 4;
        ParsingElement lastModifiedTime = vendor.getChildren().get(modifiedTimeIndex);
        assertTrue("std".equalsIgnoreCase(lastModifiedTime.getTagType()));
        assertEquals(StdTypeAdapter.class, lastModifiedTime.getTagTypeAdapter().getClass());
        assertEquals("lastModifiedTime", lastModifiedTime.getFieldName());
        assertEquals("last_update_timestamp", lastModifiedTime.getTagPath());
        assertEquals("lastModifiedTime", lastModifiedTime.getUniqueKey());
        assertEquals("yyyy-MM-dd", lastModifiedTime.getTypeParameters().get("dateFormat"));
        assertEquals("/feed/merchants/merchant", createTime.getTagAbsolutePath());
    }

    @Test
    public void testImportTest() throws IOException {

        FileParsingElementLoader parsingElementLoader = TestUtils.prepareParsingElementFile(pathPlace, "importYamlMain.yaml");

        List<ParsingElement> load = parsingElementLoader.load(true);
        TestUtils.initXmlParsingAbsolutePath(load);
        ParsingElement root = load.get(0);
        Map<String, Object> params = parsingElementLoader.getParams();
        assertEquals("yyyy-MM-dd", params.get("dateFormat"));

        FileValueTransformer transformer = (FileValueTransformer) parsingElementLoader.getValueTransformer();
        Map<String, TransformationElement> transformationElementMap = transformer.getTransformationElementMap();

        TransformationElement collectionMethod = transformationElementMap.get("COLLECTION_METHOD");
        assertEquals("COLLECTION_METHOD", collectionMethod.getTransformationCode());
        assertEquals("After", collectionMethod.getTransformationMap().get("after_fulfillment"));
        assertEquals("After", collectionMethod.getTransformationMap().get("before_fulfillment"));

        TransformationElement countries = transformationElementMap.get("COUNTRIES");
        assertEquals("COUNTRIES", countries.getTransformationCode());
        assertEquals("Turkiye", countries.getTransformationMap().get("TR"));
        assertEquals("United States", countries.getTransformationMap().get("US"));

        int vendorIndex = 0;
        ParsingElement vendor = root.getChildren().get(vendorIndex);
        testFirstVendor(vendor);
        ParsingElement isDeletedOverride = vendor.getChildren().get(vendor.getChildren().size() - 1);
        assertTrue("std".equalsIgnoreCase(isDeletedOverride.getTagType()));
        assertEquals(StdTypeAdapter.class, isDeletedOverride.getTagTypeAdapter().getClass());
        assertEquals("isDeleted", isDeletedOverride.getFieldName());
        assertEquals("isDeleted", isDeletedOverride.getTagPath());
        assertEquals("isDeleted", isDeletedOverride.getUniqueKey());
        assertEquals("/feed/merchants/merchant", isDeletedOverride.getTagAbsolutePath());


        testReview(root.getChildren().get(2));
        int deletedReviewIndex = 3;
        ParsingElement deletedReview = root.getChildren().get(deletedReviewIndex);

        assertEquals("array", deletedReview.getTagType());
        assertEquals(ListTypeAdapter.class, deletedReview.getTagTypeAdapter().getClass());
        assertEquals("reviews", deletedReview.getFieldName());
        assertEquals("deleted_review", deletedReview.getTagPath());
        assertEquals("/feed/deleted_reviews", deletedReview.getTagAbsolutePath());
        assertEquals("insertReview", deletedReview.getFunction());

        int idIndex = 0;
        ParsingElement id = deletedReview.getChildren().get(idIndex);
        assertTrue("std".equalsIgnoreCase(id.getTagType()));
        assertEquals(StdTypeAdapter.class, id.getTagTypeAdapter().getClass());
        assertEquals("id", id.getFieldName());
        assertEquals("id", id.getTagPath());
        assertEquals("id", id.getUniqueKey());
        assertEquals("/feed/deleted_reviews/deleted_review", id.getTagAbsolutePath());

        int isDeletedIndex = 1;
        ParsingElement deleted = deletedReview.getChildren().get(isDeletedIndex);
        assertTrue("std".equalsIgnoreCase(deleted.getTagType()));
        assertEquals(StdTypeAdapter.class, deleted.getTagTypeAdapter().getClass());
        assertEquals("isDeleted", deleted.getFieldName());
        assertEquals("isDeleted", deleted.getTagPath());
        assertEquals("isDeleted", deleted.getUniqueKey());
        assertEquals("/feed/deleted_reviews/deleted_review", deleted.getTagAbsolutePath());


    }


}
