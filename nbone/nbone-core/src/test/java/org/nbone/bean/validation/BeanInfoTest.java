package org.nbone.bean.validation;

import org.junit.Test;
import org.nbone.common.model.PageRequest;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * @author thinking
 * @version 1.0
 * @since 2020-07-01
 */
public class BeanInfoTest {

    @Test
    public void propertyDescriptors() throws IntrospectionException {

        PageRequest pageRequest = new PageRequest();


        BeanInfo info = Introspector.getBeanInfo(PageRequest.class);
        PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(PageRequest.class);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();

        for (int i = 0; i < propertyDescriptors.length; ++i) {
            PropertyDescriptor pd = propertyDescriptors[i];
            if (pd.getName().equals("props")) {
                pd.setValue("transient", Boolean.TRUE);
            }
        }


    }
}
