package com.github.mfatihercik.dsb.expression;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractExpressionResolver implements ExpressionResolver {
    private final ScriptEngine engine;


    public AbstractExpressionResolver(String engineName) {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        this.engine = engineManager.getEngineByName(engineName);
        setContext(new HashMap<String, Object>());
    }

    public Object resolveExpression(String expression) {
        if (expression.startsWith("$"))
            expression = expression.substring(1);
        try {
            return getEngine().eval(expression);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBean(String name, Object value) {
        getEngine().put(name, value);
    }

    public void setContext(Map<String, Object> context) {
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            setBean(entry.getKey(), entry.getValue());
        }
    }

    public ScriptEngine getEngine() {
        return engine;
    }


}
