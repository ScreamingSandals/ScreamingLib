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

package org.screamingsandals.lib.block;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.TaggableHolder;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.*;

/**
 * Class representing a <strong>block</strong> material.
 *
 * Use {@link org.screamingsandals.lib.item.ItemTypeHolder} for item materials.
 */
@SuppressWarnings("AlternativeMethodAvailable")
public interface BlockTypeHolder extends ComparableWrapper, ParticleData, TaggableHolder {

    @NotNull String platformName();

    @Deprecated
    byte legacyData();

    @Deprecated
    @Contract(value = "_ -> new", pure = true)
    @NotNull BlockTypeHolder withLegacyData(byte legacyData);

    @Unmodifiable @NotNull Map<String, String> flatteningData();

    @Contract(value = "_ -> new", pure = true)
    @NotNull BlockTypeHolder withFlatteningData(@NotNull Map<@NotNull String, @NotNull String> flatteningData);

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull BlockTypeHolder with(@NotNull String attribute, @NotNull String value);

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull BlockTypeHolder with(@NotNull String attribute, int value);

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull BlockTypeHolder with(@NotNull String attribute, boolean value);

    @Contract(value = "_ -> new", pure = true)
    default @NotNull BlockTypeHolder colorize(@NotNull String color) {
        return BlockTypeMapper.colorize(this, color);
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
    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK_TYPE_TAG)
    boolean hasTag(@NotNull Object tag);

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    boolean isSameType(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    boolean isSameType(Object... objects);

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
    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    @Override
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    static @NotNull BlockTypeHolder of(@NotNull Object type) {
        var result = ofNullable(type);
        Preconditions.checkNotNullIllegal(result, "Could not find block type: " + type);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    @Contract("null -> null")
    static @Nullable BlockTypeHolder ofNullable(@Nullable Object type) {
        if (type instanceof BlockTypeHolder) {
            return (BlockTypeHolder) type;
        }
        return BlockTypeMapper.resolve(type);
    }

    static @NotNull BlockTypeHolder air() {
        return BlockTypeMapper.getCachedAir();
    }

    static @NotNull List<@NotNull BlockTypeHolder> all() {
        return BlockTypeMapper.getValues();
    }
}
