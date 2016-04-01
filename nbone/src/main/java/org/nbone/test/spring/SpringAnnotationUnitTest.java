package org.nbone.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <p>Spring Annotation Unit  Test</p>
 * @author thinking
 * @version 1.0 
 * @since  2015-12-12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/test.xml")
//@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
//@Transactional
public class SpringAnnotationUnitTest {

    /**
     * 测试　测试环境是否正常
     */
    @Test
    public void testEnv(){
        System.out.println("ok");
    }
}
