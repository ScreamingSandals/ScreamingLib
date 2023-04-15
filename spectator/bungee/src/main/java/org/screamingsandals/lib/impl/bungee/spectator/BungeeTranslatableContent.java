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

package org.screamingsandals.lib.impl.bungee.spectator;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TranslatableComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BungeeTranslatableContent extends BungeeComponent implements TranslatableComponent {
    protected BungeeTranslatableContent(net.md_5.bungee.api.chat.@NotNull TranslatableComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String translate() {
        return ((net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject).getTranslate();
    }

    @Override
    public @NotNull TranslatableComponent withTranslate(@NotNull String translate) {
        var duplicate = (net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject.duplicate();
        duplicate.setTranslate(translate);
        return (TranslatableComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull List<Component> args() {
        var with = ((net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject).getWith();
        if (with == null || with.isEmpty()) {
            return List.of();
        }
        return with.stream()
                .map(AbstractBungeeBackend::wrapComponent)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull TranslatableComponent withArgs(@NotNull Component @NotNull... components) {
        var duplicate = (net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject.duplicate();
        duplicate.setWith(Arrays.stream(components).map(component1 -> component1.as(BaseComponent.class).duplicate()).collect(Collectors.toUnmodifiableList()));
        return (TranslatableComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @NotNull TranslatableComponent withArgs(@NotNull Collection<Component> components) {
        var duplicate = (net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject.duplicate();
        duplicate.setWith(components.stream().map(component1 -> component1.as(BaseComponent.class).duplicate()).collect(Collectors.toUnmodifiableList()));
        return (TranslatableComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @Nullable String fallback() {
        try {
            return ((net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject).getFallback();
        } catch (NoSuchMethodError ignored) {
        }
        return null;
    }

    @Override
    public @NotNull TranslatableComponent withFallback(@Nullable String fallback) {
        try {
            var duplicate = (net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject.duplicate();
            duplicate.setFallback(fallback);
            return (TranslatableComponent) AbstractBungeeBackend.wrapComponent(duplicate);
        } catch (NoSuchMethodError ignored) {
        }
        return this;
    }

    @Override
    public TranslatableComponent.@NotNull Builder toBuilder() {
        var duplicate = (net.md_5.bungee.api.chat.TranslatableComponent) wrappedObject.duplicate();
        return new BungeeTranslatableBuilder(duplicate);
    }

    public static class BungeeTranslatableBuilder extends BungeeBuilder<
            TranslatableComponent,
            TranslatableComponent.Builder,
            net.md_5.bungee.api.chat.TranslatableComponent
            > implements TranslatableComponent.Builder {

        public BungeeTranslatableBuilder(net.md_5.bungee.api.chat.TranslatableComponent component) {
            super(component);
        }

        @Override
        public TranslatableComponent.@NotNull Builder translate(@NotNull String translate) {
            component.setTranslate(translate);
            return self();
        }

        @Override
        public TranslatableComponent.@NotNull Builder args(@NotNull Component @NotNull... components) {
            component.setWith(Arrays.stream(components).map(component1 -> component1.as(BaseComponent.class).duplicate()).collect(Collectors.toUnmodifiableList()));
            return self();
        }

        @Override
        public TranslatableComponent.@NotNull Builder args(@NotNull Collection<Component> components) {
            component.setWith(components.stream().map(component1 -> component1.as(BaseComponent.class).duplicate()).collect(Collectors.toUnmodifiableList()));
            return self();
        }

        @Override
        public TranslatableComponent.@NotNull Builder fallback(@Nullable String fallback) {
            try {
                component.setFallback(fallback);
            } catch (NoSuchMethodError ignored) {
            }
            return self();
        }
    }
}
