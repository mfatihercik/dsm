package com.github.mfatihercik.dsb.function;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.PathInfo;

public interface FunctionFactory {

    void execute(String function, ParsingContext parsingContext, ParsingElement parsingElement, Node currentNode, PathInfo pathInfo, Object value);

    FunctionContext getContext();

    void setContext(FunctionContext context);
}
