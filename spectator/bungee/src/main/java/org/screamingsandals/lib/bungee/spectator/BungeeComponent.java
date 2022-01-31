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

package org.screamingsandals.lib.bungee.spectator;

import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.spectator.event.BungeeClickEvent;
import org.screamingsandals.lib.bungee.spectator.event.BungeeHoverEvent;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BungeeComponent extends BasicWrapper<BaseComponent> implements Component {
    protected BungeeComponent(BaseComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public List<Component> children() {
        var extra = wrappedObject.getExtra();
        if (extra == null || extra.isEmpty()) {
            return List.of();
        }
        return extra.stream()
                .map(AbstractBungeeBackend::wrapComponent)
                .collect(Collectors.toList());
    }

    @Override
    @Nullable
    public Color color() {
        // TODO: should we use getColor() or getColorRaw()? idk, I just want to get the same value I'd get with adventure
        var color = wrappedObject.getColorRaw();
        if (color == null) {
            return null;
        }
        return new BungeeColor(color);
    }

    @Override
    @Nullable
    public NamespacedMappingKey font() {
        try {
            // TODO: should we use getFont() or getFontRaw()? idk, I just want to get the same value I'd get with adventure
            return NamespacedMappingKey.of(wrappedObject.getFontRaw());
        } catch (Throwable ignored) {
            // old version basically; or invalid font, thanks bungee for not checking the input
            return null;
        }
    }

    @Override
    public boolean bold() {
        // TODO: should we use isBold() or isBoldRaw()? idk, I just want to get the same value I'd get with adventure
        return Boolean.TRUE == wrappedObject.isBoldRaw();
    }

    @Override
    public boolean italic() {
        // TODO: should we use isItalic() or isItalicRaw()? idk, I just want to get the same value I'd get with adventure
        return Boolean.TRUE == wrappedObject.isItalicRaw();
    }

    @Override
    public boolean underlined() {
        // TODO: should we use isUnderlined() or isUnderlinedRaw()? idk, I just want to get the same value I'd get with adventure
        return Boolean.TRUE == wrappedObject.isUnderlinedRaw();
    }

    @Override
    public boolean strikethrough() {
        // TODO: should we use isStrikethrough() or isStrikethroughRaw()? idk, I just want to get the same value I'd get with adventure
        return Boolean.TRUE == wrappedObject.isStrikethroughRaw();
    }

    @Override
    public boolean obfuscated() {
        // TODO: should we use isObfuscated() or isObfuscatedRaw()? idk, I just want to get the same value I'd get with adventure
        return Boolean.TRUE == wrappedObject.isObfuscatedRaw();
    }

    @Override
    @Nullable
    public String insertion() {
        return wrappedObject.getInsertion();
    }

    @Override
    @Nullable
    public HoverEvent hoverEvent() {
        var hover = wrappedObject.getHoverEvent();
        if (hover == null) {
            return null;
        }
        return new BungeeHoverEvent(hover);
    }

    @Override
    @Nullable
    public ClickEvent clickEvent() {
        var click = wrappedObject.getClickEvent();
        if (click == null) {
            return null;
        }
        return new BungeeClickEvent(click);
    }

    @Override
    public <T> T as(Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AbstractBungeeBackend.getAdditionalComponentConverter().convert(this, type);
        }
    }

    @Data
    public static class BungeeBuilder<C extends Component, B extends Component.Builder<B, C>, A extends BaseComponent> implements Component.Builder<B, C> {
        protected final A component;

        @Override
        public B color(Color color) {
            component.setColor(color.as(ChatColor.class));
            return self();
        }

        @Override
        public B append(Component component) {
            this.component.addExtra(component.as(BaseComponent.class));
            return self();
        }

        @Override
        public B append(Component... components) {
            for (var component : components) {
                append(component);
            }
            return self();
        }

        @Override
        public B append(Collection<Component> components) {
            for (var component : components) {
                append(component);
            }
            return self();
        }

        @Override
        public B font(NamespacedMappingKey font) {
            try {
                component.setFont(font.asString());
            } catch (Throwable ignored) {
                // old version basically
            }
            return self();
        }

        @Override
        public B bold(boolean bold) {
            component.setBold(bold);
            return self();
        }

        @Override
        public B italic(boolean italic) {
            component.setItalic(italic);
            return self();
        }

        @Override
        public B underlined(boolean underlined) {
            component.setUnderlined(underlined);
            return self();
        }

        @Override
        public B strikethrough(boolean strikethrough) {
            component.setStrikethrough(strikethrough);
            return self();
        }

        @Override
        public B obfuscated(boolean obfuscated) {
            component.setObfuscated(obfuscated);
            return self();
        }

        @Override
        public B insertion(@Nullable String insertion) {
            component.setInsertion(insertion);
            return self();
        }

        @Override
        public B hoverEvent(@Nullable HoverEvent event) {
            component.setHoverEvent(event == null ? null : event.as(net.md_5.bungee.api.chat.HoverEvent.class));
            return self();
        }

        @Override
        public B clickEvent(@Nullable ClickEvent event) {
            component.setClickEvent(event == null ? null : event.as(net.md_5.bungee.api.chat.ClickEvent.class));
            return self();
        }

        @SuppressWarnings("unchecked")
        @Override
        public C build() {
            return (C) AbstractBungeeBackend.wrapComponent(component.duplicate());
        }

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }
    }
}
