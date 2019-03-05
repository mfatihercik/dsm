package com.github.mfatihercik.dsb.transformation;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileValueTransformer implements ValueTransformer {
    static final String ERROR2 = "%s source value not exist in %s transformation code not exist.";
    private static final String ERROR1 = "%s transformation code not exist.";
    private Map<String, TransformationElement> transformationElementMap = new HashMap<>();


    public FileValueTransformer() {

    }

    public FileValueTransformer(List<TransformationElement> transformationElements) {
        super();
        for (TransformationElement transformationElement : transformationElements) {
            getTransformationElementMap().put(transformationElement.getTransformationCode(), transformationElement);
        }

    }

    protected Map<String, Object> getTransformationMap(String transformationCode) {
        return getTransformationElementMap().get(transformationCode).getTransformationMap();
    }


    public String transform(String transformationCode, String sourceValue, boolean transformOnlyIfExist) {
        if (!getTransformationElementMap().containsKey(transformationCode)) {
            throw new InvalidParameterException(String.format(ERROR1, transformationCode));
        }
        return getTransformationElementMap().get(transformationCode).transform(sourceValue);
    }

    public Map<String, TransformationElement> getTransformationElementMap() {
        return transformationElementMap;
    }

    public void setTransformationElementMap(Map<String, TransformationElement> transformationElementMap) {
        this.transformationElementMap = transformationElementMap;
    }

    @Override
    public void add(TransformationElement element) {
        getTransformationElementMap().put(element.getTransformationCode(), element);
    }

    @Override
    public void remove(String transformationCode) {
        this.transformationElementMap.remove(transformationCode);

    }


}
