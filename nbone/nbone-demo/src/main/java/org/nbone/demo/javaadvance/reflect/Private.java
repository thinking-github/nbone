package org.nbone.demo.javaadvance.reflect;

import org.nbone.demo.javaadvance.reflect.Person;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 通过反射访问private成员和方法，既然能访问为什么要private？
 * @author chenyicheng
 * @version 1.0
 * @since 2019-04-01
 */
@SuppressWarnings(value = "unchecked")
public class Private {

    public static void main(String[] args) throws Exception {

        Person person = new Person();
        Class c = person.getClass();

        //Class c2 = Class.forName("test.Person");
        //Person person2 = (Person)c2.newInstance();

        Method method = c.getDeclaredMethod("playGame");
        method.setAccessible(true);
        method.invoke(person);

        Field field = c.getDeclaredField("userName");
        field.setAccessible(true);
        field.set(person,"thinking");
        method.invoke(person);

    }

}
