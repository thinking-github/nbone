package org.nbone.util.concurrent;


import java.util.concurrent.*;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-10-25
 */
public class ThreadPool {

    public static final String GENERIC = "generic";
    private static ThreadPoolExecutor genericExecutor;


    public static ThreadPoolExecutor genericExecutor() {
        if (genericExecutor == null) {
            synchronized (ThreadPool.class) {
                if (genericExecutor == null) {
                    final int genericThreadPoolMax = boundedBy(4 * Runtime.getRuntime().availableProcessors(), 128, 512);
                    genericExecutor = makeScalingExecutor(4, genericThreadPoolMax, GENERIC);
                }
            }
        }
        return genericExecutor;
    }

    /**
     * org.elasticsearch.common.util.concurrent.EsExecutors.newScaling()
     */
    public static ThreadPoolExecutor makeScalingExecutor(final int min, final int max, final String serverName) {
        ExecutorScalingQueue<Runnable> queue = new ExecutorScalingQueue<>();
        ThreadPoolExecutor executor = makeThreadPool(min, max, 60L, queue, serverName);
        queue.executor = executor;
        return executor;
    }


    public static ThreadPoolExecutor makeFixedThreadPool(final int size, final int queueCapacity, final String serverName) {
        return makeThreadPool(size, size, 60L, queueCapacity, serverName);
    }


    public static ThreadPoolExecutor makeThreadPool(final int core, final String serverName) {
        return makeThreadPool(core, 200, 60L, 1000, serverName);
    }

    public static ThreadPoolExecutor makeThreadPool(final int core, final int max, final String serverName) {
        return makeThreadPool(core, max, 60L, 1000, serverName);
    }

    public static ThreadPoolExecutor makeThreadPool(final int core, final int max,
                                                    long keepAliveTime,
                                                    final int queueCapacity,
                                                    final String serverName) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                core,
                max,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueCapacity),
                new NamedThreadFactory(serverName)
        );        // default maxThreads 200, minThreads 60

        return threadPoolExecutor;
    }

    public static ThreadPoolExecutor makeThreadPool(final int core, final int max,
                                                    long keepAliveTime,
                                                    final int queueCapacity,
                                                    final String serverName,
                                                    RejectedExecutionHandler handler) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                core,
                max,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueCapacity),
                new NamedThreadFactory(serverName),
                handler
        );        // default maxThreads 200, minThreads 60

        return threadPoolExecutor;
    }


    public static ThreadPoolExecutor makeThreadPool(final int core, final int max,
                                                    long keepAliveTime,
                                                    BlockingQueue<Runnable> workQueue,
                                                    final String serverName) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                core,
                max,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                new NamedThreadFactory(serverName)
        );        // default maxThreads 200, minThreads 60

        return threadPoolExecutor;
    }


    // see org.elasticsearch.threadpool.ThreadPool#boundedBy
    static int boundedBy(int value, int min, int max) {
        return Math.min(max, Math.max(min, value));
    }

    // see org.elasticsearch.common.util.concurrent.Esexecutors
    static class ExecutorScalingQueue<E> extends LinkedTransferQueue<E> {

        ThreadPoolExecutor executor;

        ExecutorScalingQueue() {
        }

        @Override
        public boolean offer(E e) {
            // first try to transfer to a waiting worker thread
            if (!tryTransfer(e)) {
                // check if there might be spare capacity in the thread
                // pool executor
                int left = executor.getMaximumPoolSize() - executor.getCorePoolSize();
                if (left > 0) {
                    // reject queuing the task to force the thread pool
                    // executor to add a worker if it can; combined
                    // with ForceQueuePolicy, this causes the thread
                    // pool to always scale up to max pool size and we
                    // only queue when there is no spare capacity
                    return false;
                } else {
                    return super.offer(e);
                }
            } else {
                return true;
            }
        }

    }

    /* new ThreadFactory() {
         @Override
         public Thread newThread(Runnable r) {
             return new Thread(r, "threadPool." + serverName + "." + r.hashCode());
         }
     }*/


}
