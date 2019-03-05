package com.github.mfatihercik.dsb.expression;

public class ExpressionResolverFactory {

    public static ExpressionResolver getExpressionResolver(String name) {
        return new Jexl3ExpressionResolver();
    }

}
