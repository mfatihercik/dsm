package com.github.mfatihercik.dsb.expression;

import java.util.Map;

public interface ExpressionResolver {

    Object resolveExpression(String expression);

    void setBean(String name, Object value);

    void setContext(Map<String, Object> context);

}
