/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.utils.executor;

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

