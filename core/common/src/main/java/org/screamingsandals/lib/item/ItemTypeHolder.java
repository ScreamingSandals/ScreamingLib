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

package org.screamingsandals.lib.item;

import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.TaggableHolder;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.List;

@SuppressWarnings("AlternativeMethodAvailable")
@Accessors(fluent = true)
public interface ItemTypeHolder extends ComparableWrapper, ParticleData, TaggableHolder {
    String platformName();

    @Deprecated
    @LimitedVersionSupport("<= 1.12.2")
    short forcedDurability();

    default boolean isAir() {
        return equals(air());
    }

    int maxStackSize();

    @Deprecated
    @LimitedVersionSupport("<= 1.12.2")
    @Contract(value = "_ -> new", pure = true)
    @NotNull ItemTypeHolder withForcedDurability(short durability);

    /**
     * Use fluent variant!!
     */
    @Deprecated(forRemoval = true)
    default int getMaxStackSize() {
        return maxStackSize();
    }

    default @NotNull ItemTypeHolder colorize(@NotNull String color) {
        return ItemTypeMapper.colorize(this, color);
    }

    @Nullable BlockTypeHolder block();

    @CustomAutocompletion(CustomAutocompletion.Type.ITEM_TYPE_TAG)
    @Override
    boolean hasTag(@NotNull Object tag);

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Override
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    static @NotNull ItemTypeHolder of(@NotNull Object type) {
        var result = ofNullable(type);
        Preconditions.checkNotNullIllegal(result, "Could not find item type: " + type);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Contract("null -> null")
    static @Nullable ItemTypeHolder ofNullable(@Nullable Object type) {
        if (type instanceof ItemTypeHolder) {
            return (ItemTypeHolder) type;
        }
        return ItemTypeMapper.resolve(type);
    }

    static @NotNull ItemTypeHolder air() {
        return ItemTypeMapper.getCachedAir();
    }

    static @NotNull List<@NotNull ItemTypeHolder> all() {
        return ItemTypeMapper.getValues();
    }
}
