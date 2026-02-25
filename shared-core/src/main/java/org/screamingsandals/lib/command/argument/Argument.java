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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.command.CommandBuilder;
import org.screamingsandals.lib.command.Context;
import org.screamingsandals.lib.command.SuggestionProvider;
import org.screamingsandals.lib.sender.CommandSender;

import java.util.List;
import java.util.Queue;

@Data
@Accessors(chain = true, fluent = true)
@AllArgsConstructor
public abstract class Argument<C extends CommandSender, T> implements CommandBuilder.Node {
    private final @NotNull String name;
    private final @NotNull Parser<C, T> parser;
    private final boolean required;
    private final @Nullable T defaultValue;
    private final @Nullable SuggestionProvider<C> suggestionProvider;

    public interface Parser<C extends CommandSender, T> extends SuggestionProvider<C> {
        @NotNull ArgumentResult<T> parse(@NotNull Context<C> context, @NotNull Queue<@NotNull String> inputQueue);

        @Override
        default @Nullable List<@NotNull String> suggest(@NotNull Context<C> context, @NotNull String input) {
            return null;
        }
    }
}
