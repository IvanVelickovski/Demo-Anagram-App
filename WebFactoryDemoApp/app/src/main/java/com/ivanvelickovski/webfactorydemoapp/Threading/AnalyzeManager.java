package com.ivanvelickovski.webfactorydemoapp.Threading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AnalyzeManager {
    private final ThreadPoolExecutor downloadThreadPool;
    private final BlockingQueue<Runnable> downaloadWorkQueue;

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 5;
    private static final int KEEP_ALIVE_TIME = 50;

    private static AnalyzeManager analyzeManager = null;
    private static MainThreadExecutor handler;

    static {
        analyzeManager = new AnalyzeManager();
        handler = new MainThreadExecutor();
    }

    private AnalyzeManager(){
        downaloadWorkQueue = new LinkedBlockingQueue<Runnable>();

        downloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, downaloadWorkQueue);
    }

    public static AnalyzeManager getAnalyzeManager(){
        return analyzeManager;
    }

    public void runDownloadFile(Runnable task){
        downloadThreadPool.execute(task);
    }

    //to runs task on main thread from background thread
    public MainThreadExecutor getMainThreadExecutor(){
        return handler;
    }
}
