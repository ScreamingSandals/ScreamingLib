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

package org.screamingsandals.lib.item;

import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.TaggableHolder;
import org.screamingsandals.lib.api.types.server.ItemTypeHolder;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.impl.item.ItemTypeRegistry;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

@Accessors(fluent = true)
public interface ItemType extends RegistryItem, ParticleData, TaggableHolder, ItemTypeHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    default boolean isAir() {
        return equals(air());
    }

    int maxStackSize();

    default @NotNull ItemType colorize(@NotNull String color) {
        return ItemTypeRegistry.colorize(this, color);
    }

    @Nullable Block block();

    @Override
    boolean hasTag(@MinecraftType(MinecraftType.Type.ITEM_TYPE_TAG) @NotNull Object tag);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.ITEM_TYPE_OR_TAG) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.ITEM_TYPE_OR_TAG) @Nullable Object @NotNull... objects);

    static @NotNull ItemType of(@MinecraftType(MinecraftType.Type.ITEM_TYPE) @NotNull Object type) {
        var result = ofNullable(type);
        Preconditions.checkNotNullIllegal(result, "Could not find item type: " + type);
        return result;
    }

    @Contract("null -> null")
    static @Nullable ItemType ofNullable(@MinecraftType(MinecraftType.Type.ITEM_TYPE) @Nullable Object type) {
        if (type instanceof ItemType) {
            return (ItemType) type;
        }
        return ItemTypeRegistry.getInstance().resolveMapping(type);
    }

    static @NotNull ItemType air() {
        return ItemTypeRegistry.getCachedAir();
    }

    static @NotNull RegistryItemStream<@NotNull ItemType> all() {
        return ItemTypeRegistry.getInstance().getRegistryItemStream();
    }
}
