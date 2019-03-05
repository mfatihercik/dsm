package com.github.mfatihercik.dsb.typeconverter;

import java.math.BigDecimal;
import java.util.Map;

public class BigIntegerTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        BigDecimal nValue = null;
        if (value != null)
            nValue = new BigDecimal(value);
        return nValue;

    }

}
