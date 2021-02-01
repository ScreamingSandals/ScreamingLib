package org.screamingsandals.lib.utils.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class ExecutorProvider {

    public static ExecutorService buildExecutor(String name) {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder()
                .setNameFormat(name + " - #%d")
                .setDaemon(true)
                .build());
    }

    public static void destroyExecutor(ExecutorService executorService) {
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

