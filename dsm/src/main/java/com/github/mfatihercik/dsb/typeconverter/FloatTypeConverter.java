package com.github.mfatihercik.dsb.typeconverter;

import java.util.Map;

public class FloatTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        Float nValue = null;
        if (value != null)
            nValue = Float.valueOf(value);
        return nValue;
    }

}
