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

package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.nbt.Tag;
import org.screamingsandals.lib.nbt.configurate.TagSerializer;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ItemContentSerializer implements TypeSerializer<ItemContent> {
    public static final @NotNull ItemContentSerializer INSTANCE = new ItemContentSerializer();

    private static final @NotNull SNBTSerializer internalSNBTSerializer = SNBTSerializer.builder().shouldSaveLongArraysDirectly(true).build();
    private static final @NotNull String ID_KEY = "id";
    private static final @NotNull String COUNT_KEY = "count";
    private static final @NotNull String TAG_KEY = "tag";

    @Override
    public @NotNull ItemContent deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        try {
            var id = ResourceLocation.of(node.node(ID_KEY).getString("minecraft:air"));
            var count = node.node(COUNT_KEY).getInt(1);
            var tag = node.node(TAG_KEY);
            CompoundTag compoundTag;
            if (!tag.empty()) {
                if (tag.isMap()) {
                    compoundTag = (CompoundTag) TagSerializer.INSTANCE.deserialize(Tag.class, tag);
                } else {
                    compoundTag = (CompoundTag) internalSNBTSerializer.deserialize(tag.getString(""));
                }
            } else {
                compoundTag = null;
            }
            return ItemContent.builder()
                    .id(id)
                    .count(count)
                    .tag(compoundTag)
                    .build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(@NotNull Type type, @Nullable ItemContent obj, @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(ID_KEY).set(obj.id().asString());
        node.node(COUNT_KEY).set(obj.count());
        node.node(TAG_KEY).set(obj.tag());
    }
}
