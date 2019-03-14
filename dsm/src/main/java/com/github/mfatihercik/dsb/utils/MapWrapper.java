package com.github.mfatihercik.dsb.utils;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

public class MapWrapper {

    private final Map<String, Object> map;
    private String parent = null;

    public MapWrapper(Map<String, Object> map, String parent) {
        super();
        this.map = map;
        this.setParentFieldName(parent);
    }

    @SuppressWarnings("unchecked")
    public MapWrapper(Object map, String parent) {
        this((Map<String, Object>) map, parent);
    }

    public String toString(String fieldName, String defaultValue) {
        return getFieldOrDefault(fieldName, defaultValue);
    }

    public String toString(String fieldName, boolean required) {
        if (required)
            return getField(fieldName, true).toString();
        else
            return toString(fieldName);
    }

    public String toString(String fieldName) {
        Object field = getField(fieldName);
        if (field == null)
            return null;
        return field.toString();
    }

    public Boolean toBooleanDefault(String fieldName, Boolean defaultValue) {
        return getFieldOrDefault(fieldName, defaultValue);
    }

    public Boolean toBoolean(String fieldName, Boolean required) {
        return Boolean.valueOf(getField(fieldName, required).toString());
    }

    public Boolean toBoolean(String fieldName) {
        return Boolean.valueOf(toString(fieldName));
    }

    public Map<String, Object> toMap(String fieldName, Map<String, Object> defaultValue) {
        return getFieldOrDefault(fieldName, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public List<Object> toList(String fieldName) {
        Object field = getField(fieldName);
        if (field instanceof List<?>)
            return (List<Object>) field;
        throw new InvalidParameterException(String.format("%s field under %s is not List", fieldName, getParent()));
    }

    @SuppressWarnings("unchecked")
    public List<Object> toList(String fieldName, Boolean required) {

        Object field = getField(fieldName, required);
        if (field instanceof List<?>)
            return (List<Object>) field;
        throw new InvalidParameterException(String.format("%s field under %s is not List", fieldName, getParent()));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> toMap(String fieldName) {
        Object field = getField(fieldName);
        if (field instanceof Map<?, ?>)
            return (Map<String, Object>) field;
        throw new InvalidParameterException(String.format("%s field under %s is not Map", fieldName, getParent()));
    }

    public Boolean isExist(String fieldName) {
        return null != map.get(fieldName);
    }

    public Boolean isList(String fieldName) {
        return map.get(fieldName) instanceof List;
    }

    public Boolean isMap(String fieldName) {
        return map.get(fieldName) instanceof Map;
    }

    public Object getField(String fieldName, Boolean required) {
        Object object = map.get(fieldName);
        if (required && object == null) {
            throw new InvalidParameterException(String.format("%s key in %s is required", fieldName, getParent()));
        }
        return object;
    }

    public Object getField(String fieldName) {
        return map.get(fieldName);

    }

    @SuppressWarnings({"unchecked",})
    private Map<String, Object> getFieldOrDefault(String fieldName, Map<String, Object> defaultValue) {
        Object value = map.get(fieldName);
        if (value == null) {
            return defaultValue;
        }
        return (Map<String, Object>) value;

    }

    @SuppressWarnings("unchecked")
    private <T> T getFieldOrDefault(String fieldName, T defaultValue) {
        Object value = map.get(fieldName);
        if (value == null) {
            return defaultValue;
        }
        if (defaultValue instanceof String) {
            return (T) defaultValue.getClass().cast(value);
        }
        if (defaultValue instanceof Boolean) {
            return (T) defaultValue.getClass().cast(Boolean.valueOf(value.toString()));
        }
        if (defaultValue instanceof Integer) {
            return (T) defaultValue.getClass().cast(Integer.valueOf(value.toString()));
        }
        return (T) value;
    }

    public String getParent() {
        return parent;
    }

    public void setParentFieldName(String parent) {
        this.parent = parent;
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
