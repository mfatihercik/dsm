package com.github.mfatihercik.dsb.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.PathInfo;
import com.github.mfatihercik.dsb.StreamParser;
import com.github.mfatihercik.dsb.expression.ExpressionResolver;
import com.github.mfatihercik.dsb.function.FunctionFactory;
import com.github.mfatihercik.dsb.typeadapter.TypeAdaptor;
import com.github.mfatihercik.dsb.utils.PathUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class JsonStreamParser extends StreamParser {

    protected final Map<String, List<ParsingElement>> cacheStartEventConfigMaps = new HashMap<>();
    protected final Map<String, List<ParsingElement>> cacheEndObjectEventConfigMaps = new HashMap<>();
    protected final Map<String, List<ParsingElement>> cacheEndValueEventConfigMaps = new HashMap<>();


    public JsonStreamParser(FunctionFactory functionFactory, ExpressionResolver expressionResolver, ObjectMapper objectMapper, Class<?> resultType) {
        super(functionFactory, expressionResolver, new AbsoluteJsonPathGenerator(), objectMapper, resultType);
    }

    @Override
    protected boolean isStartElement(ParsingElement parsingElement) {
        return parsingElement.getTagTypeAdapter().isStartElement();
    }

    @Override
    protected String getAttributePath(ParsingElement parsingElement) {
        return generateKey(parsingElement.getTagPath(), parsingElement.getTagAbsolutePath());
    }


    protected void startObject(String qName) {
        tempValue = null;
        String parentTagPath = parentTag();
        if (qName != null)
            parentTagAdd(qName);
        String generateKey = parentTag();

        PathInfo path = pathInfo.set(qName, parentTagPath, generateKey);
        List<ParsingElement> startEventElements = getStartEventElements(generateKey);
        for (ParsingElement parsingElement : startEventElements) {
            TypeAdaptor typeAdapter = parsingElement.getTagTypeAdapter();
            if (typeAdapter.isObject()) {
                registerNewNode(parsingElement, path);
            } else {
                setValueOnNode(parsingElement, path, null);
            }
        }
    }

    protected void endValue(String qName, String value) {
        String parentTagPath = parentTag();
        if (qName != null)
            parentTagAdd(qName);
        String genderedKey = parentTag();

        PathInfo path = pathInfo.set(qName, parentTagPath, genderedKey);
        List<ParsingElement> valueEventElements = getEndValueEventElements(genderedKey);
        List<ParsingElement> objectParsingElements = null;
        for (ParsingElement parsingElement : valueEventElements) {
            TypeAdaptor typeAdaptor = parsingElement.getTagTypeAdapter();
            if (typeAdaptor.isObject()) {
                if (objectParsingElements == null)
                    objectParsingElements = new LinkedList<>();
                objectParsingElements.add(parsingElement);
                registerNewNode(parsingElement, path);
            } else {
                if (parsingElement.isDefault() && parsingElement.getTagAbsolutePath().equals(genderedKey))
                    setDefaultValueOnNode(parsingElement, parsingElement.getDefaultValue(), path);
                else
                    setValueOnNode(parsingElement, path, value);
            }
        }
        if (objectParsingElements != null)
            setValueForParsingElements(objectParsingElements, value, path);

        if (qName != null)
            parentTagRemove(qName);
    }

    protected void endObject(String qName) {

        String genderedKey = parentTag();
        PathInfo path = pathInfo.set(qName, genderedKey, genderedKey);
        List<ParsingElement> endObjectEventElements = getEndObjectEventElements(genderedKey);
        for (ParsingElement parsingElement : endObjectEventElements) {
            TypeAdaptor typeAdaptor = parsingElement.getTagTypeAdapter();
            if (typeAdaptor.isObject()) {
                setValueOnNode(parsingElement, path, null);
            } else {
                setDefaultValueOnNode(parsingElement, parsingElement.getDefaultValue(), path);
            }
        }

        if (qName != null)
            parentTagRemove(qName);
    }

    protected List<ParsingElement> getEndValueEventElements(String genderedKey) {

        List<ParsingElement> parsingElements = cacheEndValueEventConfigMaps.get(genderedKey);

        if (parsingElements == null) {
            parsingElements = getParsingElements(objectParsingElementMaps, genderedKey);
            parsingElements.addAll(getParsingElements(endElementConfigMaps, genderedKey));
            if (!defaultValuesElementConfigMaps.isEmpty())
                parsingElements.addAll(getParsingElements(defaultValuesElementConfigMaps, genderedKey));
            cacheEndValueEventConfigMaps.put(genderedKey, parsingElements);
        }
        return parsingElements;

    }

    protected List<ParsingElement> getEndObjectEventElements(String genderedKey) {

        List<ParsingElement> parsingElements = cacheEndObjectEventConfigMaps.get(genderedKey);

        if (parsingElements == null) {
            parsingElements = getParsingElements(defaultValuesElementConfigMaps, genderedKey);
            parsingElements.addAll(getParsingElements(objectParsingElementMaps, genderedKey));
            cacheEndObjectEventConfigMaps.put(genderedKey, parsingElements);
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

    protected void setValueForParsingElements(List<ParsingElement> endParsingElements, String value, PathInfo pathInfo) {
        for (ParsingElement parsingElement : endParsingElements) {
            setValueOnNode(parsingElement, pathInfo, value);
        }
    }

    @Override
    protected String generateKey(ParsingElement parsingElement) {
        return PathUtils.removeDuplicateSlash(super.generateKey(parsingElement));
    }

}
