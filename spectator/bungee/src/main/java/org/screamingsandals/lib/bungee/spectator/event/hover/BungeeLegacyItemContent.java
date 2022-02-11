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
import org.jetbrains.annotations.NotNull;
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
    @NotNull
    public NamespacedMappingKey id() {
        return null; // TODO
    }

    @Override
    @NotNull
    public ItemContent withId(@NotNull NamespacedMappingKey id) {
        return this; // TODO
    }

    @Override
    public int count() {
        return 0; // TODO
    }

    @Override
    @NotNull
    public ItemContent withCount(int count) {
        return this; // TODO
    }

    @Override
    @Nullable
    public String tag() {
        return wrappedObject; // TODO
    }

    @Override
    @NotNull
    public ItemContent withTag(@Nullable String tag) {
        return this; // TODO
    }

    @Override
    @NotNull
    public ItemContent.Builder toBuilder() {
        return new BungeeLegacyItemContentBuilder(id(), count(), tag());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class BungeeLegacyItemContentBuilder implements ItemContent.Builder {
        private NamespacedMappingKey id;
        private int count = 1;
        private String tag;

        @Override
        @NotNull
        public ItemContent build() {
            return new BungeeLegacyItemContent(
                    "{id: " + (id == null ? "minecraft:air" : id.asString()) + ", Count: " + count + "b"
                            + (tag != null && !tag.isEmpty() ? ", tag: " + tag : "")
                            + "}" // I hope this is correct
            );
        }
    }
}
