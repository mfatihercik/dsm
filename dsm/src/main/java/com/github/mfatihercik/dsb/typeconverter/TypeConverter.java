package com.github.mfatihercik.dsb.typeconverter;

import java.util.Map;

public interface TypeConverter {
    Object convert(String value, Map<String, Object> params);
}
