package com.github.mfatihercik.dsb.typeconverter;

import java.util.Map;

public class IntegerTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        Integer nValue = null;
        if (value != null)
            nValue = Integer.valueOf(value);
        return nValue;
    }

}
