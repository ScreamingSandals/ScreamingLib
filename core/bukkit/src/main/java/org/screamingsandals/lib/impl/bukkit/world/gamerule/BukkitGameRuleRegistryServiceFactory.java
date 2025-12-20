/*
 * Copyright 2025 ScreamingSandals
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

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.world.gamerule.GameRuleRegistry;
import org.screamingsandals.lib.utils.annotations.ServiceFactory;

@UtilityClass
@ServiceFactory
public class BukkitGameRuleRegistryServiceFactory {
    public static @NotNull GameRuleRegistry create() {
        if (BukkitFeature.GAME_RULE_REGISTRY.isSupported()) {
            return new BukkitGameRuleRegistry1_21_11();
        } else if (BukkitFeature.GAME_RULE_API.isSupported()) {
            return new BukkitGameRuleRegistry1_13();
        } else {
            return new BukkitGameRuleRegistry1_8();
        }
    }
}
