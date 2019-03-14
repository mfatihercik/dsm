package com.github.mfatihercik.dsb.typeadapter;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.PathInfo;
import com.github.mfatihercik.dsb.model.ParsingElement;

import java.util.Map;

public abstract class BaseSimpleAdapter implements TypeAdaptor {

    public String getValue(ParsingContext parsingContext, Node node, ParsingElement parsingElement, PathInfo pathInfo, String value) {
        return value;

    }

    public boolean isStartElement() {
        return false;
    }

    public void setValue(ParsingContext parsingContext, Node currentNode, ParsingElement parsingElement, PathInfo pathInfo, Object value) {
        currentNode.set(parsingElement.getFieldName(), value);
        if (currentNode.isClose()) {
            currentNode.broadcast(parsingContext, parsingElement, pathInfo, value);
        }

    }

    @Override
    public Map<String, Object> getParameters() {
        return null;
    }

    @Override
    public void setParameters(Map<String, Object> params) {

    }

    @Override
    public void postProcess(ParsingElement parsingElement) {

    }

    @Override
    public Object getInitialObject() {
        return null;
    }

    @Override
    public boolean isObject() {
        return false;
    }


    @Override
    public Node registerNode(ParsingContext parsingContext, ParsingElement parsingElement, PathInfo pathInfo) {
        return null;
    }

    @Override
    public Node getParentNode(ParsingContext parsingContext, ParsingElement parsingElement) {

        if (parsingElement.isRoot())
            return parsingContext.getRootNode();
        else {
            ParsingElement parentElement = parsingElement.getParentElement();
            return parentElement.getTagTypeAdapter().getParentNode(parsingContext, parentElement);
        }
    }

    @Override
    public Node getCurrentNode(ParsingContext parsingContext, ParsingElement parsingElement) {
        if (isObject()) {

            Node currentNode = parsingContext.get(parsingElement);
            return currentNode == null ? parsingContext.getRootNode() : currentNode;
        } else {
            ParsingElement parentElement = parsingElement.getParentElement();
            return parentElement.getTagTypeAdapter().getCurrentNode(parsingContext, parentElement);
        }
    }

    @Override
    public void deregisterNode(ParsingContext parsingContext, Node node, ParsingElement parsingElement, PathInfo pathInfo) {

    }

    @Override
    public void broadcast(ParsingContext parsingContext, Node node, ParsingElement currentElement, PathInfo pathInfo, Object value) {


    }

    @Override
    public boolean isArray() {

        return false;
    }

}
