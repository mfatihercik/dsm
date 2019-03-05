package com.github.mfatihercik.dsb.function;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FunctionContext {
    final Map<String, FunctionExecutor> functionExecutorMap = new LinkedHashMap<>();

    public FunctionContext() {

    }

    public FunctionContext(Map<String, String> functionMap) {
        add(functionMap);

    }

    private void add(Map<String, String> functionMap) {
        for (Entry<String, String> entry : functionMap.entrySet()) {

            String value = entry.getValue();
            FunctionExecutor functionExecutor = createFunction(value);
            functionExecutorMap.put(entry.getKey(), functionExecutor);

        }
    }

    private FunctionExecutor createFunction(String value) {
        try {
            Class<?> functionClass = Class.forName(value);
            return (FunctionExecutor) functionClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void merge(Map<String, String> functionMap) {
        for (Entry<String, FunctionExecutor> item : functionExecutorMap.entrySet()) {
            functionMap.remove(item.getKey());
        }
        add(functionMap);

    }

    public void registerFunction(String name, FunctionExecutor executor) {
        functionExecutorMap.put(name, executor);
    }

    public void registerFunction(String name, String fullClassName) {
        functionExecutorMap.put(name, createFunction(fullClassName));
    }

    public FunctionExecutor get(String name) {
        return functionExecutorMap.get(name);
    }

    public FunctionExecutor remove(String name) {
        return functionExecutorMap.remove(name);
    }

    public Map<String, FunctionExecutor> getAll() {
        return functionExecutorMap;
    }

}
