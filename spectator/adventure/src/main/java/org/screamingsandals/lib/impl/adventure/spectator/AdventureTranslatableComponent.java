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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.TranslatableComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AdventureTranslatableComponent extends AdventureComponent implements TranslatableComponent {
    public AdventureTranslatableComponent(net.kyori.adventure.text.@NotNull TranslatableComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String translate() {
        return ((net.kyori.adventure.text.TranslatableComponent) wrappedObject).key();
    }

    @Override
    public @NotNull TranslatableComponent withTranslate(@NotNull String translate) {
        return (TranslatableComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.TranslatableComponent) wrappedObject).key(translate));
    }

    @Override
    public @NotNull List<Component> args() {
        return ((net.kyori.adventure.text.TranslatableComponent) wrappedObject).args()
                .stream()
                .map(AdventureBackend::wrapComponent)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull TranslatableComponent withArgs(@NotNull Component @NotNull... components) {
        return (TranslatableComponent) AdventureBackend.wrapComponent(
                ((net.kyori.adventure.text.TranslatableComponent) wrappedObject)
                        .args(Arrays.stream(components)
                                .map(component -> component.as(net.kyori.adventure.text.Component.class))
                                .collect(Collectors.toList())
                        )
        );
    }

    @Override
    public @NotNull TranslatableComponent withArgs(@NotNull Collection<Component> components) {
        return (TranslatableComponent) AdventureBackend.wrapComponent(
                ((net.kyori.adventure.text.TranslatableComponent) wrappedObject)
                        .args(components
                                .stream()
                                .map(component -> component.as(net.kyori.adventure.text.Component.class))
                                .collect(Collectors.toList())
                        )
        );
    }

    @Override
    public @Nullable String fallback() {
        try {
            return ((net.kyori.adventure.text.TranslatableComponent) wrappedObject).fallback();
        } catch (NoSuchMethodError ignored) {
        }
        return null;
    }

    @Override
    public @NotNull TranslatableComponent withFallback(@Nullable String fallback) {
        try {
            return (TranslatableComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.TranslatableComponent) wrappedObject).fallback(fallback));
        } catch (NoSuchMethodError ignored) {
        }
        return this;
    }

    @Override
    public TranslatableComponent.@NotNull Builder toBuilder() {
        return new AdventureTranslatableBuilder(((net.kyori.adventure.text.TranslatableComponent) wrappedObject).toBuilder());
    }

    public static class AdventureTranslatableBuilder extends AdventureBuilder<
            net.kyori.adventure.text.TranslatableComponent,
            TranslatableComponent.Builder,
            TranslatableComponent,
            net.kyori.adventure.text.TranslatableComponent.Builder
            > implements TranslatableComponent.Builder {

        public AdventureTranslatableBuilder(net.kyori.adventure.text.TranslatableComponent.Builder builder) {
            super(builder);
        }

        @Override
        public TranslatableComponent.@NotNull Builder translate(@NotNull String translate) {
            getBuilder().key(translate);
            return self();
        }

        @Override
        public TranslatableComponent.@NotNull Builder args(@NotNull Component @NotNull... components) {
            getBuilder().args(Arrays.stream(components).map(component -> component.as(net.kyori.adventure.text.Component.class)).collect(Collectors.toList()));
            return self();
        }

        @Override
        public TranslatableComponent.@NotNull Builder args(@NotNull Collection<Component> components) {
            getBuilder().args(components.stream().map(component -> component.as(net.kyori.adventure.text.Component.class)).collect(Collectors.toList()));
            return self();
        }

        @Override
        public TranslatableComponent.@NotNull Builder fallback(@Nullable String fallback) {
            try {
                getBuilder().fallback(fallback);
            } catch (NoSuchMethodError ignored) {
            }
            return self();
        }
    }
}
