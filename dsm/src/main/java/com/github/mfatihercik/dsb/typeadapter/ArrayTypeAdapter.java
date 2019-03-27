package com.github.mfatihercik.dsb.typeadapter;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.PathInfo;
import com.github.mfatihercik.dsb.model.ParsingElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ArrayTypeAdapter extends BaseObjectAdapter {

    public static final String NAME = "array";

    @Override
    public void setValue(ParsingContext parsingContext, Node currentNode, ParsingElement parsingElement, PathInfo pathInfo, Object value) {


        if (isSimpleObject(parsingElement)) {
            currentNode.add(value);
            ((List) currentNode.getParent().get(parsingElement.getFieldName())).add(value);
        } else {
            List list = (List) currentNode.getParent().getParent().get(parsingElement.getFieldName());

            if (parsingElement.isRoot() && parsingContext.getResultType() != null) {
                currentNode.getParent().add(currentNode.getData());
                list.add(currentNode.toObject(parsingContext.getResultType()));
            } else {
                currentNode.getParent().add(currentNode.getData());
                list.add(currentNode.getData());
                currentNode.setClose(true);
            }
        }
    }

    private boolean isSimpleObject(ParsingElement parsingElement) {
        return parsingElement.getChildren().isEmpty();
    }

    @Override
    public Node registerNode(ParsingContext parsingContext, ParsingElement parsingElement, PathInfo pathInfo) {
        TypeAdaptor typeAdapter = parsingElement.getTypeAdapter();

        Node parentElementNode = getParentNode(parsingContext, parsingElement);

        String fieldName = parsingElement.getFieldName();
        Object parentData = parentElementNode.get(fieldName);

        Node parentNode = parentElementNode.getChild(parsingElement);

        if (parentData == null) {
            parentData = typeAdapter.getInitialObject();
            parentElementNode.set(fieldName, parentData);
            parsingContext.addMainNodeMap(parentElementNode.addChild(parentData, parsingElement));
            parentNode = null;
        }

        if (parentNode == null) {
            parentNode = parentElementNode.addChild(typeAdapter.getInitialObject(), parsingElement);
        } else {
            parentNode.incrementIndex();
        }

        if (!isSimpleObject(parsingElement)) {
            Node child = parentNode.getChild();
            if (child == null) {
                child = parentNode.addChild(new LinkedHashMap<String, Object>(), parsingElement);
            } else {
                child.setData(new LinkedHashMap<>());
                child.setClose(false);
            }
            child.setIndex(parentNode.getIndex());
            parsingContext.add(child);
            return child;
        } else {
            parsingContext.add(parentNode);
        }
        return parentNode;

    }

    @Override
    public Node getParentNode(ParsingContext parsingContext, ParsingElement parsingElement) {
        if (parsingElement.isRoot())
            return parsingContext.getRootNode();
        else {
            ParsingElement parentElement = parsingElement.getParentElement();
            return parentElement.getTypeAdapter().getCurrentNode(parsingContext, parentElement);
        }

    }


    @Override
    public Node getCurrentNode(ParsingContext parsingContext, ParsingElement parsingElement) {

        Node parentNode = getParentNode(parsingContext, parsingElement);
        Node currentNode = parentNode.getChild(parsingElement);
        if (currentNode == null) {
            return parsingContext.getRootNode();
        } else {
            if (isSimpleObject(parsingElement)) {
                return currentNode;
            } else {
                return currentNode.getChild();

            }
        }

    }

    @Override
    public Object getInitialObject() {
        return new ArrayList<>();
    }

    private boolean isListElement(Node root) {
        if (root.getParent() == null || root.getParent().getParsingElement() == null)
            return false;
        return root.getParsingElement().getIndex() == root.getParent().getParsingElement().getIndex();
    }

    @Override
    public void broadcast(ParsingContext parsingContext, Node node, ParsingElement currentElement, PathInfo pathInfo, Object value) {
        super.broadcast(parsingContext, node, currentElement, pathInfo, value);
        if (isListElement(node)) {
            List<Object> children = (List<Object>) node.getParent().getData();
            for (Object childData : children) {
                node.setClose(false);
                node.setData(childData);
                currentElement.getTypeAdapter().setValue(parsingContext, node, currentElement, pathInfo, value);
                node.setClose(true);
            }
        }
    }

    @Override
    public boolean isArray() {
        return true;
    }

}
