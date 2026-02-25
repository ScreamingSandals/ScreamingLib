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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.command.Context;
import org.screamingsandals.lib.command.SuggestionProvider;
import org.screamingsandals.lib.sender.CommandSender;

import java.util.List;
import java.util.Queue;

public final class BooleanArgument<C extends CommandSender> extends Argument<C, Boolean> {
    @SuppressWarnings("unchecked")
    public BooleanArgument(@NotNull String name, boolean required, @Nullable Boolean defaultValue, @Nullable SuggestionProvider<C> suggestionProvider) {
        super(name, (Parser<C>) Parser.INSTANCE, required, defaultValue, suggestionProvider);
    }

    public static final class Parser<C extends CommandSender> implements Argument.Parser<C, Boolean> {
        private static final @NotNull Parser<?> INSTANCE = new Parser<>();
        private static final @NotNull List<String> SUGGESTION = List.of("true", "false");
        private static final @NotNull ArgumentResult<Boolean> TRUE = ArgumentResult.success(true);
        private static final @NotNull ArgumentResult<Boolean> FALSE = ArgumentResult.success(false);

        @Override
        public @NotNull ArgumentResult<Boolean> parse(@NotNull Context<C> context, @NotNull Queue<@NotNull String> inputQueue) {
            var input = inputQueue.peek();
            if (input == null) {
                return ArgumentResult.fail(new RuntimeException("No input")); // TODO: specific exception
            }

            if ("true".equalsIgnoreCase(input) || "yes".equalsIgnoreCase(input) || "on".equalsIgnoreCase(input)) {
                inputQueue.remove();
                return TRUE;
            }
            if ("false".equalsIgnoreCase(input) || "no".equalsIgnoreCase(input) || "off".equalsIgnoreCase(input)) {
                inputQueue.remove();
                return FALSE;
            }

            return ArgumentResult.fail(new RuntimeException("Expected boolean, got '" + input + "'")); // TODO: specific exception
        }

        @Override
        public @Nullable List<@NotNull String> suggest(@NotNull Context<C> context, @NotNull String input) {
            return SUGGESTION;
        }
    }
}
