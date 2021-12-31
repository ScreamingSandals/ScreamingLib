package org.screamingsandals.lib.utils.executor;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class ExecutorProvider {
    public ExecutorService buildExecutor(String name) {
       return buildExecutor(Runtime.getRuntime().availableProcessors(), name);
    }

    public ExecutorService buildExecutor(int threadCount, String name) {
        return Executors.newFixedThreadPool(threadCount, new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(@NotNull Runnable r) {
                final Thread thread = Executors.defaultThreadFactory().newThread(r);
                thread.setName(name + " - #" + count.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    public ScheduledExecutorService buildScheduledExecutor(String name) {
        return buildScheduledExecutor(Runtime.getRuntime().availableProcessors(), name);
    }

    public ScheduledExecutorService buildScheduledExecutor(int threadCount, String name) {
        return Executors.newScheduledThreadPool(threadCount, new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(@NotNull Runnable r) {
                final Thread thread = Executors.defaultThreadFactory().newThread(r);
                thread.setName(name + " - #" + count.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    public void destroyExecutor(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(20, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(20, TimeUnit.SECONDS)) {
                    throw new UnsupportedOperationException("ExecutorService did not terminated, something is wrong..");
                }
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

