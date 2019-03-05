package com.github.mfatihercik.dsb.typeconverter;

import java.util.Map;

public class LongTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        Long nValue = null;
        if (value != null)
            nValue = Long.valueOf(value);
        return nValue;
    }

}
