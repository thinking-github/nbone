package org.nbone.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;


/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-07
 */
public abstract class CountDownLatchRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CountDownLatchRunnable.class);

    private final CountDownLatch countDownLatch;

    /**
     * 当前执行位置索引
     */
    private int index;

    /**
     * 返回结果集
     */
    private Object[] results;

    public CountDownLatchRunnable(CountDownLatch countDownLatch, int index, Object[] results) {
        this.countDownLatch = countDownLatch;
        this.index = index;
        this.results = results;
    }

    @Override
    public void run() {
        try {
            results[index] = doExecute();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            countDownLatch.countDown();
        }
    }

    protected abstract Object doExecute();
}
