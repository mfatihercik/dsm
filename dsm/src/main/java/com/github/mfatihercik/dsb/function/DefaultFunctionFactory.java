package com.github.mfatihercik.dsb.function;

import java.security.InvalidParameterException;

public class DefaultFunctionFactory implements FunctionFactory {

    private FunctionContext context;

    public DefaultFunctionFactory() {

    }

    public DefaultFunctionFactory(FunctionContext context) {
        this.context = context;
    }


    public void execute(String function, Params params) {

        FunctionExecutor functionExecutor = this.context.get(function);
        if (functionExecutor == null) {
            throw new InvalidParameterException(String.format("%s function is not defined", function));
        }
        functionExecutor.execute(params);
    }

    public FunctionContext getContext() {
        return context;
    }

    public void setContext(FunctionContext context) {
        this.context = context;
    }
}
