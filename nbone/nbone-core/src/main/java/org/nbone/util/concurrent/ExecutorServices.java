package org.nbone.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;


/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-07
 */
public class ExecutorServices {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorServices.class);

    //parallelExecute
    public static <T, R> List<R> parallelRun(ExecutorService executorService, Collection<T> collection, Invoker<T, R> invoker) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyList();
        }
        Object[] results = new Object[collection.size()];
        CountDownLatch countDownLatch = new CountDownLatch(collection.size());
        int count = 0;
        //thread pool execute
        for (T bean : collection) {
            int index = count;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        results[index] = invoker.invoke(bean);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    } finally {
                        countDownLatch.countDown();
                    }
                }

            });
            count++;
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return (List<R>) Arrays.asList(results);
    }

    public static <T, R> List<R> parallelRun(Collection<T> collection, Invoker<T, R> invoker) {
        return parallelRun(ThreadPool.genericExecutor(), collection, invoker);
    }

    public interface Invoker<T, R> {
        R invoke(T input);
    }
}
