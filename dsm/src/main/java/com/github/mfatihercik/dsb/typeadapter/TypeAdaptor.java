package com.github.mfatihercik.dsb.typeadapter;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.PathInfo;

import java.util.Map;

public interface TypeAdaptor {

    boolean isArray();

    Node getCurrentNode(ParsingContext parsingContext, ParsingElement parsingElement);

    Node getParentNode(ParsingContext parsingContext, ParsingElement parsingElement);

    Node registerNode(ParsingContext parsingContext, ParsingElement parsingElement, PathInfo pathInfo);

    void deregisterNode(ParsingContext parsingContext, Node node, ParsingElement parsingElement, PathInfo pathInfo);

    String getValue(ParsingContext parsingContext, Node node, ParsingElement parsingElement, PathInfo pathInfo, String value);

    boolean isStartElement();

    Map<String, Object> getParameters();

    void setParameters(Map<String, Object> params);

    void setValue(ParsingContext parsingContext, Node currentNode, ParsingElement parsingElement, PathInfo pathInfo, Object value);

    void postProcess(ParsingElement parsingElement);

    Object getInitialObject();

    boolean isObject();


    void broadcast(ParsingContext parsingContext, Node node, ParsingElement currentElement, PathInfo pathInfo, Object value);

}
