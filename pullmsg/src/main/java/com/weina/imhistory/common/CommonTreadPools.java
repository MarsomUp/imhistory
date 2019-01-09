package com.weina.imhistory.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 通用线程池
 * @Author: mayc
 * @Date: 2019/01/08 17:47
 */
public class CommonTreadPools {
    private static final Integer CORE_POOL_SIZE = 8;
    private static final Integer MAXIMUM_POOL_SIZE = 16;
    private static final Long KEEP_ALIVE_TIME = 30L;
    private BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue(100);
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, blockingQueue);
    private static CommonTreadPools commonTreadPools = null;
    private static Object object = new Object();
    private CommonTreadPools() {}

    public static CommonTreadPools getInstance() {
        synchronized (object) {
            if (commonTreadPools == null) {
                commonTreadPools = new CommonTreadPools();
            }
        }
        return commonTreadPools;
    }

    /**
     * 新增一个任务到线程池
     * 新增的线程必须是实现Runnable接口的线程
     * @param t
     */
    public static void addThred(Runnable t) throws NullPointerException {
        if (t == null) {
            throw new NullPointerException("线程不能为null");
        }
        CommonTreadPools ctp = getInstance();
        ctp.executor.execute(t);
    }
}
