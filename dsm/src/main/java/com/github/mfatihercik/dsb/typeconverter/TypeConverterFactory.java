package com.github.mfatihercik.dsb.typeconverter;

import java.util.HashMap;
import java.util.Map;

public class TypeConverterFactory {
    private static final Map<String, TypeConverter> typeConverter = new HashMap<>();

    static {
        typeConverter.put("date", new DateTypeConverter());
        typeConverter.put("int", new IntegerTypeConverter());
        typeConverter.put("integer", new IntegerTypeConverter());
        typeConverter.put("float", new FloatTypeConverter());
        typeConverter.put("short", new ShortTypeConverter());
        typeConverter.put("double", new DoubleTypeConverter());
        typeConverter.put("long", new LongTypeConverter());
        typeConverter.put("char", new CharTypeConverter());
        typeConverter.put("boolean", new BooleanTypeConverter());
        typeConverter.put("bigdecimal", new BigDecimalTypeConverter());
        typeConverter.put("biginteger", new BigIntegerTypeConverter());
    }

    public static TypeConverter getTypeConverter(String name) {
        if (name == null)
            return new DefaultTypeConverter();
        assert typeConverter.containsKey(name) : String.format("%s is not registered as type converter", name);
        return typeConverter.get(name.toLowerCase());
    }

    void registerTypeConverter(String name, TypeConverter converter) {
        typeConverter.put(name, converter);
    }
}
