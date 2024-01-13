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

package org.screamingsandals.lib.impl.bungee.spectator.event.hover;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.StringTag;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.UUID;

public class BungeeLegacyEntityContent extends BasicWrapper<CompoundTag> implements EntityContent {
    public BungeeLegacyEntityContent(@NotNull String snbt) {
        this(readTag(snbt));
    }

    public BungeeLegacyEntityContent(@NotNull CompoundTag tag) {
        super(tag);
    }

    private static @NotNull CompoundTag readTag(@NotNull String snbt) {
        var tag = AbstractBungeeBackend.getSnbtSerializer().deserialize(snbt);
        if (tag instanceof CompoundTag) {
            return (CompoundTag) tag;
        }
        return CompoundTag.EMPTY;
    }

    // TODO: parse snbt
    @Override
    public @NotNull UUID id() {
        var string = wrappedObject.tag("id");
        if (string instanceof StringTag) {
            return UUID.fromString(((StringTag) string).value());
        }
        return UUID.randomUUID();
    }

    @Override
    public @NotNull EntityContent withId(@NotNull UUID id) {
        return new BungeeLegacyEntityContent(wrappedObject.with("id", id.toString()));
    }

    @Override
    public @NotNull ResourceLocation type() {
        var string = wrappedObject.tag("type");
        if (string instanceof StringTag) {
            return ResourceLocation.of(((StringTag) string).value());
        }
        return ResourceLocation.of("minecraft", "pig");
    }

    @Override
    public @NotNull EntityContent withType(@NotNull ResourceLocation type) {
        return new BungeeLegacyEntityContent(wrappedObject.with("type", type.asString()));
    }

    @Override
    public @Nullable Component name() {
        var name = wrappedObject.tag("name");
        if (name instanceof StringTag) {
            return Component.fromJavaJson(((StringTag) name).value());
        }
        return null;
    }

    @Override
    public @NotNull EntityContent withType(@Nullable Component name) {
        if (name != null) {
            return new BungeeLegacyEntityContent(wrappedObject.with("name", name.toJavaJson()));
        } else {
            return new BungeeLegacyEntityContent(wrappedObject.without("name"));
        }
    }

    @Override
    public EntityContent.@NotNull Builder toBuilder() {
        return new BungeeLegacyEntityContentBuilder(
                id(),
                type(),
                name()
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == String.class) {
            return (T) AbstractBungeeBackend.getSnbtSerializer().serialize(wrappedObject);
        }
        return super.as(type);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class BungeeLegacyEntityContentBuilder implements EntityContent.Builder {
        private @Nullable UUID id;
        private @Nullable ResourceLocation type;
        private @Nullable Component name;

        @Override
        public @NotNull EntityContent build() {
            var compound = CompoundTag.EMPTY
                    .with("id", id != null ? id.toString() : UUID.randomUUID().toString())
                    .with("type", type != null ? type.asString() : "minecraft:pig");
            if (name != null) {
                compound = compound.with("name", name.toJavaJson());
            }
            return new BungeeLegacyEntityContent(compound);
        }
    }
}
