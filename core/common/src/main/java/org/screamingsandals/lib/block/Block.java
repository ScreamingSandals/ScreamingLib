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

package org.screamingsandals.lib.block;

import org.jetbrains.annotations.*;
import org.screamingsandals.lib.TaggableHolder;
import org.screamingsandals.lib.api.types.server.BlockHolder;
import org.screamingsandals.lib.impl.block.BlockRegistry;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.*;

/**
 * Class representing a <strong>block</strong> material.
 * <p>
 * Use {@link ItemType} for item materials.
 */
public interface Block extends RegistryItem, ParticleData, TaggableHolder, BlockHolder {

    @ApiStatus.Experimental
    @NotNull String platformName();

    @Unmodifiable @NotNull Map<@NotNull String, String> stateData();

    @Contract(value = "_ -> new", pure = true)
    @NotNull Block withStateData(@NotNull Map<@NotNull String, String> stateData);

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull Block with(@NotNull String attribute, @NotNull String value);

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull Block with(@NotNull String attribute, int value);

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull Block with(@NotNull String attribute, boolean value);

    @Contract(value = "_ -> new", pure = true)
    default @NotNull Block colorize(@NotNull String color) {
        return BlockRegistry.colorize(this, color);
    }

    @Nullable String get(@NotNull String attribute);

    @Nullable Integer getInt(@NotNull String attribute);

    @Nullable Boolean getBoolean(@NotNull String attribute);

    default boolean isAir() {
        return isSameType(air(), "minecraft:cave_air", "minecraft:void_air");
    }

    boolean isSolid();

    boolean isTransparent();

    boolean isFlammable();

    boolean isBurnable();

    boolean isOccluding();

    boolean hasGravity();

    @Override
    boolean hasTag(@MinecraftType(MinecraftType.Type.BLOCK_TYPE_TAG) @NotNull Object tag);

    boolean isSameType(@MinecraftType(MinecraftType.Type.BLOCK_TYPE) @Nullable Object object);

    boolean isSameType(@MinecraftType(MinecraftType.Type.BLOCK_TYPE) @Nullable Object @NotNull... objects);

    /**
     * This method accept any object that represents block type, or:
     * <br>
     * tags if prefixed with #
     * <br>
     * the type without the exact state if suffixed by [*] (alternative to {@link #isSameType(Object)}
     *
     * @param object object that represents block type
     * @return true if this block is the same as the object
     */
    @Override
    boolean is(@MinecraftType(MinecraftType.Type.BLOCK_OR_TAG) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.BLOCK_OR_TAG) @Nullable Object @NotNull... objects);

    /**
     * Returns location followed by this specific block state
     *
     * @return location followed by this specific block state
     */
    @NotNull String completeState();

    static @NotNull Block of(@MinecraftType(MinecraftType.Type.BLOCK) @NotNull Object type) {
        var result = ofNullable(type);
        Preconditions.checkNotNullIllegal(result, "Could not find block type: " + type);
        return result;
    }

    @Contract("null -> null")
    static @Nullable Block ofNullable(@MinecraftType(MinecraftType.Type.BLOCK) @Nullable Object type) {
        if (type instanceof Block) {
            return (Block) type;
        }
        return BlockRegistry.getInstance().resolveMapping(type);
    }

    static @NotNull Block air() {
        return BlockRegistry.getCachedAir();
    }

    static @NotNull RegistryItemStream<@NotNull Block> all() {
        return BlockRegistry.getInstance().getRegistryItemStream();
    }
}
