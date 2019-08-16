package org.nbone.demo.spring.expression;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-08-13
 */
public class ExpressionDemo {

    public static void main(String[] args) throws NoSuchMethodException {

        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        // registerFunction
        context.registerFunction("math", ExpressionDemo.class.getMethod("math"));
        context.setVariable("name", "thinking");


        //Methods invoke
        String bc = parser.parseExpression("'abc'.substring(1, 3)").getValue(String.class);


        //class load
        Class dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);


        Object value = parser.parseExpression("22").getValue(context);

        Object name = parser.parseExpression("#name").getValue(context);

        //static method invoke
        String el = "T(org.nbone.demo.spring.expression.ExpressionDemo).math()";
        Object bool = parser.parseExpression(el).getValue(context);


        //registerFunction invoke
        Boolean math = parser.parseExpression("#math()").getValue(context, Boolean.class);

        System.out.println(value);
        System.out.println(name);
        System.out.println(bool);
    }


    public static boolean math() {
        System.out.println("math....");
        return true;
    }

}
