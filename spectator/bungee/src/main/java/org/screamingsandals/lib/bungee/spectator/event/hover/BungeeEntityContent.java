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

package org.screamingsandals.lib.bungee.spectator.event.hover;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.hover.content.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.UUID;

public class BungeeEntityContent extends BasicWrapper<Entity> implements EntityContent {
    public BungeeEntityContent(@NotNull Entity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public UUID id() {
        return UUID.fromString(wrappedObject.getId());
    }

    @Override
    @NotNull
    public EntityContent withId(@NotNull UUID id) {
        return new BungeeEntityContent(new Entity(wrappedObject.getType(), id.toString(), wrappedObject.getName()));
    }

    @Override
    @NotNull
    public NamespacedMappingKey type() {
        var type = wrappedObject.getType();
        if (type == null) {
            return NamespacedMappingKey.of("minecraft:pig"); // md_5's nice api said: will be pig if null
        }
        return NamespacedMappingKey.of(type);
    }

    @Override
    @NotNull
    public EntityContent withType(@NotNull NamespacedMappingKey type) {
        return new BungeeEntityContent(new Entity(type.asString(), wrappedObject.getId(), wrappedObject.getName()));
    }

    @Override
    @Nullable
    public Component name() {
        return AbstractBungeeBackend.wrapComponent(wrappedObject.getName());
    }

    @Override
    @NotNull
    public EntityContent withType(@Nullable Component name) {
        return new BungeeEntityContent(new Entity(wrappedObject.getType(), wrappedObject.getId(), name == null ? null : name.as(BaseComponent.class)));
    }

    @Override
    @NotNull
    public EntityContent.Builder toBuilder() {
        return new BungeeEntityContentBuilder(
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
            return AbstractBungeeBackend.getAdditionalEntityContentConverter().convert(this, type);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class BungeeEntityContentBuilder implements EntityContent.Builder {
        private UUID id;
        private NamespacedMappingKey type;
        private Component name;

        @Override
        @NotNull
        public EntityContent build() {
            return new BungeeEntityContent(new Entity(
                    type != null ? type.asString() : "minecraft:pig",
                    id != null ? id.toString() : UUID.randomUUID().toString(),
                    name != null ? name.as(BaseComponent.class) : null
            ));
        }
    }
}
