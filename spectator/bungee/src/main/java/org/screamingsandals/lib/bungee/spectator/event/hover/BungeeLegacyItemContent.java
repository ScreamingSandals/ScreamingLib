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

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class BungeeLegacyItemContent extends BasicWrapper<String> implements ItemContent {
    public BungeeLegacyItemContent(String snbt) {
        super(snbt);
    }

    // TODO: parse snbt
    @Override
    public NamespacedMappingKey id() {
        return null; // TODO
    }

    @Override
    public int count() {
        return 0; // TODO
    }

    @Override
    public @Nullable String tag() {
        return wrappedObject; // TODO
    }

    public static class BungeeLegacyItemContentBuilder implements ItemContent.Builder {
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
            return new BungeeLegacyItemContent(
                    "{id: " + (id == null ? "minecraft:air" : id.asString()) + ", Count: " + count + "b"
                            + (tag != null && !tag.isEmpty() ? ", tag: " + tag : "")
                            + "}" // I hope this is correct
            );
        }
    }
}
