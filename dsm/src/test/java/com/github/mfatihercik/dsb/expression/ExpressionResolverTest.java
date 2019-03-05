package com.github.mfatihercik.dsb.expression;

import com.github.mfatihercik.dsb.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpressionResolverTest {

    @Test
    public void expressionTestSetBean() {

        ExpressionResolver resolver = ExpressionResolverFactory.getExpressionResolver(null);
        Node thizz = new Node();
        Node parent = new Node();
        thizz.set("name", "computer");
        thizz.set("type", "tablet");
        parent.set("name", "printer");
        parent.set("type", "lan");
        thizz.setParent(parent);
        resolver.setBean("this", thizz);
        Object result = resolver.resolveExpression("(this.data.name=='computer' and this.parent.data.name=='printer') and 1==1");
        Assert.assertTrue(Boolean.valueOf(result.toString()));

    }

    @Test
    public void expressionTestSetContext() {

        ExpressionResolver resolver = ExpressionResolverFactory.getExpressionResolver(null);
        Node thizz = new Node();
        Node parent = new Node();
        thizz.set("name", "computer");
        thizz.set("type", "tablet");
        parent.set("name", "printer");
        parent.set("type", "lan");
        thizz.setParent(parent);
        LinkedHashMap<String, Object> context = new LinkedHashMap<>();
        context.put("this", thizz);

        resolver.setContext(context);
        Object result = resolver.resolveExpression("(this.data.name=='computer' and this.parent.data.name=='printer') and 1==1");
        Assert.assertTrue(Boolean.valueOf(result.toString()));

    }

    @Test
    public void expressionTestForEach() {

        ExpressionResolver resolver = ExpressionResolverFactory.getExpressionResolver(null);
        List<Node> nodes = new ArrayList<>();

        Node thizz = new Node();
        Node parent = new Node();
        thizz.set("name", "computer");
        thizz.set("type", "tablet");
        parent.set("name", "printer");
        parent.set("type", "lan");
        thizz.setParent(parent);

        nodes.add(thizz);
        nodes.add(parent);

        LinkedHashMap<String, Object> context = new LinkedHashMap<>();
        context.put("self", thizz);
        context.put("nodes", nodes);
        context.put("out", System.out);

        resolver.setContext(context);
        Object result = resolver.resolveExpression("for(b:nodes){if(b.data.name=='computer') return true;}");
        Assert.assertTrue(Boolean.valueOf(String.valueOf(result)));

    }

}
