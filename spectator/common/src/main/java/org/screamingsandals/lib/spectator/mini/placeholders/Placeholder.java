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
    String getName();

    @NotNull
    <B extends Component.Builder<B, C>, C extends Component> B getResult(MiniMessageParser parser, List<String> arguments, Placeholder... placeholders);

    @Override
    default <B extends Component.Builder<B, C>, C extends Component> B resolve(@NotNull MiniMessageParser parser, @NotNull TagNode tag, Placeholder... placeholders) {
        return getResult(parser, tag.getArgs(), placeholders);
    }

    @NotNull
    static Placeholder component(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Component component) {
        return new ComponentPlaceholder(name, component);
    }

    @NotNull
    static Placeholder lazyComponent(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull Component> component) {
        return new LazyComponentPlaceholder(name, component);
    }

    @NotNull
    static Placeholder unparsed(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull String value) {
        return new StringPlaceholder(name, value);
    }

    @NotNull
    static Placeholder lazyUnparsed(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull String> value) {
        return new LazyStringPlaceholder(name, value);
    }

    @NotNull
    static Placeholder parsed(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull String value) {
        return new MiniPlaceholder(name, value);
    }

    @NotNull
    static Placeholder lazyParsed(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull String> value) {
        return new LazyMiniPlaceholder(name, value);
    }

    @NotNull
    static Placeholder number(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Number value) {
        return new NumberPlaceholder(name, value);
    }

    @NotNull
    static Placeholder lazyNumber(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull Number> value) {
        return new LazyNumberPlaceholder(name, value);
    }

    @NotNull
    static Placeholder lazyNumber(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull IntSupplier value) {
        return new LazyNumberPlaceholder(name, value::getAsInt);
    }

    @NotNull
    static Placeholder lazyNumber(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull LongSupplier value) {
        return new LazyNumberPlaceholder(name, value::getAsLong);
    }

    @NotNull
    static Placeholder lazyNumber(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull DoubleSupplier value) {
        return new LazyNumberPlaceholder(name, value::getAsDouble);
    }

    @NotNull
    static Placeholder bool(@Pattern("[a-z\\d_-]+") @NotNull String name, boolean value) {
        return new BooleanPlaceholder(name, value);
    }

    @NotNull
    static Placeholder lazyBool(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull BooleanSupplier value) {
        return new LazyBooleanPlaceholder(name, value);
    }

    @NotNull
    static Placeholder character(@Pattern("[a-z\\d_-]+") @NotNull String name, char value) {
        return new CharPlaceholder(name, value);
    }

    @NotNull
    static Placeholder lazyCharacter(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull Character> value) {
        return new LazyCharPlaceholder(name, value);
    }

    @NotNull
    static Placeholder dateTime(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull TemporalAccessor value) {
        return new DateTimePlaceholder(name, value);
    }

    @NotNull
    static Placeholder lazyDateTime(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull TemporalAccessor> value) {
        return new LazyDateTimePlaceholder(name, value);
    }

    @NotNull
    static Placeholder choice(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Number value) {
        return new ChoicePlaceholder(name, value);
    }

    @NotNull
    static Placeholder lazyChoice(@Pattern("[a-z\\d_-]+") @NotNull String name, @NotNull Supplier<@NotNull Number> value) {
        return new LazyChoicePlaceholder(name, value);
    }

}
