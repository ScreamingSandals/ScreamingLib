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

package org.screamingsandals.lib.impl.bukkit.entity.villager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.villager.VillagerType;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.List;
import java.util.stream.Stream;

public class BukkitVillagerTypeRegistry1_8 extends BukkitVillagerTypeRegistry {
    // There's only one villager type
    public static final @NotNull BukkitVillagerType1_8 INSTANCE = new BukkitVillagerType1_8();

    @Override
    protected @NotNull RegistryItemStream<@NotNull VillagerType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Stream.of(INSTANCE),
                t -> t,
                BukkitVillagerType1_8::location,
                (villagerType, literal) -> villagerType.location().path().contains(literal),
                (villagerType, namespace) -> villagerType.location().namespace().equals(namespace),
                List.of()
        );
    }

    @Override
    protected @Nullable VillagerType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (location.equals(INSTANCE.location())) {
            return INSTANCE;
        }
        return null;
    }
}
