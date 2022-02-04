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

package org.screamingsandals.lib.adventure.spectator;

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
import org.screamingsandals.lib.adventure.spectator.event.AdventureClickEvent;
import org.screamingsandals.lib.adventure.spectator.event.AdventureHoverEvent;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AdventureComponent extends BasicWrapper<net.kyori.adventure.text.Component> implements Component {
    public AdventureComponent(net.kyori.adventure.text.Component wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @Unmodifiable
    public List<Component> children() {
        return wrappedObject.children()
                .stream()
                .map(AdventureBackend::wrapComponent)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    @NotNull
    public Component withChildren(@Nullable List<Component> children) {
        return AdventureBackend.wrapComponent(wrappedObject.children(
                children == null ? List.of() : children.stream().map(component -> component.as(net.kyori.adventure.text.Component.class)
                ).collect(Collectors.toList())));
    }

    @Override
    @NotNull
    public Component withAppendix(Component component) {
        return AdventureBackend.wrapComponent(wrappedObject.append(component.as(net.kyori.adventure.text.Component.class)));
    }

    @Override
    @NotNull
    public Component withAppendix(Component... components) {
        if (components == null || components.length == 0) {
            return this;
        }
        var returnC = wrappedObject;
        for (var child : components) {
            returnC = returnC.append(child.as(net.kyori.adventure.text.Component.class));
        }
        return AdventureBackend.wrapComponent(returnC);
    }

    @Override
    @NotNull
    public Component withAppendix(Collection<Component> components) {
        if (components == null || components.isEmpty()) {
            return this;
        }
        var returnC = wrappedObject;
        for (var child : components) {
            returnC = returnC.append(child.as(net.kyori.adventure.text.Component.class));
        }
        return AdventureBackend.wrapComponent(returnC);
    }

    @Override
    @Nullable
    public Color color() {
        var color = wrappedObject.style().color();
        if (color == null) {
            return null;
        }
        return new AdventureColor(color);
    }

    @Override
    @NotNull
    public Component withColor(@Nullable Color color) {
        return AdventureBackend.wrapComponent(wrappedObject.color(color == null ? null : color.as(TextColor.class)));
    }

    @Override
    @Nullable
    public NamespacedMappingKey font() {
        var font = wrappedObject.style().font();
        if (font == null) {
            return null;
        }
        return NamespacedMappingKey.of(font.asString());
    }

    @SuppressWarnings("PatternValidation")
    @Override
    @NotNull
    public Component withFont(@Nullable NamespacedMappingKey font) {
        return AdventureBackend.wrapComponent(wrappedObject.style(wrappedObject.style().font(font == null ? null : Key.key(font.toString()))));
    }

    @Override
    public boolean bold() {
        return wrappedObject.style().hasDecoration(TextDecoration.BOLD);
    }

    @Override
    @NotNull
    public Component withBold(boolean bold) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.BOLD, bold));
    }

    @Override
    public boolean italic() {
        return wrappedObject.style().hasDecoration(TextDecoration.ITALIC);
    }

    @Override
    @NotNull
    public Component withItalic(boolean italic) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.ITALIC, italic));
    }

    @Override
    public boolean underlined() {
        return wrappedObject.style().hasDecoration(TextDecoration.UNDERLINED);
    }

    @Override
    @NotNull
    public Component withUnderlined(boolean underlined) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.UNDERLINED, underlined));
    }

    @Override
    public boolean strikethrough() {
        return wrappedObject.style().hasDecoration(TextDecoration.STRIKETHROUGH);
    }

    @Override
    @NotNull
    public Component withStrikethrough(boolean strikethrough) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.STRIKETHROUGH, strikethrough));
    }

    @Override
    public boolean obfuscated() {
        return wrappedObject.style().hasDecoration(TextDecoration.OBFUSCATED);
    }

    @Override
    @NotNull
    public Component withObfuscated(boolean obfuscated) {
        return AdventureBackend.wrapComponent(wrappedObject.decoration(TextDecoration.OBFUSCATED, obfuscated));
    }

    @Override
    @Nullable
    public String insertion() {
        return wrappedObject.style().insertion();
    }

    @Override
    @NotNull
    public Component withObfuscated(@Nullable String insertion) {
        return AdventureBackend.wrapComponent(wrappedObject.insertion(insertion));
    }

    @Override
    @Nullable
    public HoverEvent hoverEvent() {
        var hoverEvent = wrappedObject.style().hoverEvent();
        if (hoverEvent == null) {
            return null;
        }
        return new AdventureHoverEvent(hoverEvent);
    }

    @Override
    @NotNull
    public Component withHoverEvent(@Nullable HoverEvent hoverEvent) {
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
    @Nullable
    public ClickEvent clickEvent() {
        var clickEvent = wrappedObject.style().clickEvent();
        if (clickEvent == null) {
            return null;
        }
        return new AdventureClickEvent(clickEvent);
    }

    @Override
    @NotNull
    public Component withClickEvent(@Nullable ClickEvent clickEvent) {
        return AdventureBackend.wrapComponent(wrappedObject.clickEvent(clickEvent == null ? null : clickEvent.as(net.kyori.adventure.text.event.ClickEvent.class)));
    }

    @Override
    public String toLegacy() {
        return AdventureBackend.getLegacyComponentSerializer().serialize(wrappedObject);
    }

    @Override
    public String toPlainText() {
        return AdventureBackend.getPlainTextComponentSerializer().serialize(wrappedObject);
    }

    @Override
    public String toJson() {
        return AdventureBackend.getGsonComponentSerializer().serialize(wrappedObject);
    }

    @Override
    public <T> T as(Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AdventureBackend.getAdditionalComponentConverter().convert(this, type);
        }
    }

    @RequiredArgsConstructor
    @Data
    public static abstract class AdventureBuilder<A extends BuildableComponent<A, D>, B extends Builder<B, C>, C extends Component, D extends ComponentBuilder<A, D>> implements Builder<B, C> {
        private final D builder;

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        @Override
        public B color(Color color) {
            builder.color(color.as(TextColor.class));
            return self();
        }

        @Override
        public B append(Component component) {
            builder.append(component.as(net.kyori.adventure.text.Component.class));
            return self();
        }

        @Override
        public B append(Component... components) {
            builder.append(Arrays.stream(components).map(component -> component.as(net.kyori.adventure.text.Component.class)).collect(Collectors.toList()));
            return self();
        }

        @Override
        public B append(Collection<Component> components) {
            builder.append(components.stream().map(component -> component.as(net.kyori.adventure.text.Component.class)).collect(Collectors.toList()));
            return self();
        }

        @SuppressWarnings("PatternValidation")
        @Override
        public B font(NamespacedMappingKey font) {
            builder.font(font == null ? null : Key.key(font.toString()));
            return self();
        }

        @Override
        public B bold(boolean bold) {
            builder.decoration(TextDecoration.BOLD, bold);
            return self();
        }

        @Override
        public B italic(boolean italic) {
            builder.decoration(TextDecoration.ITALIC, italic);
            return self();
        }

        @Override
        public B underlined(boolean underlined) {
            builder.decoration(TextDecoration.UNDERLINED, underlined);
            return self();
        }

        @Override
        public B strikethrough(boolean strikethrough) {
            builder.decoration(TextDecoration.STRIKETHROUGH, strikethrough);
            return null;
        }

        @Override
        public B obfuscated(boolean obfuscated) {
            builder.decoration(TextDecoration.OBFUSCATED, obfuscated);
            return self();
        }

        @Override
        public B insertion(@Nullable String insertion) {
            builder.insertion(insertion);
            return self();
        }

        @Override
        public B hoverEvent(@Nullable HoverEvent event) {
            builder.hoverEvent(event == null ? null : event.as(net.kyori.adventure.text.event.HoverEvent.class));
            return self();
        }

        @Override
        public B hoverEvent(@Nullable ItemContent itemContent) {
            builder.hoverEvent(itemContent == null ? null : net.kyori.adventure.text.event.HoverEvent.showItem(itemContent.as(net.kyori.adventure.text.event.HoverEvent.ShowItem.class)));
            return self();
        }

        @Override
        public B hoverEvent(@Nullable EntityContent entityContent) {
            builder.hoverEvent(entityContent == null ? null : net.kyori.adventure.text.event.HoverEvent.showEntity(entityContent.as(net.kyori.adventure.text.event.HoverEvent.ShowEntity.class)));
            return self();
        }

        @Override
        public B hoverEvent(@Nullable ComponentLike component) {
            if (component != null) {
                hoverEvent(component.asComponent());
            }
            return self();
        }

        @Override
        public B hoverEvent(@Nullable Component component) {
            builder.hoverEvent(component == null ? null : net.kyori.adventure.text.event.HoverEvent.showText(component.as(net.kyori.adventure.text.Component.class)));
            return self();
        }

        @Override
        public B clickEvent(@Nullable ClickEvent event) {
            builder.clickEvent(event == null ? null : event.as(net.kyori.adventure.text.event.ClickEvent.class));
            return self();
        }

        @SuppressWarnings("unchecked")
        @Override
        public C build() {
            return (C) AdventureBackend.wrapComponent(builder.build());
        }
    }
}
