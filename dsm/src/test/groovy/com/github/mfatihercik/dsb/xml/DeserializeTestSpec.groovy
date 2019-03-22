package com.github.mfatihercik.dsb.xml

import com.github.mfatihercik.dsb.DCM
import com.github.mfatihercik.dsb.DCMBuilder
import com.github.mfatihercik.dsb.TestUtils
import com.github.mfatihercik.dsb.model.Product
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class DeserializeTestSpec extends Specification {

    static final String pathPlace = "/configs/parsing"
    static final Path rootPath = Paths.get (TestUtils.getTestResourcePath (), pathPlace)


    def "deserialize product list"() {

        DCMBuilder builder = new DCMBuilder (rootPath.resolve ("simpleProductDeserializeList.yaml").toFile (), rootPath.toString ())
        builder.setType (DCMBuilder.TYPE.XML)
        DCM dsm = builder.create (Product.class)

        when:

        TestUtils.toJson (dsm.toObject (rootPath.resolve ("simpleProduct.xml").toFile ()))

        List<Product> products = (List<Product>) dsm.toObject (rootPath.resolve ("simpleProduct.xml").toFile ())

        then:
        2 == products.size ()
        Product.class == products [0].getClass ()
        "1234" == products [0].id
        "Google Store" == products [0].name
    }

    def "deserialize single product"() {
        DCMBuilder builder = new DCMBuilder (rootPath.resolve ("simpleProductDeserializeSingle.yaml").toFile (), rootPath.toString ())
        builder.setType (DCMBuilder.TYPE.XML)
        DCM dsm = builder.create (Product.class)

        when:

        TestUtils.toJson (dsm.toObject (rootPath.resolve ("simpleProduct.xml").toFile ()))

        Product product = (Product) dsm.toObject (rootPath.resolve ("simpleProduct.xml").toFile ())

        then:
        Product.class == product.getClass ()
        "123" == product.id
        "Google Store" == product.name

    }


//	def "deserialize simple order"(){
//		
//			dsm Builder builder = new dsm Builder(rootPath.resolve("simpleOrder.yaml").toFile(),rootPath.toString());
//	
//			builder.setDataType(dsm Builder.TYPE.XML);
//			dsm dsm = builder.create(SimpleOrder.class);
//
//		when:
//
//			SimpleOrder simpleOrder= dsm .toObject(rootPath.resolve("simpleOrder.xml").toFile());
//		
//		then:
//			 SimpleOrder.class== simpleOrder.getClass()
//			 simpleOrder.mainProduct
//			 simpleOrder.mainProduct.id=="1234"
//			 simpleOrder.productList.size()==2
//			 simpleOrder.productList[0].id=="1234"
//		 
//
//
//	}

//	@Test
//	public void deserializeSingleOrderJackson() throws ParserConfigurationException, JsonParseException, JsonProcessingException, IOException {
//		ObjectMapper mapper = new ObjectMapper(new XmlFactory());
//		TypeReference<List<SimpleOrder>> dataType = new TypeReference<List<SimpleOrder>>() {
//		};
//		mapper.readValue(new File(TestUtils.getTestResourcePath() + pathPlace + "/" + "simpleOrder.xml"), dataType);
//	}

}
