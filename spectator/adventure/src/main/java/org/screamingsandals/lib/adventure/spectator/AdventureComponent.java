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
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.event.AdventureClickEvent;
import org.screamingsandals.lib.adventure.spectator.event.AdventureHoverEvent;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdventureComponent extends BasicWrapper<net.kyori.adventure.text.Component> implements Component {
    public AdventureComponent(net.kyori.adventure.text.Component wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public List<Component> children() {
        return wrappedObject.children()
                .stream()
                .map(AdventureBackend::wrapComponent)
                .collect(Collectors.toList());
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
    @Nullable
    public NamespacedMappingKey font() {
        var font = wrappedObject.style().font();
        if (font == null) {
            return null;
        }
        return NamespacedMappingKey.of(font.asString());
    }

    @Override
    public boolean bold() {
        return wrappedObject.style().hasDecoration(TextDecoration.BOLD);
    }

    @Override
    public boolean italic() {
        return wrappedObject.style().hasDecoration(TextDecoration.ITALIC);
    }

    @Override
    public boolean underlined() {
        return wrappedObject.style().hasDecoration(TextDecoration.UNDERLINED);
    }

    @Override
    public boolean strikethrough() {
        return wrappedObject.style().hasDecoration(TextDecoration.STRIKETHROUGH);
    }

    @Override
    public boolean obfuscated() {
        return wrappedObject.style().hasDecoration(TextDecoration.OBFUSCATED);
    }

    @Override
    @Nullable
    public String insertion() {
        return wrappedObject.style().insertion();
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
    @Nullable
    public ClickEvent clickEvent() {
        var clickEvent = wrappedObject.style().clickEvent();
        if (clickEvent == null) {
            return null;
        }
        return new AdventureClickEvent(clickEvent);
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
