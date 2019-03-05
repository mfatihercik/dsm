package com.github.mfatihercik.dsb.expression

import com.github.mfatihercik.dsb.Node
import spock.lang.Specification

class ExpressionResolverSpec extends Specification {

    def "should set node on context with setBean method"() {


        ExpressionResolver resolver = ExpressionResolverFactory.getExpressionResolver (null)
        when:
        Node thizz = new Node ()
        Node parent = new Node ()
        thizz.set ("name", "computer")
        thizz.set ("type", "tablet")
        parent.set ("name", "printer")
        parent.set ("type", "lan")
        thizz.setParent (parent)
        resolver.setBean ("this", thizz)
        Object result = resolver.resolveExpression ("(this.data.name=='computer' and this.parent.data.name=='printer') and 1==1")
        then:

        result.toString () as Boolean
    }

    def "test expression setContext"() {
        ExpressionResolver resolver = ExpressionResolverFactory.getExpressionResolver (null)
        when:
        Node thizz = new Node ()
        Node parent = new Node ()
        thizz.set ("name", "computer")
        thizz.set ("type", "tablet")
        parent.set ("name", "printer")
        parent.set ("type", "lan")
        thizz.setParent (parent)
        LinkedHashMap<String, Object> context = new LinkedHashMap<> ()
        context.put ("this", thizz)

        resolver.setContext (context)
        Object result = resolver.resolveExpression ("(this.data.name=='computer' and this.parent.data.name=='printer') and 1==1")
        then:
        result.toString () as Boolean
    }

    def "test foreach expression"() {

        ExpressionResolver resolver = ExpressionResolverFactory.getExpressionResolver (null)

        when:
        List<Node> nodes = new ArrayList<> ()
        Node thizz = new Node ()
        Node parent = new Node ()
        thizz.set ("name", "computer")
        thizz.set ("type", "tablet")
        parent.set ("name", "printer")
        parent.set ("type", "lan")
        thizz.setParent (parent)

        nodes.add (thizz)
        nodes.add (parent)

        LinkedHashMap<String, Object> context = new LinkedHashMap<> ()
        context.put ("self", thizz)
        context.put ("nodes", nodes)
        context.put ("out", System.out)

        resolver.setContext (context)

        Object result = resolver.resolveExpression ("for(b:nodes){if(b.data.name=='computer') return true;}")
        then:
        result.toString () as Boolean
    }
}

