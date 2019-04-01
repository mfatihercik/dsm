package com.github.mfatihercik.dsb;

import com.github.mfatihercik.dsb.expression.ExpressionResolver;
import com.github.mfatihercik.dsb.function.FunctionFactory;
import com.github.mfatihercik.dsb.model.DeferAssignment;
import com.github.mfatihercik.dsb.model.ParsingElement;
import com.github.mfatihercik.dsb.transformation.ValueTransformer;

import java.util.*;

public class ParsingContext {
    private int nodeMapSize = 0;
    private Map<String, Node> mainNodeMap = new WeakHashMap<>();
    private final Map<ParsingElement, Node> parsingElementToNode = new HashMap<>();
    private final Map<ParsingElement, List<DeferAssignment>> deferAssignmentMap = new HashMap<>();
    private Class<?> resultType;
    private Node rootNode;
    protected ValueTransformer valueTransformer = null;
    protected ExpressionResolver expressionResolver = null;
    protected FunctionFactory functionFactory = null;

    private Node[] nodeMap = null;

    public void initNodeMap(int size) {
        nodeMapSize = size;
        nodeMap = new Node[nodeMapSize];

    }

    public void clear() {
        getMainNodeMap().clear();
        initNodeMap(nodeMapSize);
        parsingElementToNode.clear();
        deferAssignmentMap.clear();

    }

    public void addDeferAssignment(ParsingElement currentParsingElement, PathInfo pathInfo, Object value) {
        addDeferAssignment(currentParsingElement, pathInfo, value, false);
    }

    public void addDeferAssignment(ParsingElement currentParsingElement, PathInfo pathInfo, Object value, boolean isDefault) {
        if (!currentParsingElement.isRoot()) {
            ParsingElement parentElement = currentParsingElement.getParentElement();
            List<DeferAssignment> deferAssignments = deferAssignmentMap.get(parentElement);
            if (deferAssignments == null) {
                deferAssignments = new ArrayList<>();
                deferAssignmentMap.put(parentElement, deferAssignments);
            }
            deferAssignments.add(new DeferAssignment(currentParsingElement, pathInfo, value));
        }
    }

    public List<DeferAssignment> getDeferredAssignment(ParsingElement parentParsingElement) {
        return deferAssignmentMap.get(parentParsingElement);
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


    public ValueTransformer getValueTransformer() {
        return valueTransformer;
    }

    public void setValueTransformer(ValueTransformer valueTransformer) {
        this.valueTransformer = valueTransformer;
    }

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

    public FunctionFactory getFunctionFactory() {
        return functionFactory;
    }

    public void setFunctionFactory(FunctionFactory functionFactory) {
        this.functionFactory = functionFactory;
    }
}
