package com.github.mfatihercik.dsb.typeconverter;

import java.util.Map;

public class CharTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        Character nValue = null;
        if (value != null)
            nValue = value.charAt(0);
        return nValue;

    }

}
