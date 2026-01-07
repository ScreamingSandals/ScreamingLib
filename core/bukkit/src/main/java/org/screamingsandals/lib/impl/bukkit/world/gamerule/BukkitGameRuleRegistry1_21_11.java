/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.world.gamerule;

import org.bukkit.GameRule;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.utils.BukkitRegistry;
import org.screamingsandals.lib.impl.world.gamerule.GameRuleRegistry;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.world.gamerule.GameRuleType;

@Service
public class BukkitGameRuleRegistry1_21_11 extends GameRuleRegistry {

    public BukkitGameRuleRegistry1_21_11() {
        specialType(GameRule.class, BukkitGameRuleType1_21_11::new);
    }

    @Override
    protected @Nullable GameRuleType resolveMappingPlatform(@NotNull ResourceLocation location) {
        return BukkitRegistry.tryObtainItem(Registry.GAME_RULE, BukkitGameRuleType1_21_11::new, location);
    }


    @Override
    protected @NotNull RegistryItemStream<@NotNull GameRuleType> getRegistryItemStream0() {
        return BukkitRegistry.registryStream(Registry.GAME_RULE, BukkitGameRuleType1_21_11::new);
    }
}
