package com.ivanvelickovski.webfactorydemoapp.Threading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AnalyzeManager {
    private final ThreadPoolExecutor downloadThreadPool;

    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 50;

    private static AnalyzeManager analyzeManager = null;
    private static MainThreadExecutor handler;

    static {
        analyzeManager = new AnalyzeManager();
        handler = new MainThreadExecutor();
    }

    private AnalyzeManager(){
        BlockingQueue<Runnable> downloadWorkQueue = new LinkedBlockingQueue<>();

        downloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, downloadWorkQueue);
    }

    public static AnalyzeManager getAnalyzeManager(){
        return analyzeManager;
    }

    public void runAnalyze(Runnable task){
        downloadThreadPool.execute(task);
    }

    MainThreadExecutor getMainThreadExecutor(){
        return handler;
    }
}
