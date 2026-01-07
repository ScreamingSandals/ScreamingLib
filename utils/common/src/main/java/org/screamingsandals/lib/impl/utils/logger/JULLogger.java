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
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.logger.Logger;
import org.screamingsandals.lib.utils.logger.LoggerWrapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class JULLogger extends BasicWrapper<java.util.logging.Logger> implements Logger, LoggerWrapper {
    public JULLogger(@NotNull java.util.logging.Logger wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable String getName() {
        return wrappedObject.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return wrappedObject.isLoggable(Level.FINEST);
    }

    @Override
    public void trace(@NotNull String msg) {
        wrappedObject.log(Level.FINEST, msg);
    }

    @Override
    public void trace(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        var msg = getMsg(format, new Object[]{argument}, throwable);
        wrappedObject.log(Level.FINEST, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void trace(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        var msg = getMsg(format, Arrays.asList(arguments));
        wrappedObject.log(Level.FINEST, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void trace(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.log(Level.FINEST, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return wrappedObject.isLoggable(Level.FINE);
    }

    @Override
    public void debug(@NotNull String msg) {
        wrappedObject.log(Level.FINE, msg);
    }

    @Override
    public void debug(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        var msg = getMsg(format, new Object[]{argument}, throwable);
        wrappedObject.log(Level.FINE, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void debug(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        var msg = getMsg(format, Arrays.asList(arguments));
        wrappedObject.log(Level.FINE, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void debug(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.log(Level.FINE, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return wrappedObject.isLoggable(Level.INFO);
    }

    @Override
    public void info(@NotNull String msg) {
        wrappedObject.log(Level.INFO, msg);
    }

    @Override
    public void info(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        var msg = getMsg(format, new Object[]{argument}, throwable);
        wrappedObject.log(Level.INFO, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void info(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        var msg = getMsg(format, Arrays.asList(arguments));
        wrappedObject.log(Level.INFO, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void info(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.log(Level.INFO, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return wrappedObject.isLoggable(Level.WARNING);
    }

    @Override
    public void warn(@NotNull String msg) {
        wrappedObject.log(Level.WARNING, msg);
    }

    @Override
    public void warn(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        var msg = getMsg(format, Arrays.asList(arguments));
        wrappedObject.log(Level.WARNING, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void warn(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        var msg = getMsg(format, new Object[]{argument}, throwable);
        wrappedObject.log(Level.WARNING, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void warn(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.log(Level.WARNING, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return wrappedObject.isLoggable(Level.SEVERE);
    }

    @Override
    public void error(@NotNull String msg) {
        wrappedObject.log(Level.SEVERE, msg);
    }

    @Override
    public void error(@NotNull String format, @Nullable Object argument, @NotNull Throwable throwable) {
        var msg = getMsg(format, new Object[]{argument}, throwable);
        wrappedObject.log(Level.SEVERE, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void error(@NotNull String format, @Nullable Object @NotNull ... arguments) {
        var msg = getMsg(format, Arrays.asList(arguments));
        wrappedObject.log(Level.SEVERE, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void error(@NotNull String msg, @NotNull Throwable t) {
        wrappedObject.log(Level.SEVERE, msg, t);
    }


    private @NotNull Pair<@NotNull String, @Nullable Throwable> getMsg(@NotNull String msg, @NotNull List<@Nullable Object> args) {
        var throwableCandidate = args.stream().filter(o -> o instanceof Throwable).findFirst().map(o -> (Throwable) o);
        throwableCandidate.ifPresent(args::remove);
        return getMsg(msg, args.toArray(), throwableCandidate.orElse(null));
    }

    private @NotNull Pair<@NotNull String, @Nullable Throwable> getMsg(@NotNull String msg, @Nullable Object @NotNull [] args, @Nullable Throwable throwable) {
        if (args.length == 0) {
            return Pair.of(msg, throwable);
        }

        return Pair.of(format(msg, args), throwable);
    }

    // Message formatting, similar to Slf4j
    // Based on https://github.com/qos-ch/slf4j/blob/316b5d1727d647250ff791565650070094f5b85e/slf4j-api/src/main/java/org/slf4j/helpers/MessageFormatter.java
    // Licensed under MIT

    private static final @NotNull String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    private @NotNull String format(@NotNull String msg, @Nullable Object @NotNull [] args) {
        int msgLength = msg.length();
        var builder = new StringBuilder(msgLength + 50);
        int argc = 0;
        int i = 0;
        while (i < msgLength) {
            int j = msg.indexOf(DELIM_STR, i);

            if (j == -1 || argc == args.length) {
                break;
            } else {
                if (j > i && msg.charAt(j - 1) == ESCAPE_CHAR) {
                    if (j > i + 1 && msg.charAt(j - 2) == ESCAPE_CHAR) {
                        builder.append(msg, i, j - 1);
                        append(builder, args[argc], new HashMap<>());
                        argc++;
                    } else {
                        builder.append(msg, i, j - 1);
                        builder.append(DELIM_STR);
                    }
                } else {
                    builder.append(msg, i, j);
                    append(builder, args[argc], new HashMap<>());
                    argc++;
                }
                i = j + DELIM_STR.length();
            }
        }

        return builder.append(msg, i, msgLength).toString();
    }

    private void append(@NotNull StringBuilder builder, @Nullable Object argument, @NotNull Map<Object @NotNull [], @Nullable Object> seenMap) {
        if (argument == null) {
            builder.append("null");
            return;
        }
        if (!argument.getClass().isArray()) {
            try {
                var string = argument.toString();
                builder.append(string);
            } catch (Throwable t) {
                builder.append("[FAILED ").append(argument.getClass().getSimpleName()).append("#toString()]");
            }
        } else {
            if (argument instanceof boolean[]) {
                builder.append(Arrays.toString((boolean[]) argument));
            } else if (argument instanceof byte[]) {
                builder.append(Arrays.toString((byte[]) argument));
            } else if (argument instanceof char[]) {
                builder.append(Arrays.toString((char[]) argument));
            } else if (argument instanceof short[]) {
                builder.append(Arrays.toString((short[]) argument));
            } else if (argument instanceof int[]) {
                builder.append(Arrays.toString((int[]) argument));
            } else if (argument instanceof long[]) {
                builder.append(Arrays.toString((long[]) argument));
            } else if (argument instanceof float[]) {
                builder.append(Arrays.toString((float[]) argument));
            } else if (argument instanceof double[]) {
                builder.append(Arrays.toString((double[]) argument));
            } else {
                builder.append('[');
                var array = (Object[]) argument;
                if (!seenMap.containsKey(array)) {
                    seenMap.put(array, null);
                    final int len = array.length;
                    for (int i = 0; i < len; i++) {
                        append(builder, array[i], seenMap);
                        if (i != len - 1) {
                            builder.append(", ");
                        }
                    }
                    seenMap.remove(array);
                } else {
                    builder.append("...");
                }
                builder.append(']');
            }
        }
    }
}
