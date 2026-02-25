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

package org.screamingsandals.lib.command.argument;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.command.Context;
import org.screamingsandals.lib.command.SuggestionProvider;
import org.screamingsandals.lib.sender.CommandSender;

import java.util.Queue;
import java.util.StringJoiner;

public class StringArgument<C extends CommandSender> extends Argument<C, String> {
    public StringArgument(@NotNull String name, boolean required, @Nullable String defaultValue, @Nullable SuggestionProvider<C> suggestionProvider, @NotNull Mode mode) {
        super(name, new Parser<>(mode), required, defaultValue, suggestionProvider);
    }

    @RequiredArgsConstructor
    public static final class Parser<C extends CommandSender> implements Argument.Parser<C, String> {
        private final @NotNull Mode mode;

        @Override
        public @NotNull ArgumentResult<String> parse(@NotNull Context<C> context, @NotNull Queue<@NotNull String> inputQueue) {
            var input = inputQueue.peek();
            if (input == null) {
                return ArgumentResult.fail(new RuntimeException("No input")); // TODO: specific exception
            }

            if (mode == Mode.SINGLE) {
                inputQueue.remove();
                return ArgumentResult.success(input);
            } else if (mode == Mode.QUOTED) {
                if (!input.startsWith("'") && !input.startsWith("\"")) {
                    inputQueue.remove();
                    return ArgumentResult.success(input);
                }

                var it = inputQueue.iterator();

                var builder = new StringBuilder();
                char quote = input.charAt(0);
                int consumed = 0;
                boolean closed = false;

                while (it.hasNext()) {
                    var token = it.next();
                    consumed++;

                    boolean escaped = false;

                    for (int i = (consumed == 1 ? 1 : 0); i < token.length(); i++) {
                        char c = token.charAt(i);

                        if (escaped) {
                            escaped = false;
                            continue;
                        }

                        if (c == '\\') {
                            escaped = true;
                            continue;
                        }

                        if (c == quote) {
                            if (closed) {
                                return ArgumentResult.fail(new RuntimeException("Unescaped closing quote in the middle of a token: " + builder + " " + token)); // TODO: specific exception
                            }

                            closed = true;
                        }
                    }

                    if (consumed > 1) {
                        builder.append(' ');
                    }
                    builder.append(token);

                    if (closed) {
                        break;
                    }
                }

                if (!closed) {
                    return ArgumentResult.fail(new RuntimeException("Unclosed quoted string")); // TODO: specific exception
                }

                var merged = builder.toString();
                merged = merged.substring(1, merged.length() - 1)
                        .replace("\\\"", "\"")
                        .replace("\\'", "'");

                for (int i = 0; i < consumed; i++) {
                    inputQueue.remove();
                }
                return ArgumentResult.success(merged);
            } else {
                var joiner = new StringJoiner(" ");
                for (var string : inputQueue) {
                    joiner.add(string);
                    inputQueue.remove();
                }
                return ArgumentResult.success(joiner.toString());
            }
        }
    }

    public enum Mode {
        SINGLE,
        QUOTED,
        GREEDY;
    }
}
