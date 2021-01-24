package org.screamingsandals.lib.utils.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AbstractServiceWithExecutor {
    protected final ExecutorService executor;

    public AbstractServiceWithExecutor(String threadName) {
        this.executor = Executors
                .newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder()
                        .setNameFormat(threadName + " - #%d")
                        .setDaemon(true)
                        .build());
    }

    public void destroy() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60L, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60L, TimeUnit.SECONDS)) {
                    throw new UnsupportedOperationException("ExecutorService did not terminated, something is wrong..");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}

