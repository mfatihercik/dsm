package com.github.mfatihercik.dsb

import com.github.mfatihercik.dsb.model.ParsingElement
import org.junit.Test
import spock.lang.Specification

class ParsingElementSpec extends Specification {


    def "parsingElement fieldName is  required"() {
        ParsingElement element = new ParsingElement ()

        when:
        element.validate ()

        then:
        def error = thrown (DCMValidationException)
        error.getMessage ().contains ("fieldName")

    }


    def "parsingElement tagType is Required"() {

        ParsingElement element = new ParsingElement ()

        when:
        element.fieldName = "name"
        element.tagPath = "name"
        then:
        "STD" == element.tagType

        when:
        element.tagType = null
        element.validate ()

        then:
        notThrown (DCMValidationException)
        element.tagType.equalsIgnoreCase ("STD")

    }


    def "ParsingElement tag Unique Key Path Required"() {
        ParsingElement element = new ParsingElement ()
        element.fieldName = "name"
        element.tagParentPath = "product"
        element.parentElement = new ParsingElement ()

        when:
        element.validate ()

        then:
        element.getUniqueKey () == "name"

    }

    @Test
    void typeAdapterTest() {
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


    def "validate Boolean Field"() {
        ParsingElement element = new ParsingElement ()
        element.setFieldName ("name")
        element.setTagParentPath ("product")
        element.setParentElement (new ParsingElement ())
        when:


        element.setFilterExist (true)
        element.validate ()

        then:
        def error = thrown (DCMValidationException)
        error.message.contains ("filterExpression")

        when:

        element.setFilterExist (false)
        element.setTransformEnabled (true)
        element.validate ()

        then:
        error = thrown (DCMValidationException)
        error.message.contains ("transformationCode")


    }
}
