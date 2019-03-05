package com.github.mfatihercik.dsb.function;

import com.github.mfatihercik.dsb.Node;
import com.github.mfatihercik.dsb.ParsingContext;
import com.github.mfatihercik.dsb.ParsingElement;
import com.github.mfatihercik.dsb.PathInfo;

import java.security.InvalidParameterException;

public class DefaultFunctionFactory implements FunctionFactory {

    private FunctionContext context;

    public DefaultFunctionFactory() {

    }

    public DefaultFunctionFactory(FunctionContext context) {
        this.context = context;
    }


    public void execute(String function, ParsingContext parsingContext, ParsingElement parsingElement, Node currentNode, PathInfo pathInfo, Object value) {

        FunctionExecutor functionExecutor = this.context.get(function);
        if (functionExecutor == null) {
            throw new InvalidParameterException(String.format("%s function is not defined", function));
        }
        functionExecutor.execute(parsingContext, parsingElement, currentNode, pathInfo, value);
    }

    public FunctionContext getContext() {
        return context;
    }

    public void setContext(FunctionContext context) {
        this.context = context;
    }
}
