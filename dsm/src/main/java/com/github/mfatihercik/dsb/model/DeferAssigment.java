package com.github.mfatihercik.dsb.model;

import com.github.mfatihercik.dsb.PathInfo;

public class DeferAssigment {
    Object value;
    PathInfo pathInfo;
    private ParsingElement currentParsingElement;


    public DeferAssigment() {
    }

    public DeferAssigment(ParsingElement currentParsingElement, PathInfo pathInfo, Object value) {
        this.currentParsingElement = currentParsingElement;
        this.value = value;
        this.pathInfo = pathInfo;
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
