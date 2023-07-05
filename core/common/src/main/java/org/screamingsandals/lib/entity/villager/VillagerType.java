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

package org.screamingsandals.lib.entity.villager;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.entity.villager.VillagerTypeRegistry;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.annotations.ide.MinecraftType;
import org.screamingsandals.lib.utils.registry.RegistryItem;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

/**
 * Represents the various different Villager types there may be. On pre-1.14, the only existing type is {@code minecraft:plains}.
 */
@LimitedVersionSupport(">= 1.14; <= 1.13.2: the only existing villager type is minecraft:plains")
public interface VillagerType extends RegistryItem, RawValueHolder {
    @ApiStatus.Experimental
    @NotNull String platformName();

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.VILLAGER_TYPE) @Nullable Object object);

    @Override
    boolean is(@MinecraftType(MinecraftType.Type.VILLAGER_TYPE) @Nullable Object @NotNull... objects);

    static @NotNull VillagerType of(@MinecraftType(MinecraftType.Type.VILLAGER_TYPE) @NotNull Object villagerTypes) {
        var result = ofNullable(villagerTypes);
        Preconditions.checkNotNullIllegal(result, "Could not find villager type: " + villagerTypes);
        return result;
    }

    @Contract("null -> null")
    static @Nullable VillagerType ofNullable(@MinecraftType(MinecraftType.Type.VILLAGER_TYPE) @Nullable Object villagerType) {
        if (villagerType instanceof VillagerType) {
            return (VillagerType) villagerType;
        }
        return VillagerTypeRegistry.getInstance().resolveMapping(villagerType);
    }

    static @NotNull RegistryItemStream<@NotNull VillagerType> all() {
        return VillagerTypeRegistry.getInstance().getRegistryItemStream();
    }
}
