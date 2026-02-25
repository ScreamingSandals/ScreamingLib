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

public class FloatArgument<C extends CommandSender> extends Argument<C, Float> {
    @SuppressWarnings("unchecked")
    public FloatArgument(@NotNull String name, boolean required, @Nullable Float defaultValue, @Nullable SuggestionProvider<C> suggestionProvider) {
        super(name, (Parser<C>) Parser.INSTANCE, required, defaultValue, suggestionProvider);
    }

    public FloatArgument(@NotNull String name, boolean required, @Nullable Float defaultValue, @Nullable SuggestionProvider<C> suggestionProvider, float min, float max) {
        super(name, new Parser<>(min, max), required, defaultValue, suggestionProvider);
    }

    @RequiredArgsConstructor
    public static final class Parser<C extends CommandSender> implements Argument.Parser<C, Float> {
        private static final @NotNull Parser<?> INSTANCE = new Parser<>(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        private final float min;
        private final float max;

        @Override
        public @NotNull ArgumentResult<Float> parse(@NotNull Context<C> context, @NotNull Queue<@NotNull String> inputQueue) {
            var input = inputQueue.peek();
            if (input == null) {
                return ArgumentResult.fail(new RuntimeException("No input")); // TODO: specific exception
            }

            try {
                float value = Float.parseFloat(input);
                if (value < min || value > max) {
                    return ArgumentResult.fail(new RuntimeException("Expected value between '" + min + "' and '" + max + "' , got '" + value + "'")); // TODO: specific exception
                }
                inputQueue.remove();
                return ArgumentResult.success(value);
            } catch (Exception ex) {
                return ArgumentResult.fail(new RuntimeException("Expected float, got '" + input + "'", ex)); // TODO: specific exception
            }
        }
    }
}
