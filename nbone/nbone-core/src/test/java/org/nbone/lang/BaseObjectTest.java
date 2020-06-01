package org.nbone.lang;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-12-25
 */
public class BaseObjectTest {

    @Test
    public void copy() {
        BaseObject ss = new BaseObject() {
        };
        List<?> sources = new ArrayList<>();
        ss.copyProperties(sources, BaseObject.class);
    }
}
