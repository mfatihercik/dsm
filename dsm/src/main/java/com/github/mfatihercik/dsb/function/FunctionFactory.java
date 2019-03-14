package com.github.mfatihercik.dsb.function;

public interface FunctionFactory {

    void execute(String function, Params params);

    FunctionContext getContext();

    void setContext(FunctionContext context);
}
