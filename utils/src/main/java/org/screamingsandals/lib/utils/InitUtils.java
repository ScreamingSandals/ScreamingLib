package org.screamingsandals.lib.utils;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * InitUtils are useful only if you are not using screaming-annotation.
 *
 * @see org.screamingsandals.lib.utils.annotations.Service
 */
@UtilityClass
public class InitUtils {
    public void doIf(Supplier<Boolean> booleanSupplier, Runnable runnable) {
        if (booleanSupplier.get()) {
            runnable.run();
        }
    }

    public void doIf(Supplier<Boolean> booleanSupplier, Runnable runnable, Runnable elseRunnable) {
        if (booleanSupplier.get()) {
            runnable.run();
        } else {
            elseRunnable.run();
        }
    }

    public void doIfNot(Supplier<Boolean> booleanSupplier, Runnable runnable) {
        if (!booleanSupplier.get()) {
            runnable.run();
        }
    }

    public void doIfNot(Supplier<Boolean> booleanSupplier, Runnable runnable, Runnable elseRunnable) {
        if (!booleanSupplier.get()) {
            runnable.run();
        } else {
            elseRunnable.run();
        }
    }

    /**
     * Creates plugin-less environment for initializing services.
     * Don't use if you are using screaming-annotation processor.
     * After init, it automatically executes the enable state.
     *
     * @param consumer that consumes Controllable instance
     * @return the created Controllable instance
     */
    public ControllableImpl pluginlessEnvironment(Consumer<Controllable> consumer) {
        var controllable = new ControllableImpl();
        consumer.accept(controllable);
        controllable.enable();
        controllable.postEnable();
        return controllable;
    }
}
