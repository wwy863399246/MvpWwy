package com.elson.lib.download_library;

import android.support.annotation.NonNull;

import com.elson.lib.download_library.utils.DownLoadConfig;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class DownLoadExecutors
{
    private int maxRequests;
    private ThreadPoolExecutor executorService;
    private final Deque<NickRunnable> runningAsyncCalls;
    private final Deque<NickRunnable> readyAsyncCalls;

    public DownLoadExecutors()
    {
        runningAsyncCalls = new ArrayDeque<>();
        readyAsyncCalls = new ArrayDeque<>();
        maxRequests = DownLoadConfig.getConfig().getMaxTasks();
    }

    public synchronized ThreadPoolExecutor executorService()
    {
        if (executorService == null)
        {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory
                    ("miniexecutors", false));

            executorService.allowCoreThreadTimeOut(true);
        }
        return executorService;
    }

    private static ThreadFactory threadFactory(final String name, final boolean daemon)
    {
        return new ThreadFactory()
        {
            @Override
            public Thread newThread(@NonNull Runnable runnable)
            {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

    synchronized void execute(NickRunnable call)
    {
        if (runningAsyncCalls.size() < maxRequests)
        {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        }
        else
        {
            readyAsyncCalls.add(call);
        }
    }

    void finished(NickRunnable call)
    {
        synchronized (this)
        {
            if (!runningAsyncCalls.remove(call))
            {
                throw new AssertionError("AsyncCall wasn't running!");
            }
            promoteCalls();
            runningCallsCount();
        }

    }

    public synchronized int runningCallsCount()
    {
        return runningAsyncCalls.size();
    }

    private void promoteCalls()
    {
        if (runningAsyncCalls.size() >= maxRequests)
        {
            return;
        }
        if (readyAsyncCalls.isEmpty())
        {
            return;
        }

        for (Iterator<NickRunnable> i = readyAsyncCalls.iterator(); i.hasNext(); )
        {
            NickRunnable call = i.next();
            i.remove();
            runningAsyncCalls.add(call);
            executorService().execute(call);

            if (runningAsyncCalls.size() >= maxRequests)
            {
                return;
            }
        }
    }

}
