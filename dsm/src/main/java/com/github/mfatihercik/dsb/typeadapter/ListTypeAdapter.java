package com.github.mfatihercik.dsb.typeadapter;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.PathInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ListTypeAdapter extends BaseObjectAdapter {

    public static final String NAME = "array";

    @Override
    public void setValue(ParsingContext parsingContext, Node currentNode, ParsingElement parsingElement, PathInfo pathInfo, Object value) {

        if (isSimpleObject(parsingElement)) {
            currentNode.add(value);
        } else {
            if (parsingElement.isRoot() && parsingContext.getResultType() != null) {
                currentNode.getParent().add(currentNode.toObject(parsingContext.getResultType()));
            } else {

                currentNode.getParent().add(currentNode.getData());
                currentNode.setClose(true);
            }
        }
    }

    private boolean isSimpleObject(ParsingElement parsingElement) {
        return parsingElement.getChildren().isEmpty();
    }

    @Override
    public Node registerNode(ParsingContext parsingContext, ParsingElement parsingElement, PathInfo pathInfo) {
        TypeAdaptor typeAdapter = parsingElement.getTagTypeAdapter();

        Node parentElementNode = getParentNode(parsingContext, parsingElement);

        String uniqueKey = parsingElement.getUniqueKey();
        Object parentData = parentElementNode.get(uniqueKey);
        parentData = parentData == null ? typeAdapter.getInitialObject() : parentData;

        Node parentNode = parentElementNode.getChild();
        if (parentNode != null && parsingElement.getIndex() != parentNode.getParsingElement().getIndex()) {

            parentNode = null;
        }

        if (parentNode == null) {
            parentNode = parentElementNode.addChild(parentData, parsingElement);
            parentElementNode.set(uniqueKey, parentData);
            parsingContext.add(parentNode);

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
            return child;
        }
        return parentNode;

    }

    @Override
    public Node getParentNode(ParsingContext parsingContext, ParsingElement parsingElement) {
        if (parsingElement.isRoot())
            return parsingContext.getRootNode();
        else {
            ParsingElement parentElement = parsingElement.getParentElement();
            return parentElement.getTagTypeAdapter().getCurrentNode(parsingContext, parentElement);
        }

    }


    @Override
    public Node getCurrentNode(ParsingContext parsingContext, ParsingElement parsingElement) {

        Node currentNode = parsingContext.get(parsingElement);
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
                currentElement.getTagTypeAdapter().setValue(parsingContext, node, currentElement, pathInfo, value);
                node.setClose(true);
            }
        }
    }

    @Override
    public boolean isArray() {
        return true;
    }

}
