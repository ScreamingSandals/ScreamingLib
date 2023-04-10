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
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

public class BungeeItemContent extends BasicWrapper<Item> implements ItemContent {
    public BungeeItemContent(@NotNull Item wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull ResourceLocation id() {
        var id = wrappedObject.getId();
        if (id == null) {
            return ResourceLocation.of("minecraft:air"); // md_5's nice api said: will be air if null
        }
        return ResourceLocation.of(id);
    }

    @Override
    public @NotNull ItemContent withId(@NotNull ResourceLocation id) {
        return new BungeeItemContent(new Item(id.asString(), wrappedObject.getCount(), wrappedObject.getTag()));
    }

    @Override
    public int count() {
        return wrappedObject.getCount();
    }

    @Override
    public @NotNull ItemContent withCount(int count) {
        return new BungeeItemContent(new Item(wrappedObject.getId(), count, wrappedObject.getTag()));
    }

    @Override
    public @Nullable CompoundTag tag() {
        var tag = wrappedObject.getTag();
        if (tag == null) {
            return null;
        }
        var compound = AbstractBungeeBackend.getSnbtSerializer().deserialize(tag.getNbt());
        if (compound instanceof CompoundTag) {
            return (CompoundTag) compound;
        }
        return null;
    }

    @Override
    public @NotNull ItemContent withTag(@Nullable CompoundTag tag) {
        return new BungeeItemContent(new Item(wrappedObject.getId(), wrappedObject.getCount(), tag != null ? ItemTag.ofNbt(AbstractBungeeBackend.getSnbtSerializer().serialize(tag)) : null));
    }

    @Override
    public ItemContent.@NotNull Builder toBuilder() {
        return new BungeeItemContentBuilder(id(), count(), tag());
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AbstractBungeeBackend.getAdditionalItemContentConverter().convert(this, type);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class BungeeItemContentBuilder implements ItemContent.Builder {
        private @Nullable ResourceLocation id;
        private int count = 1;
        private @Nullable CompoundTag tag;

        @Override
        public @NotNull ItemContent build() {
            return new BungeeItemContent(new Item(
                    id != null ? id.asString() : "minecraft:air",
                    count,
                    tag != null ? ItemTag.ofNbt(AbstractBungeeBackend.getSnbtSerializer().serialize(tag)) : null
            ));
        }
    }
}
