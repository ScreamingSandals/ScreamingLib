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

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.stream.Collectors;

public class EnumArgument<C extends CommandSender, E extends Enum<E>> extends Argument<C, E> {
    public EnumArgument(@NotNull String name, boolean required, @Nullable E defaultValue, @NotNull EnumSet<E> allowedValues, @Nullable SuggestionProvider<C> suggestionProvider) {
        super(name, new Parser<>(allowedValues), required, defaultValue, suggestionProvider);
    }

    @RequiredArgsConstructor
    public static final class Parser<C extends CommandSender, E extends Enum<E>> implements Argument.Parser<C, E> {
        private final @NotNull EnumSet<E> allowedValues;

        @Override
        public @NotNull ArgumentResult<E> parse(@NotNull Context<C> context, @NotNull Queue<@NotNull String> inputQueue) {
            var input = inputQueue.peek();
            if (input == null) {
                return ArgumentResult.fail(new RuntimeException("No input")); // TODO: specific exception
            }

            for (var value : this.allowedValues) {
                if (value.name().equalsIgnoreCase(input)) {
                    inputQueue.remove();
                    return ArgumentResult.success(value);
                }
            }

            return ArgumentResult.fail(new RuntimeException("Unexpected value " + input)); // TODO: specific exception
        }

        @Override
        public @Nullable List<@NotNull String> suggest(@NotNull Context<C> context, @NotNull String input) {
            return allowedValues.stream().map(e -> e.name().toLowerCase(Locale.ROOT)).collect(Collectors.toList());
        }
    }
}
