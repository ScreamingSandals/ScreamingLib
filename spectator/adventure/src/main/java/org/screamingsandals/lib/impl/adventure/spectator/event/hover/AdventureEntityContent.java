/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.adventure.spectator.event.hover;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureFeature;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.UUID;

public class AdventureEntityContent extends BasicWrapper<HoverEvent.ShowEntity> implements EntityContent {
    public AdventureEntityContent(HoverEvent.@NotNull ShowEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull UUID id() {
        return wrappedObject.id();
    }

    @Override
    public @NotNull EntityContent withId(@NotNull UUID id) {
        if (AdventureFeature.SHOW_ENTITY_NEW_FACTORY_METHOD.isSupported()) {
            return new AdventureEntityContent(HoverEvent.ShowEntity.showEntity(wrappedObject.type(), id, wrappedObject.name()));
        } else {
            //noinspection UnstableApiUsage
            return new AdventureEntityContent(HoverEvent.ShowEntity.of(wrappedObject.type(), id, wrappedObject.name()));
        }
    }

    @Override
    public @NotNull ResourceLocation type() {
        return ResourceLocation.of(wrappedObject.type().asString());
    }

    @SuppressWarnings("PatternValidation")
    @Override
    public @NotNull EntityContent withType(@NotNull ResourceLocation type) {
        if (AdventureFeature.SHOW_ENTITY_NEW_FACTORY_METHOD.isSupported()) {
            return new AdventureEntityContent(HoverEvent.ShowEntity.showEntity(Key.key(type.namespace(), type.path()), wrappedObject.id(), wrappedObject.name()));
        } else {
            //noinspection UnstableApiUsage
            return new AdventureEntityContent(HoverEvent.ShowEntity.of(Key.key(type.namespace(), type.path()), wrappedObject.id(), wrappedObject.name()));
        }
    }

    @Override
    public @Nullable Component name() {
        return AdventureBackend.wrapComponent(wrappedObject.name());
    }

    @Override
    public @NotNull EntityContent withType(@Nullable Component name) {
        if (AdventureFeature.SHOW_ENTITY_NEW_FACTORY_METHOD.isSupported()) {
            return new AdventureEntityContent(HoverEvent.ShowEntity.showEntity(wrappedObject.type(), wrappedObject.id(), name == null ? null : name.as(net.kyori.adventure.text.Component.class)));
        } else {
            //noinspection UnstableApiUsage
            return new AdventureEntityContent(HoverEvent.ShowEntity.of(wrappedObject.type(), wrappedObject.id(), name == null ? null : name.as(net.kyori.adventure.text.Component.class)));
        }
    }

    @Override
    public EntityContent.@NotNull Builder toBuilder() {
        return new AdventureEntityContentBuilder(
                id(),
                type(),
                name()
        );
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AdventureBackend.getAdditionalEntityContentConverter().convert(this, type);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class AdventureEntityContentBuilder implements EntityContent.Builder {
        private static final @NotNull ResourceLocation INVALID_KEY = ResourceLocation.of("minecraft", "pig");

        private @Nullable UUID id;
        private @NotNull ResourceLocation type = INVALID_KEY; // Should be pig if not present
        private @Nullable Component name;

        @SuppressWarnings("PatternValidation")
        @Override
        public @NotNull EntityContent build() {
            Preconditions.checkArgument(id != null, "Id of the entity is not specified!");
            if (AdventureFeature.SHOW_ENTITY_NEW_FACTORY_METHOD.isSupported()) {
                return new AdventureEntityContent(HoverEvent.ShowEntity.showEntity(Key.key(type.namespace(), type.path()), id, name == null ? null : name.as(net.kyori.adventure.text.Component.class)));
            } else {
                //noinspection UnstableApiUsage
                return new AdventureEntityContent(HoverEvent.ShowEntity.of(Key.key(type.namespace(), type.path()), id, name == null ? null : name.as(net.kyori.adventure.text.Component.class)));
            }
        }
    }
}
