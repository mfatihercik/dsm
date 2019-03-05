package com.github.mfatihercik.dsb.expression;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Jexl3ExpressionResolver implements ExpressionResolver {
    private final ScriptEngine engine;


    public Jexl3ExpressionResolver() {
        this(new HashMap<>());

    }

    public Jexl3ExpressionResolver(Map<String, Object> context) {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        this.engine = engineManager.getEngineByName("jexl3");
        setContext(context);

    }

    public Object resolveExpression(String expression) {
        if (expression.startsWith("$"))
            expression = expression.substring(1);
        try {
            return engine.eval(expression);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBean(String name, Object value) {
        engine.put(name, value);
    }

    public void setContext(Map<String, Object> context) {
        for (Entry<String, Object> entry : context.entrySet()) {
            setBean(entry.getKey(), entry.getValue());
        }
    }


}
