package com.github.mfatihercik.dsb.typeconverter;

import java.util.Map;

public class ShortTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        Short nValue = null;
        if (value != null)
            nValue = Short.valueOf(value);
        return nValue;
    }

}
