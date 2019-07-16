package org.nbone.demo.spring.util;


import org.junit.Test;
import org.nbone.demo.common.domain.User;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019/6/27
 */
public class BeanUtilsDemo {

    @Test
    public void testPropertyDescriptor(){
        //可以获取到父类的属性清单
        PropertyDescriptor[] propertyDescriptors =   BeanUtils.getPropertyDescriptors(User.class);

        Field[] fields =  User.class.getDeclaredFields();

        System.out.println("00");

        StringBuilder sb =  new StringBuilder().append(" ").append((String) null).append(" ").append("chen");

        System.out.println(sb);
    }
}
