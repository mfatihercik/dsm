package com.github.mfatihercik.dsb.expression;

import com.github.mfatihercik.dsb.utils.StringUtils;

public class ExpressionResolverFactory {

    public static ExpressionResolver getExpressionResolver(String engineName) {
        if (StringUtils.isBlank(engineName))
            engineName = "jexl3";
        return new DefaultExpressionResolver(engineName);
    }

}
