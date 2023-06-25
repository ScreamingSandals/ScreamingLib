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

package org.screamingsandals.lib.impl.adventure.spectator;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.impl.adventure.spectator.event.AdventureClickEvent;
import org.screamingsandals.lib.impl.adventure.spectator.event.AdventureHoverEvent;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.TriState;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AdventureComponent extends BasicWrapper<net.kyori.adventure.text.Component> implements Component {
    public AdventureComponent(net.kyori.adventure.text.@NotNull Component wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Component> children() {
        return wrappedObject.children()
                .stream()
                .map(AdventureBackend::wrapComponent)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public @NotNull Component withChildren(@Nullable List<@NotNull Component> children) {
        return AdventureBackend.wrapComponent(wrappedObject.children(
                children == null ? List.of() : children.stream().map(component -> component.as(net.kyori.adventure.text.Component.class)
                ).collect(Collectors.toList())));
    }

    @Override
    public @NotNull Component withAppendix(@NotNull Component component) {
        return AdventureBackend.wrapComponent(wrappedObject.append(component.as(net.kyori.adventure.text.Component.class)));
    }

    @Override
    public @NotNull Component withAppendix(@NotNull ComponentLike component) {
        return withAppendix(component.asComponent());
    }

    @Override
    public @NotNull Component withAppendix(@NotNull Component @NotNull... components) {
        if (components.length == 0) {
            return this;
        }
        var returnC = wrappedObject;
        for (var child : components) {
            returnC = returnC.append(child.as(net.kyori.adventure.text.Component.class));
        }
        return AdventureBackend.wrapComponent(returnC);
    }

    @Override
    public @NotNull Component withAppendix(@NotNull ComponentLike @NotNull... components) {
        if (components.length == 0) {
            return this;
        }
        var returnC = wrappedObject;
        for (var child : components) {
            returnC = returnC.append(child.asComponent().as(net.kyori.adventure.text.Component.class));
        }
        return AdventureBackend.wrapComponent(returnC);
    }

    @Override
    public @NotNull Component withAppendix(@NotNull Collection<@NotNull Component> components) {
        if (components.isEmpty()) {
            return this;
        }
        var returnC = wrappedObject;
        for (var child : components) {
            returnC = returnC.append(child.as(net.kyori.adventure.text.Component.class));
        }
        return AdventureBackend.wrapComponent(returnC);
    }

    @Override
    public @Nullable Color color() {
        var color = wrappedObject.style().color();
        if (color == null) {
            return null;
        }
        return new AdventureColor(color);
    }

    @Override
    public @NotNull Component withColor(@Nullable Color color) {
        return AdventureBackend.wrapComponent(wrappedObject.color(color == null ? null : color.as(TextColor.class)));
    }

    @Override
    public @Nullable ResourceLocation font() {
        var font = wrappedObject.style().font();
        if (font == null) {
            return null;
        }
        return ResourceLocation.of(font.asString());
    }

    @SuppressWarnings("PatternValidation")
    @Override
    public @NotNull Component withFont(@Nullable ResourceLocation font) {
        return AdventureBackend.wrapComponent(wrappedObject.style(wrappedObject.style().font(font == null ? null : Key.key(font.toString()))));
    }

    @Override
    public @NotNull TriState bold() {
        return fromAdventure(wrappedObject.style().decoration(TextDecoration.BOLD));
    }

    @Override
    public @NotNull Component withBold(boolean bold) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.BOLD, bold));
    }

    @Override
    public @NotNull Component withBold(@NotNull TriState bold) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.BOLD, toAdventure(bold)));
    }

    @Override
    public @NotNull TriState italic() {
        return fromAdventure(wrappedObject.style().decoration(TextDecoration.ITALIC));
    }

    @Override
    public @NotNull Component withItalic(boolean italic) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.ITALIC, italic));
    }

    @Override
    public @NotNull Component withItalic(@NotNull TriState italic) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.ITALIC, toAdventure(italic)));
    }

    @Override
    public @NotNull TriState underlined() {
        return fromAdventure(wrappedObject.style().decoration(TextDecoration.UNDERLINED));
    }

    @Override
    public @NotNull Component withUnderlined(boolean underlined) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.UNDERLINED, underlined));
    }

    @Override
    public @NotNull Component withUnderlined(@NotNull TriState underlined) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.UNDERLINED, toAdventure(underlined)));
    }

    @Override
    public @NotNull TriState strikethrough() {
        return fromAdventure(wrappedObject.style().decoration(TextDecoration.STRIKETHROUGH));
    }

    @Override
    public @NotNull Component withStrikethrough(boolean strikethrough) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.STRIKETHROUGH, strikethrough));
    }

    @Override
    public @NotNull Component withStrikethrough(@NotNull TriState strikethrough) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.STRIKETHROUGH, toAdventure(strikethrough)));
    }

    @Override
    public @NotNull TriState obfuscated() {
        return fromAdventure(wrappedObject.style().decoration(TextDecoration.OBFUSCATED));
    }

    @Override
    public @NotNull Component withObfuscated(boolean obfuscated) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.OBFUSCATED, obfuscated));
    }

    @Override
    public @NotNull Component withObfuscated(@NotNull TriState obfuscated) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.OBFUSCATED, toAdventure(obfuscated)));
    }

    @Override
    public @Nullable String insertion() {
        return wrappedObject.style().insertion();
    }

    @Override
    public @NotNull Component withInsertion(@Nullable String insertion) {
        return AdventureBackend.wrapComponent(wrappedObject.insertion(insertion));
    }

    @Override
    public @Nullable HoverEvent hoverEvent() {
        var hoverEvent = wrappedObject.style().hoverEvent();
        if (hoverEvent == null) {
            return null;
        }
        return new AdventureHoverEvent(hoverEvent);
    }

    @Override
    public @NotNull Component withHoverEvent(@Nullable HoverEvent hoverEvent) {
        return AdventureBackend.wrapComponent(wrappedObject.hoverEvent(hoverEvent == null ? null : hoverEvent.as(net.kyori.adventure.text.event.HoverEvent.class)));
    }

    @Override
    public @NotNull Component withHoverEvent(@Nullable ItemContent itemContent) {
        return AdventureBackend.wrapComponent(wrappedObject.hoverEvent(itemContent == null ? null : net.kyori.adventure.text.event.HoverEvent.showItem(itemContent.as(net.kyori.adventure.text.event.HoverEvent.ShowItem.class))));
    }

    @Override
    public @NotNull Component withHoverEvent(@Nullable EntityContent entityContent) {
        return AdventureBackend.wrapComponent(wrappedObject.hoverEvent(entityContent == null ? null : net.kyori.adventure.text.event.HoverEvent.showEntity(entityContent.as(net.kyori.adventure.text.event.HoverEvent.ShowEntity.class))));
    }

    @Override
    public @NotNull Component withHoverEvent(@Nullable Component component) {
        return AdventureBackend.wrapComponent(wrappedObject.hoverEvent(component == null ? null : net.kyori.adventure.text.event.HoverEvent.showText(component.as(net.kyori.adventure.text.Component.class))));
    }

    @Override
    public @NotNull Component withHoverEvent(@Nullable ComponentLike component) {
        return withHoverEvent(component == null ? null : component.asComponent());
    }

    @Override
    public @Nullable ClickEvent clickEvent() {
        var clickEvent = wrappedObject.style().clickEvent();
        if (clickEvent == null) {
            return null;
        }
        return new AdventureClickEvent(clickEvent);
    }

    @Override
    public @NotNull Component withClickEvent(@Nullable ClickEvent clickEvent) {
        return AdventureBackend.wrapComponent(wrappedObject.clickEvent(clickEvent == null ? null : clickEvent.as(net.kyori.adventure.text.event.ClickEvent.class)));
    }

    @Override
    public @NotNull String toLegacy() {
        return AdventureBackend.getLegacyComponentSerializer().serialize(wrappedObject);
    }

    @Override
    public @NotNull String toPlainText() {
        return AdventureBackend.getPlainTextComponentSerializer().serialize(wrappedObject);
    }

    @Override
    public @NotNull String toJavaJson() {
        return AdventureBackend.getJsonComponentSerializer().serialize(wrappedObject);
    }

    @Override
    public boolean hasStyling() {
        return wrappedObject.hasStyling();
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AdventureBackend.getAdditionalComponentConverter().convert(this, type);
        }
    }

    @RequiredArgsConstructor
    @Data
    public abstract static class AdventureBuilder<A extends BuildableComponent<A, D>, B extends Builder<B, C>, C extends Component, D extends ComponentBuilder<A, D>> implements Builder<B, C> {
        private final @NotNull D builder;

        @SuppressWarnings("unchecked")
        protected @NotNull B self() {
            return (B) this;
        }

        @Override
        public @NotNull B color(@NotNull Color color) {
            builder.color(color.as(TextColor.class));
            return self();
        }

        @Override
        public @NotNull B append(@NotNull Component component) {
            builder.append(component.as(net.kyori.adventure.text.Component.class));
            return self();
        }

        @Override
        public @NotNull B append(@NotNull ComponentLike component) {
            builder.append(component.asComponent().as(net.kyori.adventure.text.Component.class));
            return self();
        }

        @Override
        public @NotNull B append(@NotNull Component @NotNull... components) {
            builder.append(Arrays.stream(components).map(component -> component.as(net.kyori.adventure.text.Component.class)).collect(Collectors.toList()));
            return self();
        }

        @Override
        public @NotNull B append(@NotNull ComponentLike @NotNull... components) {
            builder.append(Arrays.stream(components).map(component -> component.asComponent().as(net.kyori.adventure.text.Component.class)).collect(Collectors.toList()));
            return self();
        }

        @Override
        public @NotNull B append(@NotNull Collection<@NotNull Component> components) {
            builder.append(components.stream().map(component -> component.as(net.kyori.adventure.text.Component.class)).collect(Collectors.toList()));
            return self();
        }

        @SuppressWarnings("PatternValidation")
        @Override
        public @NotNull B font(@Nullable ResourceLocation font) {
            builder.font(font == null ? null : Key.key(font.toString()));
            return self();
        }

        @Override
        public @NotNull B bold(boolean bold) {
            builder.decoration(TextDecoration.BOLD, bold);
            return self();
        }

        @Override
        public @NotNull B bold(@NotNull TriState bold) {
            builder.decoration(TextDecoration.BOLD, toAdventure(bold));
            return self();
        }

        @Override
        public @NotNull B italic(boolean italic) {
            builder.decoration(TextDecoration.ITALIC, italic);
            return self();
        }

        @Override
        public @NotNull B italic(@NotNull TriState italic) {
            builder.decoration(TextDecoration.ITALIC, toAdventure(italic));
            return self();
        }

        @Override
        public @NotNull B underlined(boolean underlined) {
            builder.decoration(TextDecoration.UNDERLINED, underlined);
            return self();
        }

        @Override
        public @NotNull B underlined(@NotNull TriState underlined) {
            builder.decoration(TextDecoration.UNDERLINED, toAdventure(underlined));
            return self();
        }

        @Override
        public @NotNull B strikethrough(boolean strikethrough) {
            builder.decoration(TextDecoration.STRIKETHROUGH, strikethrough);
            return self();
        }

        @Override
        public @NotNull B strikethrough(@NotNull TriState strikethrough) {
            builder.decoration(TextDecoration.STRIKETHROUGH, toAdventure(strikethrough));
            return self();
        }

        @Override
        public @NotNull B obfuscated(boolean obfuscated) {
            builder.decoration(TextDecoration.OBFUSCATED, obfuscated);
            return self();
        }

        @Override
        public @NotNull B obfuscated(@NotNull TriState obfuscated) {
            builder.decoration(TextDecoration.OBFUSCATED, toAdventure(obfuscated));
            return self();
        }

        @Override
        public @NotNull B insertion(@Nullable String insertion) {
            builder.insertion(insertion);
            return self();
        }

        @Override
        public @NotNull B hoverEvent(@Nullable HoverEvent event) {
            builder.hoverEvent(event == null ? null : event.as(net.kyori.adventure.text.event.HoverEvent.class));
            return self();
        }

        @Override
        public @NotNull B hoverEvent(@Nullable ItemContent itemContent) {
            builder.hoverEvent(itemContent == null ? null : net.kyori.adventure.text.event.HoverEvent.showItem(itemContent.as(net.kyori.adventure.text.event.HoverEvent.ShowItem.class)));
            return self();
        }

        @Override
        public @NotNull B hoverEvent(@Nullable EntityContent entityContent) {
            builder.hoverEvent(entityContent == null ? null : net.kyori.adventure.text.event.HoverEvent.showEntity(entityContent.as(net.kyori.adventure.text.event.HoverEvent.ShowEntity.class)));
            return self();
        }

        @Override
        public @NotNull B hoverEvent(@Nullable ComponentLike component) {
            if (component != null) {
                hoverEvent(component.asComponent());
            }
            return self();
        }

        @Override
        public @NotNull B hoverEvent(@Nullable Component component) {
            builder.hoverEvent(component == null ? null : net.kyori.adventure.text.event.HoverEvent.showText(component.as(net.kyori.adventure.text.Component.class)));
            return self();
        }

        @Override
        public @NotNull B clickEvent(@Nullable ClickEvent event) {
            builder.clickEvent(event == null ? null : event.as(net.kyori.adventure.text.event.ClickEvent.class));
            return self();
        }

        @Override
        public boolean hasStyling() {
            var result = Reflect.fastInvoke(builder, "hasStyle");
            return result instanceof Boolean && (Boolean) result; // Sorry Adventure, I had to
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull C build() {
            return (C) AdventureBackend.wrapComponent(builder.build());
        }
    }

    private static @NotNull TriState fromAdventure(TextDecoration.@NotNull State state) {
        switch (state) {
            case TRUE:
                return TriState.TRUE;
            case FALSE:
                return TriState.FALSE;
            default:
                return TriState.INITIAL;
        }
    }

    private static TextDecoration.@NotNull State toAdventure(@NotNull TriState state) {
        switch (state) {
            case TRUE:
                return TextDecoration.State.TRUE;
            case FALSE:
                return TextDecoration.State.FALSE;
            default:
                return TextDecoration.State.NOT_SET;
        }
    }
}
