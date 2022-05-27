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
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class BungeeItemContent extends BasicWrapper<Item> implements ItemContent {
    public BungeeItemContent(Item wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public NamespacedMappingKey id() {
        var id = wrappedObject.getId();
        if (id == null) {
            return NamespacedMappingKey.of("minecraft:air"); // md_5's nice api said: will be air if null
        }
        return NamespacedMappingKey.of(id);
    }

    @Override
    @NotNull
    public ItemContent withId(@NotNull NamespacedMappingKey id) {
        return new BungeeItemContent(new Item(id.asString(), wrappedObject.getCount(), wrappedObject.getTag()));
    }

    @Override
    public int count() {
        return wrappedObject.getCount();
    }

    @Override
    @NotNull
    public ItemContent withCount(int count) {
        return new BungeeItemContent(new Item(wrappedObject.getId(), count, wrappedObject.getTag()));
    }

    @Override
    @Nullable
    public String tag() {
        var tag = wrappedObject.getTag();
        if (tag == null) {
            return null;
        }
        return tag.getNbt();
    }

    @Override
    @NotNull
    public ItemContent withTag(@Nullable String tag) {
        return new BungeeItemContent(new Item(wrappedObject.getId(), wrappedObject.getCount(), tag != null ? ItemTag.ofNbt(tag) : null));
    }

    @Override
    @NotNull
    public ItemContent.Builder toBuilder() {
        return new BungeeItemContentBuilder(id(), count(), tag());
    }

    @Override
    public <T> T as(Class<T> type) {
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
        private NamespacedMappingKey id;
        private int count = 1;
        private String tag;

        @Override
        @NotNull
        public ItemContent build() {
            return new BungeeItemContent(new Item(
                    id != null ? id.asString() : "minecraft:air",
                    count,
                    tag != null ? ItemTag.ofNbt(tag) : null
            ));
        }
    }
}