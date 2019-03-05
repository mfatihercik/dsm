package com.github.mfatihercik.dsb.typeadapter;

import java.util.HashMap;
import java.util.Map;

public class TypeAdaptorFactory {
    private static final Map<String, Class<? extends TypeAdaptor>> typeAdapterMap = new HashMap<>();

    static {
        typeAdapterMap.put("object", MapTypeAdapter.class);
        typeAdapterMap.put("std", StdTypeAdapter.class);
        typeAdapterMap.put("array", ListTypeAdapter.class);
        typeAdapterMap.put("join", JoinTypeAdapter.class);
        typeAdapterMap.put("sum", SumTypeAdapter.class);
        typeAdapterMap.put("mul", MultiplyTypeAdapter.class);
        typeAdapterMap.put("multiply", MultiplyTypeAdapter.class);
    }

    public static TypeAdaptor getTypeAdapter(String name) {
        try {
            name = name.toLowerCase();
            Class<? extends TypeAdaptor> typeAdapter = typeAdapterMap.get(name);
            assert typeAdapter != null : String.format("%s tagType adapter is not registered", name);
            return typeAdapter.newInstance();
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

}
