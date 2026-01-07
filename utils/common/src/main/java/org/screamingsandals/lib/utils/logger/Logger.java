/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.utils.logger;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.impl.utils.logger.PrefixedLogger;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface Logger extends Wrapper {

    @Nullable String getName();

    boolean isTraceEnabled();

    void trace(@NotNull String msg);

    void trace(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable);

    void trace(@NotNull String format, @Nullable Object @NotNull ... arguments);

    void trace(@NotNull String msg, @NotNull Throwable t);

    boolean isDebugEnabled();

    void debug(@NotNull String msg);

    void debug(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable);

    void debug(@NotNull String format, @Nullable Object @NotNull ... arguments);

    void debug(@NotNull String msg, @NotNull Throwable t);

    boolean isInfoEnabled();

    void info(@NotNull String msg);

    void info(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable);

    void info(@NotNull String format, @Nullable Object @NotNull ... arguments);

    void info(@NotNull String msg, @NotNull Throwable t);

    boolean isWarnEnabled();

    void warn(@NotNull String msg);

    void warn(@NotNull String format, @Nullable Object @NotNull ... arguments);

    void warn(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable);

    void warn(@NotNull String msg, @NotNull Throwable t);

    boolean isErrorEnabled();

    void error(@NotNull String msg);

    void error(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable);

    void error(@NotNull String format, @Nullable Object @NotNull ... arguments);

    void error(@NotNull String msg, @NotNull Throwable t);

    @Contract("_ -> new")
    default @NotNull Logger prefixed(@NotNull String prefix) {
        if (prefix.isBlank()) {
            return this;
        }

        return new PrefixedLogger(this, prefix);
    }

    @Contract("_ -> new")
    default @NotNull Logger prefixed(@NotNull Class<?> clazz) {
        return new PrefixedLogger(this, abbreviateClassName(clazz));
    }

    @Contract("_, _ -> new")
    default @NotNull Logger prefixed(@NotNull Class<?> clazz, @NotNull String subName) {
        if (subName.isBlank()) {
            return new PrefixedLogger(this, abbreviateClassName(clazz));
        } else {
            return new PrefixedLogger(this, abbreviateClassName(clazz) + "#" + subName);
        }
    }

    private @NotNull String abbreviateClassName(@NotNull Class<?> clazz) {
        String[] parts = clazz.getName().split("\\.");
        return Arrays.stream(parts, 0, parts.length - 1)
                .map(s -> s.substring(0, 1))
                .collect(Collectors.joining(".")) +
                "." + parts[parts.length - 1].replace('$', '.');
    }
}
