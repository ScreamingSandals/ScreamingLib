package org.screamingsandals.lib.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Preconditions {
    public void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public void checkArgument(boolean expression, @NotNull Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public void checkNotNull(@Nullable Object reference) {
        if (reference == null) {
            throw new UnsupportedOperationException();
        }
    }

    public void checkNotNull(@Nullable Object reference, @NotNull Object errorMessage) {
        if (reference == null) {
            throw new UnsupportedOperationException(String.valueOf(errorMessage));
        }
    }
}
