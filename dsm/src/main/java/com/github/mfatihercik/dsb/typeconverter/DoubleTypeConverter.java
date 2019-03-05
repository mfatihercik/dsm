package com.github.mfatihercik.dsb.typeconverter;

import java.util.Map;

public class DoubleTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        Double nValue = null;
        if (value != null)
            nValue = Double.valueOf(value);
        return nValue;
    }

}
