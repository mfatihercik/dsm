package com.github.mfatihercik.dsb.typeadapter;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.PathInfo;
import com.github.mfatihercik.dsb.model.ParsingElement;

import java.util.Map;

public class MapTypeAdapter extends BaseObjectAdapter {
    public static final String NAME = "object";

    @Override
    public void setValue(ParsingContext parsingContext, Node currentNode, ParsingElement parsingElement, PathInfo pathInfo, Object value) {
        super.setValue(parsingContext, currentNode, parsingElement, pathInfo, value);

        // added to parent child

        Node parent = currentNode.getParent();
        if (parsingElement.isRoot() && parsingContext.getResultType() != null) {
            parent.set(parsingElement.getFieldName(), currentNode.toObject(parsingContext.getResultType()));

        } else {
            Map data = (Map) parent.get(parsingElement.getFieldName());
            if (data != null) {
                data.putAll((Map) currentNode.getData());
            } else {
                parent.set(parsingElement.getFieldName(), currentNode.getData());
            }
        }
        parent.addChild(currentNode);
        currentNode.setClose(true);

    }

    @Override
    public Node registerNode(ParsingContext parsingContext, ParsingElement parsingElement, PathInfo pathInfo) {

        Node parentNode = getParentNode(parsingContext, parsingElement);
        Node node = Node.newNode(this.getInitialObject(), parsingElement);

        Node child = parentNode.getChild(parsingElement);
        if (child != null) {
            int index = child.getIndex() + 1;
            node.setIndex(index);
        }
        node.setParent(parentNode);
        parsingContext.add(node);
        parsingContext.addMainNodeMap(node);
        return node;

    }

    @Override
    public Node getCurrentNode(ParsingContext parsingContext, ParsingElement parsingElement) {
        if (parsingElement.isRoot()) {
            return parsingContext.get(parsingElement);
        } else {
            Node child = parsingElement.getTagTypeAdapter().getParentNode(parsingContext, parsingElement).getChild(parsingElement);
            if (child == null)
                parsingContext.getRootNode();
            return child;
        }
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
}
