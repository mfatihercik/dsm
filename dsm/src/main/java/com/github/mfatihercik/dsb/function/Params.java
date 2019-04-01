package com.github.mfatihercik.dsb.function;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.PathInfo;
import com.github.mfatihercik.dsb.model.ParsingElement;

import java.util.HashMap;
import java.util.Map;

public class Params {


    private ParsingContext parsingContext;

    private ParsingElement parsingElement;
    private Node currentNode;
    private PathInfo pathInfo;
    private Object value;
    private Map<String, Object> extParams;

    public Params() {
    }

    public Params(ParsingContext parsingContext, ParsingElement parsingElement, Node currentNode, PathInfo pathInfo, Object value, Map<String, Object> extParams) {
        this.parsingContext = parsingContext;
        this.parsingElement = parsingElement;
        this.currentNode = currentNode;
        this.pathInfo = pathInfo;
        this.value = value;
        this.extParams = extParams == null ? new HashMap<String, Object>() : extParams;
    }

    public ParsingContext getParsingContext() {
        return parsingContext;
    }

    public void setParsingContext(ParsingContext parsingContext) {
        this.parsingContext = parsingContext;
    }

    public ParsingElement getParsingElement() {
        return parsingElement;
    }

    public void setParsingElement(ParsingElement parsingElement) {
        this.parsingElement = parsingElement;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public PathInfo getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(PathInfo pathInfo) {
        this.pathInfo = pathInfo;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Map<String, Object> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, Object> extParams) {
        this.extParams = extParams;
    }
}
