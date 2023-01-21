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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.*;
import org.screamingsandals.lib.spectator.mini.MiniMessageParser;
import org.screamingsandals.lib.spectator.mini.placeholders.Placeholder;
import org.screamingsandals.lib.spectator.utils.ComponentUtils;
import org.screamingsandals.lib.spectator.utils.SimpleTextReplacement;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.TriState;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public interface Component extends ComponentLike, Wrapper, Content, RawValueHolder {

    static @NotNull Component empty() {
        return Spectator.getBackend().empty();
    }

    static @NotNull Component newLine() {
        return Spectator.getBackend().newLine();
    }

    static @NotNull Component space() {
        return Spectator.getBackend().space();
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Component fromLegacy(@Nullable String legacy) {
        return Spectator.getBackend().fromLegacy(legacy);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull Component fromLegacy(@Nullable String legacy, char colorChar) {
        return Spectator.getBackend().fromLegacy(legacy, colorChar);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Component fromJavaJson(@Nullable String json) {
        return Spectator.getBackend().fromJson(json);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Component fromMiniMessage(@NotNull String miniMessage) {
        return MiniMessageParser.INSTANCE.parse(miniMessage);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull Component fromMiniMessage(@NotNull String miniMessage, @NotNull Placeholder @NotNull...placeholders) {
        return MiniMessageParser.INSTANCE.parse(miniMessage, placeholders);
    }

    static BlockNBTComponent.Builder blockNBT() {
        return Spectator.getBackend().blockNBT();
    }

    static EntityNBTComponent.Builder entityNBT() {
        return Spectator.getBackend().entityNBT();
    }

    static KeybindComponent.Builder keybind() {
        return Spectator.getBackend().keybind();
    }

    static ScoreComponent.Builder score() {
        return Spectator.getBackend().score();
    }

    static SelectorComponent.Builder selector() {
        return Spectator.getBackend().selector();
    }

    static StorageNBTComponent.Builder storageNBT() {
        return Spectator.getBackend().storageNBT();
    }

    static TextComponent.@NotNull Builder text() {
        return Spectator.getBackend().text();
    }

    static @NotNull TextComponent text(@NotNull String text) {
        return Spectator.getBackend().text().content(text).build();
    }

    static @NotNull TextComponent text(byte value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static @NotNull TextComponent text(short value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static @NotNull TextComponent text(int value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static @NotNull TextComponent text(long value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static @NotNull TextComponent text(float value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static @NotNull TextComponent text(double value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static @NotNull TextComponent text(@NotNull Number value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static @NotNull TextComponent text(boolean value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static @NotNull TextComponent text(char value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static @NotNull TextComponent text(@NotNull String text, @NotNull Color color) {
        return Spectator.getBackend().text().content(text).color(color).build();
    }

    static @NotNull TextComponent text(byte value, @NotNull Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static @NotNull TextComponent text(short value, @NotNull Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static @NotNull TextComponent text(int value, @NotNull Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static @NotNull TextComponent text(long value, @NotNull Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static @NotNull TextComponent text(float value, @NotNull Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static @NotNull TextComponent text(double value, @NotNull Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static @NotNull TextComponent text(@NotNull Number value, @NotNull Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static @NotNull TextComponent text(boolean value, @NotNull Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static @NotNull TextComponent text(char value, @NotNull Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static TranslatableComponent.@NotNull Builder translatable() {
        return Spectator.getBackend().translatable();
    }

    static @NotNull Component join(@NotNull Component separator, @NotNull Collection<@NotNull Component> components) {
        return join(separator, components.toArray(Component[]::new));
    }

    static @NotNull Component join(@NotNull Component separator, @NotNull Component @NotNull... components) {
        var finalComponents = new ArrayList<Component>();

        for (var comp : components) {
            if (!finalComponents.isEmpty()) {
                finalComponents.add(separator);
            }
            finalComponents.add(comp);
        }

        return Component.text().append(finalComponents).build();
    }

    @Unmodifiable @NotNull List<@NotNull Component> children();

    @Contract(value = "_ -> new", pure = true)
    @NotNull Component withChildren(@Nullable List<@NotNull Component> children);

    @Contract(value = "_ -> new", pure = true)
    @NotNull Component withAppendix(@NotNull Component component);

    @Contract(value = "_ -> new", pure = true)
    @NotNull Component withAppendix(@NotNull ComponentLike component);

    @Contract(value = "_ -> new", pure = true)
    @NotNull Component withAppendix(@NotNull Component @NotNull... components);

    @Contract(value = "_ -> new", pure = true)
    @NotNull Component withAppendix(@NotNull ComponentLike @NotNull... component);

    @Contract(value = "_ -> new", pure = true)
    @NotNull Component withAppendix(@NotNull Collection<@NotNull Component> components);

    @Nullable Color color();

    @Contract(pure = true)
    @NotNull Component withColor(@Nullable Color color);


    @Contract(pure = true)
    default @NotNull Component withColorIfAbsent(@Nullable Color color) {
        if (color() == null) {
            return withColor(color);
        }
        return this;
    }

    @LimitedVersionSupport(">= 1.16")
    @Nullable NamespacedMappingKey font();

    @LimitedVersionSupport(">= 1.16")
    @Contract(pure = true)
    @NotNull Component withFont(@Nullable NamespacedMappingKey font);

    @NotNull TriState bold();

    @Contract(pure = true)
    @NotNull Component withBold(boolean bold);

    @Contract(pure = true)
    @NotNull Component withBold(@NotNull TriState bold);

    @NotNull TriState italic();

    @Contract(pure = true)
    @NotNull Component withItalic(boolean italic);

    @Contract(pure = true)
    @NotNull Component withItalic(@NotNull TriState italic);

    @NotNull TriState underlined();

    @Contract(pure = true)
    @NotNull Component withUnderlined(boolean underlined);

    @Contract(pure = true)
    @NotNull Component withUnderlined(@NotNull TriState underlined);

    @NotNull TriState strikethrough();

    @Contract(pure = true)
    @NotNull Component withStrikethrough(boolean strikethrough);

    @Contract(pure = true)
    @NotNull Component withStrikethrough(@NotNull TriState strikethrough);

    @NotNull TriState obfuscated();

    @Contract(pure = true)
    @NotNull Component withObfuscated(boolean obfuscated);

    @Contract(pure = true)
    @NotNull Component withObfuscated(@NotNull TriState obfuscated);

    @Nullable String insertion();

    @Contract(pure = true)
    @NotNull Component withInsertion(@Nullable String insertion);

    @Nullable HoverEvent hoverEvent();

    @Contract(pure = true)
    @NotNull Component withHoverEvent(@Nullable HoverEvent hoverEvent);

    @Contract(pure = true)
    @NotNull Component withHoverEvent(@Nullable ItemContent itemContent);

    @Contract(pure = true)
    default @NotNull Component withHoverEvent(@Nullable ItemContentLike itemContent) {
        return withHoverEvent(itemContent == null ? null : itemContent.asItemContent());
    }

    @Contract(pure = true)
    @NotNull Component withHoverEvent(@Nullable EntityContent entityContent);

    @Contract(pure = true)
    default @NotNull Component withHoverEvent(@Nullable EntityContentLike entityContent) {
        return withHoverEvent(entityContent == null ? null : entityContent.asEntityContent());
    }

    @Contract(pure = true)
    @NotNull Component withHoverEvent(@Nullable Component component);

    @Contract(pure = true)
    @NotNull Component withHoverEvent(@Nullable ComponentLike component);

    @Nullable ClickEvent clickEvent();

    @Contract(pure = true)
    @NotNull Component withClickEvent(@Nullable ClickEvent clickEvent);

    @Contract(pure = true)
    default @NotNull Component replaceText(@NotNull Pattern pattern, @NotNull String replacement) {
        return SimpleTextReplacement.builder().matchPattern(pattern).replacement(matchResult -> replacement).build().replace(this);
    }

    @Contract(pure = true)
    default @NotNull Component replaceText(@NotNull Pattern pattern, @NotNull Function<@NotNull MatchResult, @Nullable String> replacement) {
        return SimpleTextReplacement.builder().matchPattern(pattern).replacement(replacement).build().replace(this);
    }

    @Contract(pure = true)
    default @NotNull Component replaceText(@NotNull String literal, @NotNull String replacement) {
        return SimpleTextReplacement.builder().matchPattern(Pattern.compile(literal, Pattern.LITERAL)).replacement(matchResult -> replacement).build().replace(this);
    }

    @Contract(pure = true)
    default @NotNull Component replaceText(@NotNull String literal, @NotNull Function<@NotNull MatchResult, @Nullable String> replacement) {
        return SimpleTextReplacement.builder().matchPattern(Pattern.compile(literal, Pattern.LITERAL)).replacement(replacement).build().replace(this);
    }

    @Override
    default @NotNull Component asComponent() {
        return this;
    }

    @Override
    default @NotNull Content asContent() {
        return this;
    }

    @Contract(pure = true)
    default @NotNull Component repeat(int repetitions) {
        return ComponentUtils.repeat(this, repetitions, null);
    }

    @Contract(pure = true)
    default @NotNull Component repeat(int repetitions, @Nullable Component separator) {
        return ComponentUtils.repeat(this, repetitions, separator);
    }

    @Contract(pure = true)
    default @NotNull Component linear(@NotNull ComponentBuilderApplicable @NotNull... applicables) {
        var newArr = new ComponentBuilderApplicable[applicables.length + 1];
        newArr[0] = this;
        System.arraycopy(applicables, 0, newArr, 1, applicables.length);
        return ComponentUtils.linear(newArr);
    }

    @NotNull String toLegacy();

    @NotNull String toPlainText();

    @NotNull String toJavaJson();

    boolean hasStyling();

    interface Builder<B extends Builder<B, C>, C extends Component> extends ComponentLike {
        @Contract("_ -> this")
        @NotNull B color(@NotNull Color color);

        @Contract("_ -> this")
        default @NotNull B append(@NotNull String text) {
            return append(Component.text(text));
        }

        @Contract("_ -> this")
        @NotNull B append(@NotNull Component component);

        @Contract("_ -> this")
        @NotNull B append(@NotNull ComponentLike component);

        @Contract("_ -> this")
        @NotNull B append(@NotNull Component @NotNull... components);

        @Contract("_ -> this")
        @NotNull B append(@NotNull ComponentLike @NotNull... components);

        @Contract("_ -> this")
        @NotNull B append(@NotNull Collection<@NotNull Component> components);

        @LimitedVersionSupport(">= 1.16")
        @Contract("_ -> this")
        @NotNull B font(@Nullable NamespacedMappingKey font);

        @Contract("-> this")
        default @NotNull B bold() {
            return bold(true);
        }

        @Contract("_ -> this")
        @NotNull B bold(boolean bold);

        @Contract("_ -> this")
        @NotNull B bold(@NotNull TriState bold);

        @Contract("-> this")
        default @NotNull B italic() {
            return italic(true);
        }

        @Contract("_ -> this")
        @NotNull B italic(boolean italic);

        @Contract("_ -> this")
        @NotNull B italic(@NotNull TriState italic);

        @Contract("-> this")
        default @NotNull B underlined() {
            return underlined(true);
        }

        @Contract("_ -> this")
        @NotNull B underlined(boolean underlined);

        @Contract("_ -> this")
        @NotNull B underlined(@NotNull TriState underlined);

        @Contract("-> this")
        default @NotNull B strikethrough() {
            return strikethrough(true);
        }

        @Contract("_ -> this")
        @NotNull B strikethrough(boolean strikethrough);

        @Contract("_ -> this")
        @NotNull B strikethrough(@NotNull TriState strikethrough);

        @Contract("-> this")
        default @NotNull B obfuscated() {
            return obfuscated(true);
        }

        @Contract("_ -> this")
        @NotNull B obfuscated(@NotNull TriState obfuscated);

        @Contract("_ -> this")
        @NotNull B obfuscated(boolean obfuscated);

        @Contract("_ -> this")
        @NotNull B insertion(@Nullable String insertion);

        @Contract("_ -> this")
        @NotNull B hoverEvent(@Nullable HoverEvent event);

        @Contract("_ -> this")
        default @NotNull B hoverEvent(HoverEvent.@Nullable Builder event) {
            return hoverEvent(event != null ? event.build() : null);
        }

        @Contract("_ -> this")
        @NotNull B hoverEvent(@Nullable ItemContent itemContent);

        @Contract("_ -> this")
        default @NotNull B hoverEvent(ItemContent.@Nullable Builder itemContent) {
            return hoverEvent(itemContent != null ? itemContent.build() : null);
        }

        @Contract("_ -> this")
        @NotNull B hoverEvent(@Nullable EntityContent entityContent);

        @Contract("_ -> this")
        default @NotNull B hoverEvent(EntityContent.@Nullable Builder entityContent) {
            return hoverEvent(entityContent != null ? entityContent.build() : null);
        }

        @Contract("_ -> this")
        @NotNull B hoverEvent(@Nullable ComponentLike component);

        @Contract("_ -> this")
        @NotNull B hoverEvent(@Nullable Component component);

        @Contract("_ -> this")
        @NotNull B clickEvent(@Nullable ClickEvent event);

        boolean hasStyling();

        @Contract(value = "-> new", pure = true)
        @NotNull C build();

        @Override
        default @NotNull Component asComponent() {
            return build();
        }
    }
}
