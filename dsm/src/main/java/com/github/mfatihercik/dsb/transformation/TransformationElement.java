package com.github.mfatihercik.dsb.transformation;

import com.github.mfatihercik.dsb.utils.StringUtils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TransformationElement {
    static final String ERROR1 = "%s transformation code not exist.";
    static final String ERROR2 = "%s source value not exist in %s transformation code not exist.";
    private static final String DEFAULT_KEY = "DEFAULT";
    private boolean onlyIfExist;
    private String transformationCode;
    private Map<String, Object> transformationMap = new HashMap<>();

    public String transform(String sourceValue) {
        String originalSourceValue = sourceValue;
        Object destinationValue = null;
        if (StringUtils.isBlank(sourceValue)) {
            sourceValue = "null";
        }
        Map<String, Object> map = getTransformationMap();
        if (map.containsKey(sourceValue)) {
            destinationValue = map.get(sourceValue);
        } else if (isOnlyIfExist()) {
            destinationValue = originalSourceValue;
        }
        if (destinationValue == null && getTransformationMap().containsKey(DEFAULT_KEY)) {
            destinationValue = map.get(DEFAULT_KEY);
        }
        if (destinationValue == null) {
            throw new InvalidParameterException(String.format(ERROR2, sourceValue, getTransformationCode()));

        }
        if ("null".equalsIgnoreCase(destinationValue.toString())) {
            return null;
        }
        return destinationValue.toString();
    }

    public boolean isOnlyIfExist() {
        return onlyIfExist;
    }

    public void setOnlyIfExist(boolean onlyIfExist) {
        this.onlyIfExist = onlyIfExist;
    }

    public String getTransformationCode() {
        return transformationCode;
    }

    public void setTransformationCode(String transformationCode) {
        this.transformationCode = transformationCode;
    }


    public Map<String, Object> getTransformationMap() {
        return transformationMap;
    }

    public void setTransformationMap(Map<String, Object> transformationMap) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Entry<String, Object> keyVal : transformationMap.entrySet()) {
            map.put(String.valueOf(keyVal.getKey()), keyVal.getValue());

        }
        this.transformationMap = map;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (onlyIfExist ? 1231 : 1237);
        result = prime * result + ((transformationCode == null) ? 0 : transformationCode.hashCode());
        result = prime * result + ((transformationMap == null) ? 0 : transformationMap.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransformationElement other = (TransformationElement) obj;
        if (onlyIfExist != other.onlyIfExist)
            return false;
        if (transformationCode == null) {
            if (other.transformationCode != null)
                return false;
        } else if (!transformationCode.equals(other.transformationCode))
            return false;
        if (transformationMap == null) {
            return other.transformationMap == null;
        } else return transformationMap.equals(other.transformationMap);
    }


}
