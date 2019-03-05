package com.github.mfatihercik.dsb.typeconverter;

import java.util.Map;

public class BooleanTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        Boolean nValue = null;
        if (value != null)
            nValue = Boolean.valueOf(value);
        return nValue;

    }

}
