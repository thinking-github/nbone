package org.nbone.util;

import java.math.BigDecimal;

/**
 * @author thinking
 * @version 1.0
 * @since 2020-08-06
 */
public class ClassUtilsTest {


    public static void main(String[] args) {
        System.out.println(ClassUtils.getSmartClassName(Long.class));
        System.out.println(ClassUtils.getSmartClassName(BigDecimal.class));
        System.out.println(ClassUtils.getSmartClassName("oo.oo"));
    }

}
