package com.github.mfatihercik.dsb.xml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.PathInfo;
import com.github.mfatihercik.dsb.StreamParser;
import com.github.mfatihercik.dsb.expression.ExpressionResolver;
import com.github.mfatihercik.dsb.function.FunctionFactory;
import com.github.mfatihercik.dsb.typeadapter.TypeAdaptor;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaxParser extends StreamParser {
    protected final Map<String, List<ParsingElement>> cacheStartEventConfigMaps = new HashMap<>();
    protected final Map<String, List<ParsingElement>> cacheEndObjectEventConfigMaps = new HashMap<>();
    protected final Map<String, List<ParsingElement>> cacheEndValueEventConfigMaps = new HashMap<>();

    public StaxParser(FunctionFactory functionFactory, ExpressionResolver expressionResolver, ObjectMapper objectMapper, Class<?> resultType) {
        super(functionFactory, expressionResolver, new AbsoluteXmlPathGenerator(), objectMapper, resultType);
    }

    @Override
    protected void parseInputStream(Reader reader) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader streamReader = factory.createXMLStreamReader(reader);
            processReader(streamReader);

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void parseInputStream(InputStream inputStream) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader streamReader = factory.createXMLStreamReader(inputStream);
            processReader(streamReader);

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }

    }

    private void processReader(XMLStreamReader streamReader) throws XMLStreamException {

        while (streamReader.hasNext()) {

            switch (streamReader.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT:
                    startElement(streamReader, null);
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    startElement(streamReader, streamReader.getLocalName());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    endElement(streamReader, streamReader.getLocalName());
                    break;
                case XMLStreamConstants.SPACE:
                case XMLStreamConstants.CHARACTERS:
                    characters(streamReader);
                    break;

                default:
                    break;
            }
            streamReader.next();
            if (XMLStreamConstants.END_DOCUMENT == streamReader.getEventType()) {
                endElement(streamReader, null);
                break;
            }

        }
    }

    @Override
    protected ParsingElement initParsingElement(ParsingElement parsingElement) {
        parsingElement = super.initParsingElement(parsingElement);
        parsingElement.setTagPath(parsingElement.getTagXmlPath());
        return parsingElement;
    }

    private void characters(XMLStreamReader streamReader) {
        if (tempValue == null)
            tempValue = streamReader.getText();
        else
            tempValue = tempValue.concat(streamReader.getText());
    }

    private void endElement(final XMLStreamReader streamReader, String tagName) {

//        final String qName = streamReader.getLocalName();

        /*
         * current key is in parent tag
         */
        final String generateKey = parentTag();

        if (tagName != null)
            parentTagRemove(tagName);

        final String parentTagPath = parentTag();
        PathInfo path = pathInfo.set(tagName, parentTagPath, generateKey);

        List<ParsingElement> endValueEventElements = getEndValueEventElements(generateKey);
        for (ParsingElement parsingElement : endValueEventElements) {

            if (parsingElement.isDefault() && parsingElement.getTagAbsolutePath().equals(generateKey))
                setDefaultValueOnNode(parsingElement, parsingElement.getDefaultValue(), path);
            else
                setValueOnNode(parsingElement, path, tempValue);

        }
        tempValue = null;

    }

    private void startElement(XMLStreamReader streamReader, String tagName) {

//        final String qName ;= streamReader.getLocalName();
        tempValue = null;
        final String parentTagPath = parentTag();
        // add tag to parent to generate new key
        if (tagName != null)
            parentTagAdd(tagName);
        final String genderedKey = parentTag();

        PathInfo path = pathInfo.set(tagName, parentTagPath, genderedKey);
        Map<String, String> attributes = null;
        List<ParsingElement> startEventElements = getStartEventElements(genderedKey);
        for (ParsingElement parsingElement : startEventElements) {
            TypeAdaptor typeAdapter = parsingElement.getTagTypeAdapter();
            if (typeAdapter.isObject()) {
                registerNewNode(parsingElement, path);
            } else {
                String value = null;
                if (parsingElement.isAttribute()) {
                    if (attributes == null) {
                        attributes = new HashMap<>();
                        for (int i = 0; i < streamReader.getAttributeCount(); i++) {
                            attributes.put(streamReader.getAttributeName(i).getLocalPart(), streamReader.getAttributeValue(i));
                        }
                    }

                    value = attributes.get(parsingElement.getTagPath());
                }
                setValueOnNode(parsingElement, path, value);
            }
        }
    }

    protected List<ParsingElement> getEndValueEventElements(String genderedKey) {

        List<ParsingElement> parsingElements = cacheEndValueEventConfigMaps.get(genderedKey);

        if (parsingElements == null) {
            parsingElements = getParsingElements(endElementConfigMaps, genderedKey);
            if (!defaultValuesElementConfigMaps.isEmpty())
                parsingElements.addAll(getParsingElements(defaultValuesElementConfigMaps, genderedKey));
            parsingElements.addAll(getParsingElements(objectParsingElementMaps, genderedKey));
            cacheEndValueEventConfigMaps.put(genderedKey, parsingElements);
        }
        return parsingElements;

    }

    protected List<ParsingElement> getStartEventElements(String genderedKey) {

        List<ParsingElement> parsingElements = cacheStartEventConfigMaps.get(genderedKey);

        if (parsingElements == null) {
            parsingElements = getParsingElements(objectParsingElementMaps, genderedKey);
            if (!startElementConfigMaps.isEmpty())
                parsingElements.addAll(getParsingElements(startElementConfigMaps, genderedKey));
            cacheStartEventConfigMaps.put(genderedKey, parsingElements);
        }
        return parsingElements;

    }

}
