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

package org.screamingsandals.lib.bukkit.world.gamerule;

import org.bukkit.GameRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.ResourceLocation;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.world.gamerule.GameRuleRegistry;
import org.screamingsandals.lib.world.gamerule.GameRuleType;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitGameRuleRegistry extends GameRuleRegistry {
    // TODO: is there any bukkit-like server supporting custom values for this registry?

    private final boolean hasGameRule = Reflect.has("org.bukkit.GameRule");
    private final @NotNull List<@NotNull String> compat;

    public BukkitGameRuleRegistry() {
        if (!hasGameRule) {
            // bukkit api for legacy version didn't have proper game rules api
            // TODO: Actually fix this using NMS
            //Arrays.stream(Bukkit.getWorlds().get(0).getGameRules())
            compat = List.of("doFireTick", "mobGriefing", "keepInventory", "doMobSpawning", "doMobLoot", "doTileDrops", "commandBlockOutput", "naturalRegeneration", "doDaylightCycle",
                            "logAdminCommands", "showDeathMessages", "randomTickSpeed", "sendCommandFeedback", "reducedDebugInfo", "doEntityDrops", "spectatorsGenerateChunks",
                            "spawnRadius", "disableElytraMovementCheck", "doWeatherCycle", "maxEntityCramming", "doLimitedCrafting", "maxCommandChainLength", "announceAdvancements",
                            "gameLoopFunction"
                    );
        } else {
            compat = List.of();

            specialType(GameRule.class, BukkitGameRuleType::new);
        }
    }

    @Override
    protected @Nullable GameRuleType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null; // non-minecraft game rules probably don't exist on this platform
        }

        if (hasGameRule) {
            var g = GameRule.getByName(location.path());
            if (g != null) {
                return new BukkitGameRuleType(g);
            }
        } else {
            var str = compat.stream().filter(s -> s.equalsIgnoreCase(location.path())).findFirst();
            if (str.isPresent()) {
                return new BukkitLegacyGameRuleType(str.get());
            }
        }

        return null;
    }


    @Override
    protected @NotNull RegistryItemStream<@NotNull GameRuleType> getRegistryItemStream0() {
        if (hasGameRule) {
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(GameRule.values()),
                    BukkitGameRuleType::new,
                    gameRule -> ResourceLocation.of(gameRule.getName()),
                    (gameRule, literal) -> gameRule.getName().toLowerCase(Locale.ROOT).contains(literal),
                    (gameRule, namespace) -> "minecraft".equals(namespace),
                    List.of()
            );
        } else {
            return new SimpleRegistryItemStream<>(
                    compat::stream,
                    BukkitLegacyGameRuleType::new,
                    ResourceLocation::of,
                    (environment, literal) -> environment.toLowerCase(Locale.ROOT).contains(literal),
                    (environment, namespace) -> "minecraft".equals(namespace),
                    List.of()
            );
        }
    }
}
