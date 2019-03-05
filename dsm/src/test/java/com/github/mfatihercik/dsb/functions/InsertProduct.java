package com.github.mfatihercik.dsb.functions;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.PathInfo;
import com.github.mfatihercik.dsb.function.FunctionExecutor;

public class InsertProduct implements FunctionExecutor {


    @Override
    public void execute(ParsingContext parsingContext, ParsingElement parsingElement, Node currentNode, PathInfo pathInfo, Object value) {
        currentNode.set("function", "executed");

    }

}
