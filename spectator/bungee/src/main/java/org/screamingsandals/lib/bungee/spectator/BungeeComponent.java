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

package org.screamingsandals.lib.bungee.spectator;

import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.bungee.spectator.event.BungeeClickEvent;
import org.screamingsandals.lib.bungee.spectator.event.BungeeHoverEvent;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.TriState;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BungeeComponent extends BasicWrapper<BaseComponent> implements Component {
    protected BungeeComponent(@NotNull BaseComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Component> children() {
        var extra = wrappedObject.getExtra();
        if (extra == null || extra.isEmpty()) {
            return List.of();
        }
        return extra.stream()
                .map(AbstractBungeeBackend::wrapComponent)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public @NotNull Component withChildren(@Nullable List<@NotNull Component> children) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setExtra(children == null ? null : children.stream().map(component -> component.as(BaseComponent.class).duplicate()).collect(Collectors.toList()));
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull Component withAppendix(@NotNull Component component) {
        var duplicate = wrappedObject.duplicate();
        duplicate.addExtra(component.as(BaseComponent.class).duplicate());
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull Component withAppendix(@NotNull ComponentLike component) {
        return withAppendix(component.asComponent());
    }

    @Override
    public @NotNull Component withAppendix(@NotNull Component @NotNull... components) {
        var duplicate = wrappedObject.duplicate();
        for (var component : components) {
            duplicate.addExtra(component.as(BaseComponent.class).duplicate());
        }
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull Component withAppendix(@NotNull ComponentLike @NotNull... components) {
        var duplicate = wrappedObject.duplicate();
        for (var component : components) {
            duplicate.addExtra(component.asComponent().as(BaseComponent.class).duplicate());
        }
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull Component withAppendix(@NotNull Collection<@NotNull Component> components) {
        var duplicate = wrappedObject.duplicate();
        for (var component : components) {
            duplicate.addExtra(component.as(BaseComponent.class).duplicate());
        }
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @Nullable Color color() {
        var color = wrappedObject.getColorRaw();
        if (color == null) {
            return null;
        }
        return new BungeeColor(color);
    }

    @Override
    public @NotNull Component withColor(@Nullable Color color) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setColor(color == null ? null : color.as(ChatColor.class));
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @Nullable NamespacedMappingKey font() {
        try {
            return NamespacedMappingKey.of(wrappedObject.getFontRaw());
        } catch (Throwable ignored) {
            // old version basically; or invalid font, thanks bungee for not checking the input
            return null;
        }
    }

    @Override
    public @NotNull Component withFont(@Nullable NamespacedMappingKey font) {
        try {
            var duplicate = wrappedObject.duplicate();
            duplicate.setFont(font == null ? null : font.asString());
            return AbstractBungeeBackend.wrapComponent(duplicate);
        } catch (Throwable ignored) {
            // old version basically; or invalid font, thanks bungee for not checking the input
            return this;
        }
    }

    @Override
    public @NotNull TriState bold() {
        return TriState.fromBoolean(wrappedObject.isBoldRaw());
    }

    @Override
    public @NotNull Component withBold(boolean bold) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setBold(bold);
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull Component withBold(@NotNull TriState bold) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setBold(bold.toBoxedBoolean());
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull TriState italic() {
        return TriState.fromBoolean(Boolean.TRUE == wrappedObject.isItalicRaw());
    }

    @Override
    public @NotNull Component withItalic(boolean italic) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setItalic(italic);
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull Component withItalic(@NotNull TriState italic) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setItalic(italic.toBoxedBoolean());
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull TriState underlined() {
        return TriState.fromBoolean(wrappedObject.isUnderlinedRaw());
    }

    @Override
    public @NotNull Component withUnderlined(boolean underlined) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setUnderlined(underlined);
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull Component withUnderlined(@NotNull TriState underlined) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setUnderlined(underlined.toBoxedBoolean());
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull TriState strikethrough() {
        return TriState.fromBoolean(wrappedObject.isStrikethroughRaw());
    }

    @Override
    public @NotNull Component withStrikethrough(boolean strikethrough) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setStrikethrough(strikethrough);
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull Component withStrikethrough(@NotNull TriState strikethrough) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setStrikethrough(strikethrough.toBoxedBoolean());
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull TriState obfuscated() {
        return TriState.fromBoolean(wrappedObject.isObfuscatedRaw());
    }

    @Override
    public @NotNull Component withObfuscated(boolean obfuscated) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setObfuscated(obfuscated);
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull Component withObfuscated(@NotNull TriState obfuscated) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setObfuscated(obfuscated.toBoxedBoolean());
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @Nullable String insertion() {
        return wrappedObject.getInsertion();
    }

    @Override
    public @NotNull Component withInsertion(@Nullable String insertion) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setInsertion(insertion);
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @Nullable HoverEvent hoverEvent() {
        var hover = wrappedObject.getHoverEvent();
        if (hover == null) {
            return null;
        }
        return new BungeeHoverEvent(hover);
    }

    @Override
    public @NotNull Component withHoverEvent(@Nullable HoverEvent hoverEvent) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setHoverEvent(hoverEvent == null ? null : hoverEvent.as(net.md_5.bungee.api.chat.HoverEvent.class));
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull Component withHoverEvent(@Nullable ItemContent itemContent) {
        return withHoverEvent(itemContent == null ? null : HoverEvent.builder().action(HoverEvent.Action.SHOW_ITEM).content(itemContent).build());
    }

    @Override
    public @NotNull Component withHoverEvent(@Nullable EntityContent entityContent) {
        return withHoverEvent(entityContent == null ? null : HoverEvent.builder().action(HoverEvent.Action.SHOW_ENTITY).content(entityContent).build());
    }

    @Override
    public @NotNull Component withHoverEvent(@Nullable Component component) {
        return withHoverEvent(component == null ? null : HoverEvent.builder().action(HoverEvent.Action.SHOW_TEXT).content(component).build());
    }

    @Override
    public @NotNull Component withHoverEvent(@Nullable ComponentLike component) {
        return withHoverEvent(component == null ? null : HoverEvent.builder().action(HoverEvent.Action.SHOW_TEXT).content(component).build());
    }

    @Override
    public @Nullable ClickEvent clickEvent() {
        var click = wrappedObject.getClickEvent();
        if (click == null) {
            return null;
        }
        return new BungeeClickEvent(click);
    }

    @Override
    public @NotNull Component withClickEvent(@Nullable ClickEvent clickEvent) {
        var duplicate = wrappedObject.duplicate();
        duplicate.setClickEvent(clickEvent == null ? null : clickEvent.as(net.md_5.bungee.api.chat.ClickEvent.class));
        return AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull String toLegacy() {
        return wrappedObject.toLegacyText();
    }

    @Override
    public @NotNull String toPlainText() {
        return wrappedObject.toPlainText();
    }

    @Override
    public @NotNull String toJavaJson() {
        return ComponentSerializer.toString(wrappedObject);
    }

    @Override
    public boolean hasStyling() {
        return wrappedObject.hasFormatting();
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AbstractBungeeBackend.getAdditionalComponentConverter().convert(this, type);
        }
    }

    @Data
    public static class BungeeBuilder<C extends Component, B extends Component.Builder<B, C>, A extends BaseComponent> implements Component.Builder<B, C> {
        protected final @NotNull A component;

        @Override
        public @NotNull B color(@NotNull Color color) {
            component.setColor(color.as(ChatColor.class));
            return self();
        }

        @Override
        public @NotNull B append(@NotNull Component component) {
            this.component.addExtra(component.as(BaseComponent.class).duplicate());
            return self();
        }

        @Override
        public @NotNull B append(@NotNull ComponentLike component) {
            this.component.addExtra(component.asComponent().as(BaseComponent.class).duplicate());
            return self();
        }

        @Override
        public @NotNull B append(@NotNull Component @NotNull... components) {
            for (var component : components) {
                append(component);
            }
            return self();
        }

        @Override
        public @NotNull B append(@NotNull ComponentLike @NotNull... components) {
            for (var component : components) {
                append(component);
            }
            return self();
        }

        @Override
        public @NotNull B append(@NotNull Collection<@NotNull Component> components) {
            for (var component : components) {
                append(component);
            }
            return self();
        }

        @Override
        public @NotNull B font(@Nullable NamespacedMappingKey font) {
            try {
                component.setFont(font == null ? null : font.asString());
            } catch (Throwable ignored) {
                // old version basically
            }
            return self();
        }

        @Override
        public @NotNull B bold(boolean bold) {
            component.setBold(bold);
            return self();
        }

        @Override
        public @NotNull B bold(@NotNull TriState bold) {
            component.setBold(bold.toBoxedBoolean());
            return self();
        }

        @Override
        public @NotNull B italic(boolean italic) {
            component.setItalic(italic);
            return self();
        }

        @Override
        public @NotNull B italic(@NotNull TriState italic) {
            component.setItalic(italic.toBoxedBoolean());
            return self();
        }

        @Override
        public @NotNull B underlined(boolean underlined) {
            component.setUnderlined(underlined);
            return self();
        }

        @Override
        public @NotNull B underlined(@NotNull TriState underlined) {
            component.setUnderlined(underlined.toBoxedBoolean());
            return self();
        }

        @Override
        public @NotNull B strikethrough(boolean strikethrough) {
            component.setStrikethrough(strikethrough);
            return self();
        }

        @Override
        public @NotNull B strikethrough(@NotNull TriState strikethrough) {
            component.setStrikethrough(strikethrough.toBoxedBoolean());
            return self();
        }

        @Override
        public @NotNull B obfuscated(@NotNull TriState obfuscated) {
            component.setObfuscated(obfuscated.toBoxedBoolean());
            return self();
        }

        @Override
        public @NotNull B obfuscated(boolean obfuscated) {
            component.setObfuscated(obfuscated);
            return self();
        }

        @Override
        public @NotNull B insertion(@Nullable String insertion) {
            component.setInsertion(insertion);
            return self();
        }

        @Override
        public @NotNull B hoverEvent(@Nullable HoverEvent event) {
            component.setHoverEvent(event == null ? null : event.as(net.md_5.bungee.api.chat.HoverEvent.class));
            return self();
        }

        @Override
        public @NotNull B hoverEvent(@Nullable ItemContent itemContent) {
            hoverEvent(itemContent == null ? null : HoverEvent.builder().action(HoverEvent.Action.SHOW_ITEM).content(itemContent).build());
            return self();
        }

        @Override
        public @NotNull B hoverEvent(@Nullable EntityContent entityContent) {
            hoverEvent(entityContent == null ? null : HoverEvent.builder().action(HoverEvent.Action.SHOW_ENTITY).content(entityContent).build());
            return self();
        }

        @Override
        public @NotNull B hoverEvent(@Nullable ComponentLike component) {
            hoverEvent(component == null ? null : HoverEvent.builder().action(HoverEvent.Action.SHOW_TEXT).content(component).build());
            return self();
        }

        @Override
        public @NotNull B hoverEvent(@Nullable Component component) {
            hoverEvent(component == null ? null : HoverEvent.builder().action(HoverEvent.Action.SHOW_TEXT).content(component).build());
            return self();
        }

        @Override
        public @NotNull B clickEvent(@Nullable ClickEvent event) {
            component.setClickEvent(event == null ? null : event.as(net.md_5.bungee.api.chat.ClickEvent.class));
            return self();
        }

        @Override
        public boolean hasStyling() {
            return component.hasFormatting();
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull C build() {
            return (C) AbstractBungeeBackend.wrapComponent(component.duplicate());
        }

        @SuppressWarnings("unchecked")
        protected @NotNull B self() {
            return (B) this;
        }
    }
}
