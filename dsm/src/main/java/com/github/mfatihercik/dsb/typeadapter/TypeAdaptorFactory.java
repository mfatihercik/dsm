package com.github.mfatihercik.dsb.typeadapter;

import com.github.mfatihercik.dsb.DSMValidationException;

import java.util.HashMap;
import java.util.Map;

public class TypeAdaptorFactory implements Cloneable {
    protected final Map<String, Class<? extends TypeAdaptor>> typeAdapterMap = new HashMap<>();

    private TypeAdaptorFactory(Map<String, Class<? extends TypeAdaptor>> map) {
        typeAdapterMap.putAll(map);
    }

    public TypeAdaptorFactory() {
        registerDefaultTypeAdaptor();
    }

    private void registerDefaultTypeAdaptor() {
        typeAdapterMap.put("object", ObjectTypeAdapter.class);
        typeAdapterMap.put("std", StdTypeAdapter.class);
        typeAdapterMap.put("array", ArrayTypeAdapter.class);
        typeAdapterMap.put("join", JoinTypeAdapter.class);
        typeAdapterMap.put("sum", SumTypeAdapter.class);
        typeAdapterMap.put("mul", MultiplyTypeAdapter.class);
        typeAdapterMap.put("multiply", MultiplyTypeAdapter.class);
    }

    public TypeAdaptor getTypeAdapter(String name) {
        try {
            name = name.toLowerCase();
            Class<? extends TypeAdaptor> typeAdapter = typeAdapterMap.get(name);
            if (typeAdapter == null) {
                throw new DSMValidationException(String.format("%s tagType adapter is not registered", name));
            }
            return typeAdapter.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void register(String name, Class<? extends TypeAdaptor> typeAdaptor) {
        typeAdapterMap.put(name, typeAdaptor);
    }

    @Override
    public TypeAdaptorFactory clone() {
        return new TypeAdaptorFactory(this.typeAdapterMap);
    }
}
