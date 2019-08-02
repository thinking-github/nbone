package org.nbone.demo.spring.util;

import org.junit.Test;
import org.nbone.demo.common.domain.User;
import org.nbone.demo.spring.mvc.UserController;
import org.nbone.demo.spring.mvc.UserRestController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-07-20
 */
public class AnnotationUtilsDemo {

    @Test
    public void testFindAnnotation() throws NoSuchMethodException {

        /**
         *
         * findAnnotation  method 会向上级父类递归查找， 注解类型同时也是向上级元注解查找
         * getAnnotation   method只会在本级查找，      注解类型同时会向上级元注解查找
         */

        /**
         * 测试类型
         */
        Annotation annotation  = AnnotationUtils.findAnnotation(UserController.class, Controller.class);
        Annotation annotation1 = AnnotationUtils.findAnnotation(UserRestController.class, Controller.class);

        Annotation getAnnotation  = AnnotationUtils.getAnnotation(UserController.class, Controller.class);
        Annotation getAnnotation1    =  AnnotationUtils.getAnnotation(UserRestController.class, Controller.class);


        /**
         * 测试方法
         */
        Method method = UserController.class.getDeclaredMethod("add", User.class);
        PostMapping    postMapping     = AnnotationUtils.findAnnotation(method, PostMapping.class);
        RequestMapping requestMapping  = AnnotationUtils.findAnnotation(method, RequestMapping.class);


        RequestMapping requestMapping1 = AnnotationUtils.getAnnotation(method, RequestMapping.class);

        System.out.println(postMapping.value().length > 0 ? postMapping.value()[0] : null);
        System.out.println(requestMapping.value().length > 0 ? requestMapping.value()[0] : null);
        System.out.println(requestMapping1.value().length > 0 ? requestMapping1.value()[0] : null);

        System.out.println("-------");


    }
}
