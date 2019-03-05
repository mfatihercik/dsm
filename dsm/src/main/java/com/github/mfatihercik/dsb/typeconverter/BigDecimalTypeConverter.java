package com.github.mfatihercik.dsb.typeconverter;

import java.math.BigInteger;
import java.util.Map;

public class BigDecimalTypeConverter implements TypeConverter {

    @Override
    public Object convert(String value, Map<String, Object> params) {
        BigInteger nValue = null;
        if (value != null)
            nValue = new BigInteger(value);
        return nValue;

    }

}
