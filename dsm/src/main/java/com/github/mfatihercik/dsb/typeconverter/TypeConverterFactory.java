
package com.github.mfatihercik.dsb.typeconverter;

import com.github.mfatihercik.dsb.DSMValidationException;

import java.util.HashMap;
import java.util.Map;


public class TypeConverterFactory implements Cloneable {


    /**
     * {@link TypeConverter} name and instance mapper.
     */
    protected final Map<String, TypeConverter> typeConverter = new HashMap<>();

    public TypeConverterFactory() {
        registerDefaultConverter();
    }

    /**
     * initialize {@link TypeConverterFactory} with given map.
     * this constructor used for cloning.
     *
     * @param converterMap *
     */
    private TypeConverterFactory(Map<String, TypeConverter> converterMap) {
        typeConverter.putAll(converterMap);
    }

    protected void registerDefaultConverter() {
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
        typeConverter.put("default", new DefaultTypeConverter());
    }

    /**
     * Get {@link TypeConverter} instance with name.
     *
     * @param name of the type adapter
     * @return TypeConverter instance
     * @throws DSMValidationException if requested type adapter is not registered
     */
    public TypeConverter getTypeConverter(String name) {
        TypeConverter converter = this.typeConverter.get(name.toLowerCase());
        if (converter == null) {
            throw new DSMValidationException(String.format("%s is not registered as type converter", name));
        }
        return converter;
    }

    public void register(String name, TypeConverter converter) {
        typeConverter.put(name, converter);

    }

    @Override
    public TypeConverterFactory clone() {
        return new TypeConverterFactory(this.typeConverter);
    }


}
