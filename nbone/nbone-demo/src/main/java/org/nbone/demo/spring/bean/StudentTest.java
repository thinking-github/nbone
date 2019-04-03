package org.nbone.demo.spring.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2019-04-02
 */
public class StudentTest {

    public static void main(String[] args) {

        String path  = "spring-bean2.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(path);
        System.out.println(context.getBean("a", StudentA.class));

    }
}
