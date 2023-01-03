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

package org.screamingsandals.lib.spectator.mini.placeholders;

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.minitag.nodes.TagNode;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.resolvers.ComponentBuilderResolver;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.function.*;

public interface Placeholder extends ComponentBuilderResolver {

    @Pattern("[a-z\\d_-]+")
    @NotNull String getName();

    <B extends Component.Builder<B, C>, C extends Component> @NotNull B getResult(@NotNull MiniMessageParser parser, @NotNull List<@NotNull String> arguments, @NotNull Placeholder @NotNull... placeholders);

    @Override
    default <B extends Component.Builder<B, C>, C extends Component> @NotNull B resolve(@NotNull MiniMessageParser parser, @NotNull TagNode tag, @NotNull Placeholder @NotNull ... placeholders) {
        return getResult(parser, tag.getArgs(), placeholders);
    }

    static @NotNull Placeholder component(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Component component) {
        return new ComponentPlaceholder(name, component);
    }

    static @NotNull Placeholder lazyComponent(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull Component> component) {
        return new LazyComponentPlaceholder(name, component);
    }

    static @NotNull Placeholder unparsed(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull String value) {
        return new StringPlaceholder(name, value);
    }

    static @NotNull Placeholder lazyUnparsed(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull String> value) {
        return new LazyStringPlaceholder(name, value);
    }

    static @NotNull Placeholder parsed(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull String value) {
        return new MiniPlaceholder(name, value);
    }

    static @NotNull Placeholder lazyParsed(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull String> value) {
        return new LazyMiniPlaceholder(name, value);
    }

    static @NotNull Placeholder number(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Number value) {
        return new NumberPlaceholder(name, value);
    }

    static @NotNull Placeholder lazyNumber(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull Number> value) {
        return new LazyNumberPlaceholder(name, value);
    }

    static @NotNull Placeholder lazyNumber(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull IntSupplier value) {
        return new LazyNumberPlaceholder(name, value::getAsInt);
    }

    static @NotNull Placeholder lazyNumber(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull LongSupplier value) {
        return new LazyNumberPlaceholder(name, value::getAsLong);
    }

    static @NotNull Placeholder lazyNumber(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull DoubleSupplier value) {
        return new LazyNumberPlaceholder(name, value::getAsDouble);
    }

    static @NotNull Placeholder bool(@Pattern("[a-z\\d_-]+") @NotNull String name, boolean value) {
        return new BooleanPlaceholder(name, value);
    }

    static @NotNull Placeholder lazyBool(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull BooleanSupplier value) {
        return new LazyBooleanPlaceholder(name, value);
    }

    static @NotNull Placeholder character(@Pattern("[a-z\\d_-]+") @NotNull String name, char value) {
        return new CharPlaceholder(name, value);
    }

    static @NotNull Placeholder lazyCharacter(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull Character> value) {
        return new LazyCharPlaceholder(name, value);
    }

    static @NotNull Placeholder dateTime(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull TemporalAccessor value) {
        return new DateTimePlaceholder(name, value);
    }

    static @NotNull Placeholder lazyDateTime(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull TemporalAccessor> value) {
        return new LazyDateTimePlaceholder(name, value);
    }

    static @NotNull Placeholder choice(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Number value) {
        return new ChoicePlaceholder(name, value);
    }

    static @NotNull Placeholder lazyChoice(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull Number> value) {
        return new LazyChoicePlaceholder(name, value);
    }

}
