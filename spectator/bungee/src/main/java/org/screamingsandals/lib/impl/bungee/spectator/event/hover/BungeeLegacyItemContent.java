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
import org.screamingsandals.lib.nbt.NumericTag;
import org.screamingsandals.lib.nbt.StringTag;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

public class BungeeLegacyItemContent extends BasicWrapper<CompoundTag> implements ItemContent {

    public BungeeLegacyItemContent(@NotNull String snbt) {
        this(readTag(snbt));
    }

    public BungeeLegacyItemContent(@NotNull CompoundTag tag) {
        super(tag);
    }

    private static @NotNull CompoundTag readTag(@NotNull String snbt) {
        var tag = AbstractBungeeBackend.getSnbtSerializer().deserialize(snbt);
        if (tag instanceof CompoundTag) {
            return (CompoundTag) tag;
        }
        return CompoundTag.EMPTY;
    }

    @Override
    public @NotNull ResourceLocation id() {
        if (wrappedObject != null) {
            var idTag = wrappedObject.tag("id");
            if (idTag instanceof StringTag) {
                return ResourceLocation.of(((StringTag) idTag).value());
            }
        }
        return ResourceLocation.of("minecraft", "air");
    }

    @Override
    public @NotNull ItemContent withId(@NotNull ResourceLocation id) {
        return new BungeeLegacyItemContent(wrappedObject.with("id", id.asString()));
    }

    @Override
    public int count() {
        if (wrappedObject != null) {
            var countTag = wrappedObject.tag("Count");
            if (countTag instanceof NumericTag) {
                return ((NumericTag) countTag).intValue();
            }
        }
        return 1;
    }

    @Override
    public @NotNull ItemContent withCount(int count) {
        return new BungeeLegacyItemContent(wrappedObject.with("Count", (byte) count));
    }

    @Override
    public @Nullable CompoundTag tag() {
        if (wrappedObject != null) {
            var tag = wrappedObject.tag("tag");
            if (tag instanceof CompoundTag) {
                return (CompoundTag) tag;
            }
        }
        return null;
    }

    @Override
    public @NotNull ItemContent withTag(@Nullable CompoundTag tag) {
        if (tag == null) {
            return new BungeeLegacyItemContent(wrappedObject.without("tag"));
        } else {
            return new BungeeLegacyItemContent(wrappedObject.with("tag", tag));
        }
    }

    @Override
    public ItemContent.@NotNull Builder toBuilder() {
        return new BungeeLegacyItemContentBuilder(id(), count(), tag());
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
    public static class BungeeLegacyItemContentBuilder implements ItemContent.Builder {
        private @Nullable ResourceLocation id;
        private int count = 1;
        private @Nullable CompoundTag tag;

        @Override
        public @NotNull ItemContent build() {
            var compoundTag = CompoundTag.EMPTY
                    .with("id", id == null ? "minecraft:air" : id.asString())
                    .with("Count", (byte) count);
            if (tag != null) {
                compoundTag = compoundTag.with("tag", tag);
            }
            return new BungeeLegacyItemContent(compoundTag);
        }
    }
}
