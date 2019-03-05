package com.github.mfatihercik.dsb.typeconverter;

import java.util.Map;

public class DefaultTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        return value;
    }

}
