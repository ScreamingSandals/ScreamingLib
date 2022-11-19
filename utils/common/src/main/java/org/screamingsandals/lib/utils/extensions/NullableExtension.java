package org.screamingsandals.lib.utils.extensions;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Preconditions;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@UtilityClass
public class NullableExtension {
    @Contract(value = "null -> fail; _ -> param1", pure = true)
    public static <T> @NotNull T orElseThrow(@Nullable T obj) {
        if (obj == null) {
            throw new NoSuchElementException("No value present");
        }
        return obj;
    }

    @Contract(value = "null, _ -> fail; _, _ -> param1", pure = true)
    public static <T, X extends Throwable> @NotNull T orElseThrow(@Nullable T obj, @NotNull Supplier<? extends @NotNull X> exceptionSupplier) throws X {
        Preconditions.checkNotNullIllegal(exceptionSupplier);
        if (obj != null) {
            return obj;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Contract(value = "!null, _ -> param1; null, !null -> param2; null, null -> null", pure = true)
    public static <T> @Nullable T orElse(@Nullable T obj, @Nullable T defaultValue) {
        return obj == null ? defaultValue : obj;
    }

    @Contract(value = "!null, _ -> param1; null, _ -> param2", pure = true)
    public static <T> @Nullable T orElseGet(@Nullable T obj, @NotNull Supplier<? extends @Nullable T> defaultValue) {
        Preconditions.checkNotNullIllegal(defaultValue);
        return obj == null ? defaultValue.get() : obj;
    }

    @Contract(pure = true)
    public static <T> void ifNotNull(@Nullable T obj, @NotNull Consumer<? super @NotNull T> action) {
        Preconditions.checkNotNullIllegal(action);
        if (obj != null) {
            action.accept(obj);
        }
    }

    @Contract(pure = true)
    public static <T> void ifNotNullOrElse(@Nullable T obj, @NotNull Consumer<? super @NotNull T> action, @NotNull Runnable emptyAction) {
        Preconditions.checkNotNullIllegal(action);
        Preconditions.checkNotNullIllegal(emptyAction);
        if (obj != null) {
            action.accept(obj);
        } else {
            emptyAction.run();
        }
    }

    @Contract(value = "null, _ -> null", pure = true)
    public static <T> @Nullable T filterOrNull(@Nullable T obj, @NotNull Predicate<? super @NotNull T> predicate) {
        Preconditions.checkNotNullIllegal(predicate);
        if (obj == null) {
            return null;
        } else {
            return predicate.test(obj) ? obj : null;
        }
    }

    @Contract(value = "null, _ -> null", pure = true)
    public static <T, U> @Nullable U mapOrNull(@Nullable T obj, @NotNull Function<? super @NotNull T, ? extends @Nullable U> mapper) {
        Preconditions.checkNotNullIllegal(mapper);
        if (obj == null) {
            return null;
        } else {
            return mapper.apply(obj);
        }
    }

    @Contract(pure = true)
    public static <T> Stream<T> oneItemStream(@Nullable T obj) {
        return Stream.ofNullable(obj);
    }

    /**
     * Replacement for {@code orElse(null)} call which does not work correctly with this extension.
     */
    @SuppressWarnings({"all"})
    public static <T> @Nullable T toNullable(@NotNull Optional<T> optional) {
        return optional.orElse(null);
    }

    public static <T> @NotNull Optional<T> toOptional(@Nullable T nullable) {
        return Optional.ofNullable(nullable);
    }
}
