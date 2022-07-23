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

package org.screamingsandals.lib.block;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.TaggableHolder;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.*;

/**
 * Class representing a <strong>block</strong> material.
 *
 * Use {@link org.screamingsandals.lib.item.ItemTypeHolder} for item materials.
 */
@SuppressWarnings("AlternativeMethodAvailable")
public interface BlockTypeHolder extends ComparableWrapper, ParticleData, TaggableHolder {

    String platformName();

    @Deprecated
    byte legacyData();

    @Deprecated
    @Contract(value = "_ -> new", pure = true)
    BlockTypeHolder withLegacyData(byte legacyData);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    @Unmodifiable
    Map<String, String> flatteningData();

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    @Contract(value = "_ -> new", pure = true)
    BlockTypeHolder withFlatteningData(Map<String, String> flatteningData);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    @Contract(value = "_, _ -> new", pure = true)
    BlockTypeHolder with(String attribute, String value);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    @Contract(value = "_, _ -> new", pure = true)
    BlockTypeHolder with(String attribute, int value);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    @Contract(value = "_, _ -> new", pure = true)
    BlockTypeHolder with(String attribute, boolean value);

    @Contract(value = "_ -> new", pure = true)
    default BlockTypeHolder colorize(String color) {
        return BlockTypeMapper.colorize(this, color);
    }

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    Optional<String> get(String attribute);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    Optional<Integer> getInt(String attribute);

    /**
     * NOTE: This method does not work in legacy environments yet!
     */
    Optional<Boolean> getBoolean(String attribute);

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
    static BlockTypeHolder of(Object type) {
        return ofOptional(type).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.BLOCK)
    static Optional<BlockTypeHolder> ofOptional(Object type) {
        if (type instanceof BlockTypeHolder) {
            return Optional.of((BlockTypeHolder) type);
        }
        return BlockTypeMapper.resolve(type);
    }

    static BlockTypeHolder air() {
        return BlockTypeMapper.getCachedAir();
    }

    static List<BlockTypeHolder> all() {
        return BlockTypeMapper.getValues();
    }
}
