package com.github.mfatihercik.dsb;

import com.github.mfatihercik.dsb.model.DeferAssigment;
import com.github.mfatihercik.dsb.model.ParsingElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsingContext {
    private int nodeMapSize = 0;
    private Map<String, Node> mainNodeMap = new HashMap<>();
    private Map<ParsingElement, Node> parsingElementToNode = new HashMap<>();
    private Map<ParsingElement, List<DeferAssigment>> deferAssigmentMap = new HashMap<>();
    private Class<?> resultType;
    private Node rootNode;

    private Node[] nodeMap = null;

    public void initNodeMap(int size) {
        nodeMapSize = size;
        nodeMap = new Node[nodeMapSize];

    }

    public void clear() {
        getMainNodeMap().clear();
        initNodeMap(nodeMapSize);
        parsingElementToNode.clear();
        deferAssigmentMap.clear();

    }

    public void addDeferAssignment(ParsingElement currentParsingElement, PathInfo pathInfo, Object value) {
        addDeferAssignment(currentParsingElement, pathInfo, value, false);
    }

    public void addDeferAssignment(ParsingElement currentParsingElement, PathInfo pathInfo, Object value, boolean isDefault) {
        if (!currentParsingElement.isRoot()) {
            ParsingElement parentElement = currentParsingElement.getParentElement();
            List<DeferAssigment> deferAssigments = deferAssigmentMap.get(parentElement);
            if (deferAssigments == null) {
                deferAssigments = new ArrayList<>();
                deferAssigmentMap.put(parentElement, deferAssigments);
            }
            deferAssigments.add(new DeferAssigment(currentParsingElement, pathInfo, value));
        }
    }

    public List<DeferAssigment> getDeferAssigment(ParsingElement parentParsingElement) {
        return deferAssigmentMap.get(parentParsingElement);
    }

    public boolean contains(String uniqueName) {
        return getMainNodeMap().containsKey(uniqueName);
    }

    public boolean contains(int index) {
        return nodeMap[index] != null;
    }

    public boolean contains(ParsingElement index) {
        return index.getIndex() > -1 && nodeMap[index.getIndex()] != null;
    }



    public Node get(ParsingElement parsingElement) {
        return parsingElementToNode.get(parsingElement);
//        if (parsingElement.getIndex() > -1) {
//            return get(parsingElement.getIndex());
//        }
//        return null;
    }

    public void addMainNodeMap(Node node) {
        ParsingElement parsingElement = node.getParsingElement();
        getMainNodeMap().put(parsingElement.getUniqueKey(), node);
    }
    public void add(Node node) {
        ParsingElement parsingElement = node.getParsingElement();
        nodeMap[parsingElement.getIndex()] = node;
        parsingElementToNode.put(parsingElement, node);
    }

    public Node remove(ParsingElement parsingElement) {
        nodeMap[parsingElement.getIndex()] = null;
        parsingElementToNode.remove(parsingElement);
        return getMainNodeMap().remove(parsingElement.getUniqueKey());
    }

    public Map<String, Node> getMainNodeMap() {
        return mainNodeMap;
    }

    public void setMainNodeMap(Map<String, Node> mainNodeMap) {
        this.mainNodeMap = mainNodeMap;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

}
