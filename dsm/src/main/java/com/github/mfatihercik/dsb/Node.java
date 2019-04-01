package com.github.mfatihercik.dsb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mfatihercik.dsb.model.ParsingElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class Node {

    private Node child = null;
    private Node parent = null;
    private ParsingElement parsingElement = null;
    private Object data;
    private ObjectMapper objectMapper;
    private int index = 0;
    private boolean isClose = false;
    private Map<ParsingElement, Node> childNodes = null;

    public void reset(Object data) {
        this.data = data;
        childNodes = null;
        child = null;
        isClose = false;
        index = 0;
    }

    public Node() {
        this.data = new LinkedHashMap<String, Object>();
    }

    public Node(Object data, ParsingElement parsingElement) {
        this.data = data;
        this.parsingElement = parsingElement;
    }

    public static Node newNode(Object data, ParsingElement parsingElement) {
        return new Node(data, parsingElement);
    }

    public void incrementIndex() {
        index++;
    }

    public void broadcast(ParsingContext parsingContext, ParsingElement currentParsingElement, PathInfo pathInfo, Object value) {
        getParsingElement().getTypeAdapter().broadcast(parsingContext, this, currentParsingElement, pathInfo, value);
    }

    public <T> Object toObject(ObjectMapper objectMapper, Class<T> classOf) {
        return convertToObject(classOf, objectMapper);
    }

    public <T> T toObject(Class<T> classOf) {
        return convertToObject(classOf, this.getObjectMapper());
    }

    public <T> T toObject(TypeReference<T> classOf) {
        return getObjectMapper().convertValue(this.toObject(), classOf);
    }

    private <T> T convertToObject(Class<T> classOf, ObjectMapper mapper) {

        return mapper.convertValue(this.toObject(), classOf);
    }

    public Object toObject() {
        if (getParsingElement() == null) {
            if (this.getChild() == null) {
                return null;
            }
            return this.getChild().getData();

        } else {
            return getData();

        }
    }

    @SuppressWarnings("unchecked")
    public Boolean containsKey(String key) {
        if (data instanceof Map)
            return ((Map<String, Object>) data).containsKey(key);
        return false;
    }

    @SuppressWarnings("unchecked")
    public Object get(String key) {
        return ((Map<String, Object>) data).get(key);
    }

    @SuppressWarnings("unchecked")
    public void set(String key, Object value) {
        ((Map<String, Object>) data).put(key, value);
    }

    @SuppressWarnings("unchecked")
    public void add(Object value) {
        ((List<Object>) data).add(value);
    }

    @SuppressWarnings("unchecked")
    public Object get(int index) {
        return ((List<Object>) data).get(index);
    }

    public void setParent(Node parent, boolean addToChild) {

        this.parent = parent;
        if (addToChild && parent != null) {
            if (parent.childNodes == null) {
                parent.childNodes = new WeakHashMap<>();
            }
            parent.childNodes.put(this.parsingElement, this);
            parent.setChild(this);
        }
    }

    public void addChild(Node child) {
        child.setParent(this);
    }

    public Node addChild(Object data, ParsingElement parsingElement) {
        Node node = new Node(data, parsingElement);
        node.setParent(this);
        return node;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public ParsingElement getParsingElement() {
        return parsingElement;
    }

    public void setParsingElement(ParsingElement parsingElement) {
        this.parsingElement = parsingElement;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        setParent(parent, true);
    }

    public void removeParent() {
        this.parent = null;
    }

    public void removeChild() {
        this.child.removeParent();
        this.child = null;
    }

    public ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            if (getParent() == null)
                objectMapper = new ObjectMapper();
            else
                objectMapper = getParent().getObjectMapper();

        }
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean isClose) {
        this.isClose = isClose;
    }

    public Node getChild() {
        return child;
    }

    public Node getChild(ParsingElement child) {
        if (childNodes == null)
            return null;
        return childNodes.get(child);
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}