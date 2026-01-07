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
import org.screamingsandals.lib.utils.logger.LoggerWrapper;

public class Slf4jLogger extends BasicWrapper<org.slf4j.Logger> implements Logger, LoggerWrapper {

    public Slf4jLogger(@NotNull org.slf4j.Logger wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable String getName() {
        return wrappedObject.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return wrappedObject.isTraceEnabled();
    }

    @Override
    public void trace(@NotNull String msg) {
        wrappedObject.trace(msg);
    }

    @Override
    public void trace(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        wrappedObject.trace(format, argument, throwable);
    }

    @Override
    public void trace(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        wrappedObject.trace(format, arguments);
    }

    @Override
    public void trace(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return wrappedObject.isDebugEnabled();
    }

    @Override
    public void debug(@NotNull String msg) {
        wrappedObject.debug(msg);
    }

    @Override
    public void debug(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        wrappedObject.debug(format, argument, throwable);
    }

    @Override
    public void debug(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        wrappedObject.debug(format, arguments);
    }

    @Override
    public void debug(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return wrappedObject.isInfoEnabled();
    }

    @Override
    public void info(@NotNull String msg) {
        wrappedObject.info(msg);
    }

    @Override
    public void info(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        wrappedObject.info(format, argument, throwable);
    }

    @Override
    public void info(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        wrappedObject.info(format, arguments);
    }

    @Override
    public void info(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return wrappedObject.isWarnEnabled();
    }

    @Override
    public void warn(@NotNull String msg) {
        wrappedObject.warn(msg);
    }

    @Override
    public void warn(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        wrappedObject.warn(format, arguments);
    }

    @Override
    public void warn(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        wrappedObject.warn(format, argument, throwable);
    }

    @Override
    public void warn(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return wrappedObject.isErrorEnabled();
    }

    @Override
    public void error(@NotNull String msg) {
        wrappedObject.error(msg);
    }

    @Override
    public void error(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        wrappedObject.error(format, argument, throwable);
    }

    @Override
    public void error(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        wrappedObject.error(format, arguments);
    }

    @Override
    public void error(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.error(msg, t);
    }
}
