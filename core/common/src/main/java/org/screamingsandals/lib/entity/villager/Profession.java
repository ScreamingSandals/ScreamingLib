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

package org.screamingsandals.lib.entity.villager;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.entity.villager.ProfessionRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

/**
 * Represents the various different Villager professions there may be.
 * Villagers have different trading options depending on their profession.
 * <p>
 * On pre-1.14 versions, BLACKSMITH is automatically translated to minecraft:toolsmith and PRIEST to minecraft:cleric.
 */
public interface Profession extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.PROFESSION) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.PROFESSION) @Nullable Object @NotNull... objects);

    static @NotNull Profession of(@MinecraftType(MinecraftType.Type.PROFESSION) @NotNull Object profession) {
        var result = ofNullable(profession);
        Preconditions.checkNotNullIllegal(result, "Could not find profession: " + profession);
        return result;
    }

    @Contract("null -> null")
    static @Nullable Profession ofNullable(@MinecraftType(MinecraftType.Type.PROFESSION) @Nullable Object profession) {
        if (profession instanceof Profession) {
            return (Profession) profession;
        }
        return ProfessionRegistry.getInstance().resolveMapping(profession);
    }

    static @NotNull RegistryItemStream<@NotNull Profession> all() {
        return ProfessionRegistry.getInstance().getRegistryItemStream();
    }
}
