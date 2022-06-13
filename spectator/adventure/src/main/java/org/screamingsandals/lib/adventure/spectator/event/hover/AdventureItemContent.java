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

package org.screamingsandals.lib.adventure.spectator.event.hover;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class AdventureItemContent extends BasicWrapper<HoverEvent.ShowItem> implements ItemContent {
    public AdventureItemContent(HoverEvent.ShowItem wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public NamespacedMappingKey id() {
        return NamespacedMappingKey.of(wrappedObject.item().asString());
    }

    @SuppressWarnings("PatternValidation")
    @Override
    @NotNull
    public ItemContent withId(@NotNull NamespacedMappingKey id) {
        return new AdventureItemContent(HoverEvent.ShowItem.of(Key.key(id.getNamespace(), id.getKey()), wrappedObject.count(), wrappedObject.nbt()));
    }

    @Override
    public int count() {
        return wrappedObject.count();
    }

    @Override
    @NotNull
    public ItemContent withCount(int count) {
        return new AdventureItemContent(HoverEvent.ShowItem.of(wrappedObject.item(), count, wrappedObject.nbt()));
    }

    @Override
    @Nullable
    public String tag() {
        var nbt = wrappedObject.nbt();
        if (nbt == null) {
            return null;
        }
        return nbt.string();
    }

    @Override
    @NotNull
    public ItemContent withTag(@Nullable String tag) {
        return new AdventureItemContent(HoverEvent.ShowItem.of(wrappedObject.item(), wrappedObject.count(), tag == null || tag.isEmpty() ? null : BinaryTagHolder.of(tag)));
    }

    @Override
    @NotNull
    public ItemContent.Builder toBuilder() {
        return new AdventureItemContentBuilder(id(), count(), tag());
    }

    @Override
    public <T> T as(Class<T> type) {
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
        private static final NamespacedMappingKey INVALID_KEY = NamespacedMappingKey.of("minecraft", "air");

        private NamespacedMappingKey id = INVALID_KEY; // Should be air if not present
        private int count = 1;
        private String tag;

        @SuppressWarnings("PatternValidation")
        @Override
        @NotNull
        public ItemContent build() {
            return new AdventureItemContent(HoverEvent.ShowItem.of(Key.key(id.getNamespace(), id.getKey()), count, tag == null || tag.isEmpty() ? null : BinaryTagHolder.of(tag)));
        }
    }
}
