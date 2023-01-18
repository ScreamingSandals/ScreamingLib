/*
 * Copyright 2023 ScreamingSandals
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

//
// MIT License
//
// Copyright (c) 2021 Alexander Söderberg & Contributors
// Copyright (c) 2022 ScreamingSandals
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package org.screamingsandals.lib.cloud.extras;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.captions.CaptionVariable;
import cloud.commandframework.captions.StandardCaptionKeys;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import cloud.commandframework.exceptions.parsing.ParserException;
import cloud.commandframework.types.tuples.Pair;
import io.leangen.geantyref.TypeToken;

import java.util.*;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Color;

/**
 * Parser for color codes.
 * <p>
 * Accepts names, Legacy Minecraft {@literal &} color codes, and Hex Codes (#RRGGBB or RRGGBB)
 *
 * @param <C> Command sender type
 */
public final class ColorArgument<C> extends CommandArgument<C, Color> {

    private static final @NotNull Pattern LEGACY_PREDICATE = Pattern.compile(
            "&[0-9a-fA-F]"
    );

    private static final @NotNull Pattern HEX_PREDICATE = Pattern.compile(
            "#?([a-fA-F0-9]{1,6})"
    );

    private static final @NotNull Collection<@NotNull Pair<Character, Color>> COLORS = Arrays.asList(
            Pair.of('0', Color.BLACK),
            Pair.of('1', Color.DARK_BLUE),
            Pair.of('2', Color.DARK_GREEN),
            Pair.of('3', Color.DARK_GREEN),
            Pair.of('4', Color.DARK_AQUA),
            Pair.of('5', Color.DARK_PURPLE),
            Pair.of('6', Color.GOLD),
            Pair.of('7', Color.GRAY),
            Pair.of('8', Color.DARK_GRAY),
            Pair.of('9', Color.BLUE),
            Pair.of('a', Color.GREEN),
            Pair.of('b', Color.AQUA),
            Pair.of('c', Color.RED),
            Pair.of('d', Color.LIGHT_PURPLE),
            Pair.of('e', Color.YELLOW),
            Pair.of('f', Color.WHITE)
    );

    private ColorArgument(final boolean required, final @NotNull String name, final @NotNull String defaultValue) {
        super(
                required,
                name,
                new ColorParser<>(),
                defaultValue,
                TypeToken.get(Color.class),
                null,
                new LinkedList<>()
        );
    }

    /**
     * Create a new required TextColor argument
     *
     * @param name Argument name
     * @param <C>  Command sender type
     * @return Created argument
     */
    public static <C> @NotNull ColorArgument<C> of(final @NotNull String name) {
        return new ColorArgument<>(
                true,
                name,
                ""
        );
    }

    /**
     * Create a new optional TextColor argument
     *
     * @param name Argument name
     * @param <C>  Command sender type
     * @return Created argument
     */
    public static <C> @NotNull ColorArgument<C> optional(final @NotNull String name) {
        return new ColorArgument<>(
                false,
                name,
                ""
        );
    }

    /**
     * Create a new optional TextColor argument
     *
     * @param name         Argument name
     * @param defaultValue Default value
     * @param <C>          Command sender type
     * @return Created argument
     */
    public static <C> @NotNull ColorArgument<C> optionalWithDefault(final @NotNull String name, final @NotNull String defaultValue) {
        return new ColorArgument<>(
                false,
                name,
                defaultValue
        );
    }


    public static final class ColorParser<C> implements ArgumentParser<C, Color> {

        @Override
        public @NotNull ArgumentParseResult<@NotNull Color> parse(final @NotNull CommandContext<@NotNull C> commandContext, final @NotNull Queue<@NotNull String> inputQueue) {
            final String input = inputQueue.peek();
            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        ColorParser.class,
                        commandContext
                ));
            }
            if (LEGACY_PREDICATE.matcher(input).matches()) {
                final char code = input.substring(1).toLowerCase(Locale.ROOT).charAt(0);
                for (final Pair<Character, Color> pair : COLORS) {
                    if (pair.getFirst() == code) {
                        inputQueue.remove();
                        return ArgumentParseResult.success(
                                pair.getSecond()
                        );
                    }
                }
            }
            for (final Pair<Character, Color> pair : COLORS) {
                if (pair.getSecond().toString().equalsIgnoreCase(input)) {
                    inputQueue.remove();
                    return ArgumentParseResult.success(
                            pair.getSecond()
                    );
                }
            }
            if (HEX_PREDICATE.matcher(input).matches()) {
                inputQueue.remove();
                return ArgumentParseResult.success(
                        Color.hexOrName(input)
                );
            }
            return ArgumentParseResult.failure(
                    new ColorParseException(
                            commandContext,
                            input
                    )
            );
        }

        @Override
        public @NotNull List<@NotNull String> suggestions(final @NotNull CommandContext<C> commandContext, final @NotNull String input) {
            final List<String> suggestions = new LinkedList<>();
            if (input.isEmpty() || "#".equals(input) || (HEX_PREDICATE.matcher(input).matches()
                    && input.length() < (input.startsWith("#") ? 7 : 6))) {
                for (char c = 'a'; c <= 'f'; c++) {
                    suggestions.add(String.format("%s%c", input, c));
                    suggestions.add(String.format("&%c", c));
                }
                for (char c = '0'; c <= '9'; c++) {
                    suggestions.add(String.format("%s%c", input, c));
                    suggestions.add(String.format("&%c", c));
                }
            }
            suggestions.addAll(Color.NAMED_VALUES.keySet());
            return suggestions;
        }

    }


    private static final class ColorParseException extends ParserException {

        private static final long serialVersionUID = -6236625328843879518L;

        private ColorParseException(final @NotNull CommandContext<?> commandContext, final @NotNull String input) {
            super(
                    ColorParser.class,
                    commandContext,
                    StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_COLOR,
                    CaptionVariable.of("input", input)
            );
        }

    }

}
