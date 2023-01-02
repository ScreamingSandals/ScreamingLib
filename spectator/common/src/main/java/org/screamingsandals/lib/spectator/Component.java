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

    static Component empty() {
        return Spectator.getBackend().empty();
    }

    static Component newLine() {
        return Spectator.getBackend().newLine();
    }

    static Component space() {
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
    static @NotNull Component fromMiniMessage(@NotNull String miniMessage, @NotNull Placeholder...placeholders) {
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

    static TextComponent.Builder text() {
        return Spectator.getBackend().text();
    }

    static TextComponent text(String text) {
        return Spectator.getBackend().text().content(text).build();
    }

    static TextComponent text(byte value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static TextComponent text(short value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static TextComponent text(int value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static TextComponent text(long value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static TextComponent text(float value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static TextComponent text(double value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static TextComponent text(Number value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static TextComponent text(boolean value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static TextComponent text(char value) {
        return Spectator.getBackend().text().content(value).build();
    }

    static TextComponent text(String text, Color color) {
        return Spectator.getBackend().text().content(text).color(color).build();
    }

    static TextComponent text(byte value, Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static TextComponent text(short value, Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static TextComponent text(int value, Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static TextComponent text(long value, Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static TextComponent text(float value, Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static TextComponent text(double value, Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static TextComponent text(Number value, Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static TextComponent text(boolean value, Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static TextComponent text(char value, Color color) {
        return Spectator.getBackend().text().content(value).color(color).build();
    }

    static TranslatableComponent.Builder translatable() {
        return Spectator.getBackend().translatable();
    }

    static @NotNull Component join(@NotNull Component separator, @NotNull Collection<@NotNull Component> components) {
        return join(separator, components.toArray(Component[]::new));
    }

    static @NotNull Component join(@NotNull Component separator, @NotNull Component @NotNull ... components) {
        var finalComponents = new ArrayList<Component>();

        for (var comp : components) {
            if (!finalComponents.isEmpty()) {
                finalComponents.add(separator);
            }
            finalComponents.add(comp);
        }

        return Component.text().append(finalComponents).build();
    }

    @Unmodifiable
    List<Component> children();

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withChildren(@Nullable List<Component> children);

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withAppendix(Component component);

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withAppendix(ComponentLike component);

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withAppendix(Component... components);

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withAppendix(ComponentLike... component);

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withAppendix(Collection<Component> components);

    @Nullable
    Color color();

    @NotNull
    @Contract(pure = true)
    Component withColor(@Nullable Color color);


    @Contract(pure = true)
    default @NotNull Component withColorIfAbsent(@Nullable Color color) {
        if (color() == null) {
            return withColor(color);
        }
        return this;
    }

    @LimitedVersionSupport(">= 1.16")
    @Nullable
    NamespacedMappingKey font();

    @NotNull
    @LimitedVersionSupport(">= 1.16")
    @Contract(pure = true)
    Component withFont(@Nullable NamespacedMappingKey font);

    TriState bold();

    @NotNull
    @Contract(pure = true)
    Component withBold(boolean bold);

    @NotNull
    @Contract(pure = true)
    Component withBold(TriState bold);

    TriState italic();

    @NotNull
    @Contract( pure = true)
    Component withItalic(boolean italic);

    @NotNull
    @Contract( pure = true)
    Component withItalic(TriState italic);

    TriState underlined();

    @NotNull
    @Contract(pure = true)
    Component withUnderlined(boolean underlined);

    @NotNull
    @Contract(pure = true)
    Component withUnderlined(TriState underlined);

    TriState strikethrough();

    @NotNull
    @Contract(pure = true)
    Component withStrikethrough(boolean strikethrough);

    @NotNull
    @Contract(pure = true)
    Component withStrikethrough(TriState strikethrough);

    TriState obfuscated();

    @NotNull
    @Contract(pure = true)
    Component withObfuscated(boolean obfuscated);

    @NotNull
    @Contract(pure = true)
    Component withObfuscated(TriState obfuscated);

    @Nullable
    String insertion();

    @NotNull
    @Contract(pure = true)
    Component withInsertion(@Nullable String insertion);

    @Nullable
    HoverEvent hoverEvent();

    @NotNull
    @Contract(pure = true)
    Component withHoverEvent(@Nullable HoverEvent hoverEvent);

    @NotNull
    @Contract(pure = true)
    Component withHoverEvent(@Nullable ItemContent itemContent);

    @Contract(pure = true)
    default @NotNull Component withHoverEvent(@Nullable ItemContentLike itemContent) {
        return withHoverEvent(itemContent == null ? null : itemContent.asItemContent());
    }

    @NotNull
    @Contract(pure = true)
    Component withHoverEvent(@Nullable EntityContent entityContent);

    @Contract(pure = true)
    default @NotNull Component withHoverEvent(@Nullable EntityContentLike entityContent) {
        return withHoverEvent(entityContent == null ? null : entityContent.asEntityContent());
    }

    @NotNull
    @Contract(pure = true)
    Component withHoverEvent(@Nullable Component component);

    @NotNull
    @Contract(pure = true)
    Component withHoverEvent(@Nullable ComponentLike component);

    @Nullable
    ClickEvent clickEvent();

    @NotNull
    @Contract(pure = true)
    Component withClickEvent(@Nullable ClickEvent clickEvent);

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

    String toLegacy();

    String toPlainText();

    String toJavaJson();

    boolean hasStyling();

    interface Builder<B extends Builder<B, C>, C extends Component> extends ComponentLike {
        B color(Color color);

        default B append(String text) {
            return append(Component.text(text));
        }

        B append(Component component);

        B append(ComponentLike component);

        B append(Component... components);

        B append(ComponentLike... components);

        B append(Collection<Component> components);

        @LimitedVersionSupport(">= 1.16")
        B font(NamespacedMappingKey font);

        default B bold() {
            return bold(true);
        }

        B bold(boolean bold);

        B bold(TriState bold);

        default B italic() {
            return italic(true);
        }

        B italic(boolean italic);

        B italic(TriState italic);

        default B underlined() {
            return underlined(true);
        }

        B underlined(boolean underlined);

        B underlined(TriState underlined);

        default B strikethrough() {
            return strikethrough(true);
        }

        B strikethrough(boolean strikethrough);

        B strikethrough(TriState strikethrough);

        default B obfuscated() {
            return obfuscated(true);
        }

        B obfuscated(TriState obfuscated);

        B obfuscated(boolean obfuscated);

        B insertion(@Nullable String insertion);

        B hoverEvent(@Nullable HoverEvent event);

        default B hoverEvent(@Nullable HoverEvent.Builder event) {
            return hoverEvent(event != null ? event.build() : null);
        }

        B hoverEvent(@Nullable ItemContent itemContent);

        default B hoverEvent(@Nullable ItemContent.Builder itemContent) {
            return hoverEvent(itemContent != null ? itemContent.build() : null);
        }

        B hoverEvent(@Nullable EntityContent entityContent);

        default B hoverEvent(@Nullable EntityContent.Builder entityContent) {
            return hoverEvent(entityContent != null ? entityContent.build() : null);
        }

        B hoverEvent(@Nullable ComponentLike component);

        B hoverEvent(@Nullable Component component);

        B clickEvent(@Nullable ClickEvent event);

        boolean hasStyling();

        C build();

        @Override
        default @NotNull Component asComponent() {
            return build();
        }
    }
}
