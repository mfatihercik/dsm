package com.github.mfatihercik.dsb.typeadapter;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.PathInfo;

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
            parent.set(parsingElement.getFieldName(), currentNode.getData());
        }
        parent.addChild(currentNode);
        currentNode.setClose(true);

    }

    @Override
    public Node registerNode(ParsingContext parsingContext, ParsingElement parsingElement, PathInfo pathInfo) {

        Node parentNode = getParentNode(parsingContext, parsingElement);
        Node node = Node.newNode(this.getInitialObject(), parsingElement);

        Node child = parentNode.getChild();
        if (child != null && child.getParsingElement().getIndex() == parsingElement.getIndex()) {
            int index = child.getIndex() + 1;
            node.setIndex(index);
        }
        node.setParent(parentNode);
        parsingContext.add(node);
        return node;

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
