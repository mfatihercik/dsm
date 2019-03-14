package com.github.mfatihercik.dsb;

import com.github.mfatihercik.dsb.model.ParsingElement;
import org.junit.Assert;
import org.junit.Test;

public class ParsingElementTest {

    @Test
    public void fieldNameRequired() {
        ParsingElement element = new ParsingElement();
        try {

            element.validate();
        } catch (DCMValidationException e) {
            Assert.assertTrue(e.getMessage().contains("fieldName"));
        }
    }

    @Test
    public void tagTypeRequired() {
        ParsingElement element = new ParsingElement();
        Throwable ex = null;
        try {
            element.setFieldName("name");
            element.setTagPath("name");
            Assert.assertEquals("STD", element.getTagType());
            element.setTagType(null);
            element.validate();
        } catch (DCMValidationException e) {
            ex = e;
        }
        Assert.assertNull(ex);
        Assert.assertTrue(element.getTagType().equalsIgnoreCase("STD"));
    }

    @Test
    public void tagUniqueKeyPathRequired() {
        ParsingElement element = new ParsingElement();
        element.setFieldName("name");
        element.setTagParentPath("product");
        element.setParentElement(new ParsingElement());

        element.validate();

        Assert.assertEquals("name", element.getUniqueKey());

    }

    @Test
    public void typeAdapterTest() {
//		ParsingElement element = new ParsingElement();
//		element.setFieldName("name");
//		element.setTagName("name");
//		element.setTagParentPath("product");
//		element.setTagType("object");		
//		Throwable ex = null;
//		try {
//			element.validate();
//		} catch (Throwable e) {
//			ex = e;
//		}
//		Assert.assertTrue(ex.getMessage().contains("ObjectTypes must have fields"));
//		element.addChild(new ParsingElement());
//		element.setTagTypeAdapter(null);
//		element.setTagType("STD");
//		try {
//			element.validate();
//		} catch (Throwable e) {
//			ex = e;
//		}
//		ex.printStackTrace();
//		Assert.assertTrue(ex.getMessage().contains("Only objectType can have fields"));
    }

    @Test
    public void validateBooleanField() {
        ParsingElement element = new ParsingElement();
        element.setFieldName("name");
        element.setTagParentPath("product");
        element.setParentElement(new ParsingElement());
        Throwable ex = null;


        element.setFilterExist(true);
        try {
            element.validate();
        } catch (Throwable e) {
            ex = e;
        }
        Assert.assertTrue(ex.getMessage().contains("filterExpression"));
        element.setFilterExist(false);
        element.setTransformEnabled(true);
        try {
            element.validate();
        } catch (Throwable e) {
            ex = e;
        }
        Assert.assertTrue(ex.getMessage().contains("transformationCode"));
        element.setFilterExist(false);
        element.setTransformEnabled(false);


    }
}
