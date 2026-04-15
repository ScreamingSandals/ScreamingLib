/*
 * Copyright 2026 ScreamingSandals
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

import net.kyori.adventure.text.TranslationArgumentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.screamingsandals.lib.impl.adventure.spectator.compat.v4.ComponentBuilderCompat;
import org.screamingsandals.lib.impl.adventure.spectator.compat.v4.TranslatableComponentCompat;
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
        if (AdventureFeature.TRANSLATABLE_ARGUMENTS_METHOD.isSupported()) {
            return ((net.kyori.adventure.text.TranslatableComponent) wrappedObject).arguments()
                    .stream()
                    .map(TranslationArgumentLike::asComponent)
                    .map(AdventureBackend::wrapComponent)
                    .collect(Collectors.toList());
        } else {
            return TranslatableComponentCompat.arguments((net.kyori.adventure.text.TranslatableComponent) wrappedObject)
                    .stream()
                    .map(AdventureBackend::wrapComponent)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public @NotNull TranslatableComponent withArgs(@NotNull Component @NotNull... components) {
        var input = Arrays.stream(components)
                .map(component -> component.as(net.kyori.adventure.text.Component.class))
                .collect(Collectors.toList());

        if (AdventureFeature.TRANSLATABLE_ARGUMENTS_METHOD.isSupported()) {
            return (TranslatableComponent) AdventureBackend.wrapComponent(
                    ((net.kyori.adventure.text.TranslatableComponent) wrappedObject).arguments(input)
            );
        } else {
            return (TranslatableComponent) AdventureBackend.wrapComponent(
                    TranslatableComponentCompat.arguments(((net.kyori.adventure.text.TranslatableComponent) wrappedObject), input)
            );
        }
    }

    @Override
    public @NotNull TranslatableComponent withArgs(@NotNull Collection<Component> components) {
        var input = components
                .stream()
                .map(component -> component.as(net.kyori.adventure.text.Component.class))
                .collect(Collectors.toList());

        if (AdventureFeature.TRANSLATABLE_ARGUMENTS_METHOD.isSupported()) {
            return (TranslatableComponent) AdventureBackend.wrapComponent(
                    ((net.kyori.adventure.text.TranslatableComponent) wrappedObject).arguments(input)
            );
        } else {
            return (TranslatableComponent) AdventureBackend.wrapComponent(
                    TranslatableComponentCompat.arguments((net.kyori.adventure.text.TranslatableComponent) wrappedObject, input)
            );
        }
    }

    @Override
    public @Nullable String fallback() {
        if (AdventureFeature.TRANSLATABLE_FALLBACK.isSupported()) {
            return ((net.kyori.adventure.text.TranslatableComponent) wrappedObject).fallback();
        }
        return null;
    }

    @Override
    public @NotNull TranslatableComponent withFallback(@Nullable String fallback) {
        if (AdventureFeature.TRANSLATABLE_FALLBACK.isSupported()) {
            return (TranslatableComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.TranslatableComponent) wrappedObject).fallback(fallback));
        }
        return this;
    }

    @Override
    public TranslatableComponent.@NotNull Builder toBuilder() {
        if (AdventureFeature.BUILDABLE_COMPONENT_REMOVAL.isSupported()) {
            return new AdventureTranslatableBuilder(((net.kyori.adventure.text.TranslatableComponent) wrappedObject).toBuilder());
        } else {
            return new AdventureTranslatableBuilder((net.kyori.adventure.text.TranslatableComponent.Builder) ComponentBuilderCompat.toBuilder(wrappedObject));
        }
    }

    public static class AdventureTranslatableBuilder extends AdventureBuilder<
            net.kyori.adventure.text.TranslatableComponent,
            TranslatableComponent.Builder,
            TranslatableComponent,
            net.kyori.adventure.text.TranslatableComponent.Builder
            > implements TranslatableComponent.Builder {

        public AdventureTranslatableBuilder(net.kyori.adventure.text.TranslatableComponent.@NonNull Builder builder) {
            super(builder);
        }

        @Override
        public TranslatableComponent.@NotNull Builder translate(@NotNull String translate) {
            getBuilder().key(translate);
            return self();
        }

        @Override
        public TranslatableComponent.@NotNull Builder args(@NotNull Component @NotNull... components) {
            var input = Arrays.stream(components).map(component -> component.as(net.kyori.adventure.text.Component.class)).collect(Collectors.toList());

            if (AdventureFeature.TRANSLATABLE_ARGUMENTS_METHOD.isSupported()) {
                getBuilder().arguments(input);
            } else {
                TranslatableComponentCompat.builderArguments(getBuilder(), input);
            }
            return self();
        }

        @Override
        public TranslatableComponent.@NotNull Builder args(@NotNull Collection<Component> components) {
            var input = components.stream().map(component -> component.as(net.kyori.adventure.text.Component.class)).collect(Collectors.toList());

            if (AdventureFeature.TRANSLATABLE_ARGUMENTS_METHOD.isSupported()) {
                getBuilder().arguments(input);
            } else {
                TranslatableComponentCompat.builderArguments(getBuilder(), input);
            }
            return self();
        }

        @Override
        public TranslatableComponent.@NotNull Builder fallback(@Nullable String fallback) {
            if (AdventureFeature.TRANSLATABLE_FALLBACK.isSupported()) {
                getBuilder().fallback(fallback);
            }
            return self();
        }
    }
}
