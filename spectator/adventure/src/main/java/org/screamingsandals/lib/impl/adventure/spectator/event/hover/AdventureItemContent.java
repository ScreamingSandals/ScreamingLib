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

package org.screamingsandals.lib.impl.adventure.spectator.event.hover;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureFeature;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

public class AdventureItemContent extends BasicWrapper<HoverEvent.ShowItem> implements ItemContent {
    public AdventureItemContent(HoverEvent.@NotNull ShowItem wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ResourceLocation.of(wrappedObject.item().asString());
    }

    @SuppressWarnings("PatternValidation")
    @Override
    public @NotNull ItemContent withId(@NotNull ResourceLocation id) {
        if (AdventureFeature.SHOW_ITEM_NEW_FACTORY_METHOD.isSupported()) {
            return new AdventureItemContent(HoverEvent.ShowItem.showItem(Key.key(id.namespace(), id.path()), wrappedObject.count(), wrappedObject.nbt()));
        } else {
            //noinspection UnstableApiUsage
            return new AdventureItemContent(HoverEvent.ShowItem.of(Key.key(id.namespace(), id.path()), wrappedObject.count(), wrappedObject.nbt()));
        }
    }

    @Override
    public int count() {
        return wrappedObject.count();
    }

    @Override
    public @NotNull ItemContent withCount(int count) {
        if (AdventureFeature.SHOW_ITEM_NEW_FACTORY_METHOD.isSupported()) {
            return new AdventureItemContent(HoverEvent.ShowItem.showItem(wrappedObject.item(), count, wrappedObject.nbt()));
        } else {
            //noinspection UnstableApiUsage
            return new AdventureItemContent(HoverEvent.ShowItem.of(wrappedObject.item(), count, wrappedObject.nbt()));
        }
    }

    @Override
    public @Nullable CompoundTag tag() {
        var nbt = wrappedObject.nbt();
        if (nbt == null) {
            return null;
        }
        var tag =  AdventureBackend.getSnbtSerializer().deserialize(nbt.string());
        if (tag instanceof CompoundTag) {
            return (CompoundTag) tag;
        }
        return null;
    }

    @Override
    public @NotNull ItemContent withTag(@Nullable CompoundTag tag) {
        if (AdventureFeature.SHOW_ITEM_NEW_FACTORY_METHOD.isSupported()) {
            return new AdventureItemContent(HoverEvent.ShowItem.showItem(wrappedObject.item(), wrappedObject.count(), tag == null || tag.isEmpty() ? null : BinaryTagHolder.binaryTagHolder(AdventureBackend.getSnbtSerializer().serialize(tag))));
        } else {
            //noinspection UnstableApiUsage
            return new AdventureItemContent(HoverEvent.ShowItem.of(wrappedObject.item(), wrappedObject.count(), tag == null || tag.isEmpty() ? null : BinaryTagHolder.of(AdventureBackend.getSnbtSerializer().serialize(tag))));
        }
    }

    @Override
    public ItemContent.@NotNull Builder toBuilder() {
        return new AdventureItemContentBuilder(id(), count(), tag());
    }

    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (Throwable ignored) {
            return AdventureBackend.getAdditionalItemContentConverter().convert(this, type);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class AdventureItemContentBuilder implements ItemContent.Builder {
        private static final @NotNull ResourceLocation INVALID_KEY = ResourceLocation.of("minecraft", "air");

        private @NotNull ResourceLocation id = INVALID_KEY; // Should be air if not present
        private int count = 1;
        private @Nullable CompoundTag tag;

        @SuppressWarnings("PatternValidation")
        @Override
        public @NotNull ItemContent build() {
            if (AdventureFeature.SHOW_ITEM_NEW_FACTORY_METHOD.isSupported()) {
                return new AdventureItemContent(HoverEvent.ShowItem.showItem(Key.key(id.namespace(), id.path()), count, tag == null ? null : BinaryTagHolder.binaryTagHolder(AdventureBackend.getSnbtSerializer().serialize(tag))));
            } else {
                //noinspection UnstableApiUsage
                return new AdventureItemContent(HoverEvent.ShowItem.of(Key.key(id.namespace(), id.path()), count, tag == null ? null : BinaryTagHolder.of(AdventureBackend.getSnbtSerializer().serialize(tag))));
            }
        }
    }
}
