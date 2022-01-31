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

import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class BungeeItemContent extends BasicWrapper<Item> implements ItemContent {
    public BungeeItemContent(Item wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public NamespacedMappingKey id() {
        var id = wrappedObject.getId();
        if (id == null) {
            return NamespacedMappingKey.of("minecraft:air"); // md_5's nice api said: will be air if null
        }
        return NamespacedMappingKey.of(id);
    }

    @Override
    public int count() {
        return wrappedObject.getCount();
    }

    @Override
    public @Nullable String tag() {
        var tag = wrappedObject.getTag();
        if (tag == null) {
            return null;
        }
        return tag.getNbt();
    }

    public static class BungeeItemContentBuilder implements ItemContent.Builder {
        private NamespacedMappingKey id;
        private int count = 1;
        private String tag;

        @Override
        public Builder id(NamespacedMappingKey id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder count(int count) {
            this.count = count;
            return this;
        }

        @Override
        public Builder tag(@Nullable String tag) {
            this.tag = tag;
            return this;
        }

        @Override
        public ItemContent build() {
            return new BungeeItemContent(new Item(
                    id != null ? id.asString() : "minecraft:air",
                    count,
                    tag != null ? ItemTag.ofNbt(tag) : null
            ));
        }
    }
}
