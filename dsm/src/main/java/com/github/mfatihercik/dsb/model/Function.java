package com.github.mfatihercik.dsb.model;

import java.util.Map;

public class Function {
    String name;
    Map<String, Object> params;

    public Function() {
    }

    public Function(String name, Map<String, Object> params) {
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
