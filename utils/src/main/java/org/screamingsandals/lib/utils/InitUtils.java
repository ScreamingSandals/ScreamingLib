package org.screamingsandals.lib.utils;

import java.util.function.Supplier;

public class InitUtils {
    public static void doIf(Supplier<Boolean> booleanSupplier, Runnable runnable) {
        if (booleanSupplier.get()) {
            runnable.run();
        }
    }

    public static void doIf(Supplier<Boolean> booleanSupplier, Runnable runnable, Runnable elseRunnable) {
        if (booleanSupplier.get()) {
            runnable.run();
        } else {
            elseRunnable.run();
        }
    }

    public static void doIfNot(Supplier<Boolean> booleanSupplier, Runnable runnable) {
        if (!booleanSupplier.get()) {
            runnable.run();
        }
    }

    public static void doIfNot(Supplier<Boolean> booleanSupplier, Runnable runnable, Runnable elseRunnable) {
        if (!booleanSupplier.get()) {
            runnable.run();
        } else {
            elseRunnable.run();
        }
    }
}
