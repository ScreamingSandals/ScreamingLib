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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class AdventureItemContent extends BasicWrapper<HoverEvent.ShowItem> implements ItemContent {
    public AdventureItemContent(HoverEvent.ShowItem wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public NamespacedMappingKey id() {
        return NamespacedMappingKey.of(wrappedObject.item().asString());
    }

    @Override
    public int count() {
        return wrappedObject.count();
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

    public static class AdventureItemContentBuilder implements ItemContent.Builder {
        private static final NamespacedMappingKey INVALID_KEY = NamespacedMappingKey.of("minecraft", "air");

        private NamespacedMappingKey id = INVALID_KEY; // Should be air if not present
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

        @SuppressWarnings("PatternValidation")
        @Override
        public ItemContent build() {
            return new AdventureItemContent(HoverEvent.ShowItem.of(Key.key(id.getNamespace(), id.getKey()), count, tag == null || tag.isEmpty() ? null : BinaryTagHolder.of(tag)));
        }
    }
}
