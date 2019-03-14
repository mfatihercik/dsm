package com.github.mfatihercik.dsb.functions;

import com.github.mfatihercik.dsb.function.FunctionExecutor;
import com.github.mfatihercik.dsb.function.Params;

public class InsertProduct implements FunctionExecutor {


    @Override
    public void execute(Params params) {
        params.getCurrentNode().set("function", "executed");

    }

}
