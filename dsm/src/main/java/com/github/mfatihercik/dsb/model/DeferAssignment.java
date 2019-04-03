package com.github.mfatihercik.dsb.model;

import com.github.mfatihercik.dsb.PathInfo;

public class DeferAssignment {
    private ParsingElement currentParsingElement;
    private PathInfo pathInfo;
    private Object value;
    private boolean isDefault;


    public DeferAssignment(ParsingElement currentParsingElement, PathInfo pathInfo, Object value, boolean isDefault) {
        this.currentParsingElement = currentParsingElement;
        this.pathInfo = pathInfo;
        this.value = value;
        this.isDefault = isDefault;
    }

    public boolean isDefault() {
        return isDefault;
    }


    public DeferAssignment() {
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public PathInfo getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(PathInfo pathInfo) {
        this.pathInfo = pathInfo;
    }

    public ParsingElement getCurrentParsingElement() {
        return currentParsingElement;
    }

    public void setCurrentParsingElement(ParsingElement currentParsingElement) {
        this.currentParsingElement = currentParsingElement;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
