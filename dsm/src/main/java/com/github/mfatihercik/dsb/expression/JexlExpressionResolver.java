package com.github.mfatihercik.dsb.expression;

import org.apache.commons.jexl3.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JexlExpressionResolver implements ExpressionResolver {
    private final JexlContext expressionContext;
    private JexlEngine processor;

    public JexlExpressionResolver() {
        this(new HashMap<>());

    }

    public JexlExpressionResolver(Map<String, Object> context) {
        this.processor = new JexlBuilder().create();
        this.expressionContext = new MapContext();
        setContext(context);

    }

    @Override
    public Object resolveExpression(String expression) {
        if (expression.startsWith("$"))
            expression = expression.substring(1);

        JexlScript compiledExpression = getProcessor().createScript(expression);

        return compiledExpression.execute(expressionContext);
    }

    @Override
    public void setBean(String name, Object value) {
        expressionContext.set(name, value);
    }

    public void setContext(Map<String, Object> context) {
        for (Entry<String, Object> entry : context.entrySet()) {
            setBean(entry.getKey(), entry.getValue());
        }
    }

    public JexlEngine getProcessor() {
        return processor;
    }

    public void setProcessor(JexlEngine processor) {
        this.processor = processor;
    }

}
