package com.ivanvelickovski.webfactorydemoapp.Threading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AnagramAnalyzerThreadPool {
    private static AnagramAnalyzerThreadPool instance;
    private ThreadPoolExecutor threadPoolExecutor;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
    private static int MAX_POOL_SIZE;
    private static final int KEEP_ALIVE = 10;

    public static synchronized void post(Runnable runnable) {
        if (instance == null) {
            instance = new AnagramAnalyzerThreadPool();
        }
        instance.threadPoolExecutor.execute(runnable);
    }

    public static AnagramAnalyzerThreadPool getInstance() {
        return instance;
    }

    private AnagramAnalyzerThreadPool() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        MAX_POOL_SIZE = coreNum * 2;
        threadPoolExecutor = new ThreadPoolExecutor(coreNum,
                MAX_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                workQueue);
    }

    public static void finish() {
        instance.threadPoolExecutor.shutdown();
    }
}
