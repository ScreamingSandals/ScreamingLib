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

package org.screamingsandals.lib.impl.utils.logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.logger.Logger;

public class PrefixedLogger extends BasicWrapper<Logger> implements Logger {
    private final @NotNull String name;
    private final @NotNull String prefix;

    public PrefixedLogger(@NotNull Logger wrappedObject, @NotNull String prefix) {
        super(wrappedObject);
        this.prefix = "[" + prefix.trim() + "] ";
        if (wrappedObject.getName() != null) {
            this.name = wrappedObject.getName() + "." + prefix;
        } else {
            this.name = prefix;
        }
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return wrappedObject.isTraceEnabled();
    }

    @Override
    public void trace(@NotNull String msg) {
        wrappedObject.trace(this.prefix + msg);
    }

    @Override
    public void trace(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        wrappedObject.trace(this.prefix + format, argument, throwable);
    }

    @Override
    public void trace(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        wrappedObject.trace(this.prefix + format, arguments);
    }

    @Override
    public void trace(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.trace(this.prefix + msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return wrappedObject.isDebugEnabled();
    }

    @Override
    public void debug(@NotNull String msg) {
        wrappedObject.debug(this.prefix + msg);
    }

    @Override
    public void debug(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        wrappedObject.debug(this.prefix + format, argument, throwable);
    }

    @Override
    public void debug(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        wrappedObject.debug(this.prefix + format, arguments);
    }

    @Override
    public void debug(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.debug(this.prefix + msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return wrappedObject.isInfoEnabled();
    }

    @Override
    public void info(@NotNull String msg) {
        wrappedObject.info(this.prefix + msg);
    }

    @Override
    public void info(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        wrappedObject.info(this.prefix + format, argument, throwable);
    }

    @Override
    public void info(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        wrappedObject.info(this.prefix + format, arguments);
    }

    @Override
    public void info(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.info(this.prefix + msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return wrappedObject.isWarnEnabled();
    }

    @Override
    public void warn(@NotNull String msg) {
        wrappedObject.warn(this.prefix + msg);
    }

    @Override
    public void warn(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        wrappedObject.warn(this.prefix + format, arguments);
    }

    @Override
    public void warn(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        wrappedObject.warn(this.prefix + format, argument, throwable);
    }

    @Override
    public void warn(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.warn(this.prefix + msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return wrappedObject.isErrorEnabled();
    }

    @Override
    public void error(@NotNull String msg) {
        wrappedObject.error(this.prefix + msg);
    }

    @Override
    public void error(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        wrappedObject.error(this.prefix + format, argument, throwable);
    }

    @Override
    public void error(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        wrappedObject.error(this.prefix + format, arguments);
    }

    @Override
    public void error(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.error(this.prefix + msg, t);
    }
}
