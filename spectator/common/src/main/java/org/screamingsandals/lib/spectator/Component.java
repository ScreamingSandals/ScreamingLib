/*
 * Copyright 2022 ScreamingSandals
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
import org.screamingsandals.lib.spectator.event.hover.Content;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Collection;
import java.util.List;

public interface Component extends ComponentLike, Wrapper, Content, RawValueHolder {

    static Component empty() {
        return Spectator.getBackend().empty();
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    static Component fromLegacy(@Nullable String legacy) {
        return Spectator.getBackend().fromLegacy(legacy);
    }

    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    static Component fromLegacy(@Nullable String legacy, char colorChar) {
        return Spectator.getBackend().fromLegacy(legacy, colorChar);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    static Component fromJson(@Nullable String json) {
        return Spectator.getBackend().fromJson(json);
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

    static TranslatableComponent.Builder translatable() {
        return Spectator.getBackend().translatable();
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
    Component withAppendix(Component... components);

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withAppendix(Collection<Component> components);

    @Nullable
    Color color();

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withColor(@Nullable Color color);

    @LimitedVersionSupport(">= 1.16")
    @Nullable
    NamespacedMappingKey font();

    @NotNull
    @LimitedVersionSupport(">= 1.16")
    @Contract(value = "_ -> new", pure = true)
    Component withFont(@Nullable NamespacedMappingKey font);

    boolean bold();

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withBold(boolean bold);

    boolean italic();

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withItalic(boolean italic);

    boolean underlined();

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withUnderlined(boolean underlined);

    boolean strikethrough();

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withStrikethrough(boolean strikethrough);

    boolean obfuscated();

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withObfuscated(boolean obfuscated);

    @Nullable
    String insertion();

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withObfuscated(@Nullable String insertion);

    @Nullable
    HoverEvent hoverEvent();

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withHoverEvent(@Nullable HoverEvent hoverEvent);

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withHoverEvent(@Nullable ItemContent itemContent);

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withHoverEvent(@Nullable EntityContent entityContent);

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withHoverEvent(@Nullable Component component);

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withHoverEvent(@Nullable ComponentLike component);

    @Nullable
    ClickEvent clickEvent();

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    Component withClickEvent(@Nullable ClickEvent clickEvent);

    @Override
    default Component asComponent() {
        return this;
    }

    @Override
    default Content asContent() {
        return this;
    }

    String toLegacy();

    String toPlainText();

    String toJson();

    interface Builder<B extends Builder<B, C>, C extends Component> extends ComponentLike {
        B color(Color color);

        B append(Component component);

        B append(Component... components);

        B append(Collection<Component> components);

        @LimitedVersionSupport(">= 1.16")
        B font(NamespacedMappingKey font);

        default B bold() {
            return bold(true);
        }

        B bold(boolean bold);

        default B italic() {
            return italic(true);
        }

        B italic(boolean italic);

        default B underlined() {
            return underlined(true);
        }

        B underlined(boolean underlined);

        default B strikethrough() {
            return strikethrough(true);
        }

        B strikethrough(boolean strikethrough);

        default B obfuscated() {
            return obfuscated(true);
        }

        B obfuscated(boolean obfuscated);

        B insertion(@Nullable String insertion);

        B hoverEvent(@Nullable HoverEvent event);

        B hoverEvent(@Nullable ItemContent itemContent);

        B hoverEvent(@Nullable EntityContent entityContent);

        B hoverEvent(@Nullable ComponentLike component);

        B hoverEvent(@Nullable Component component);

        B clickEvent(@Nullable ClickEvent event);

        C build();

        @Override
        default Component asComponent() {
            return build();
        }
    }
}
