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

package org.screamingsandals.lib.impl.bukkit.world.gamerule;

import org.bukkit.GameRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.utils.StringUtils;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.impl.world.gamerule.GameRuleRegistry;
import org.screamingsandals.lib.world.gamerule.GameRuleType;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitGameRuleRegistry extends GameRuleRegistry {
    // TODO: is there any bukkit-like server supporting custom values for this registry?

    private final @NotNull List<@NotNull String> compat;

    public BukkitGameRuleRegistry() {
        if (!BukkitFeature.GAME_RULE_API.isSupported()) {
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

            specialType(GameRule.class, BukkitGameRuleType1_13::new);
        }
    }

    @Override
    protected @Nullable GameRuleType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null; // non-minecraft game rules probably don't exist on this platform
        }

        if (BukkitFeature.GAME_RULE_API.isSupported()) {
            @Nullable GameRule<?> g = null;
            if (location.path().contains("_")) {
                // sponge api-like name, can be easily converted to proper mojang name
                g = GameRule.getByName(StringUtils.snakeToCamel(location.path()));
            } else {
                // lowercased mojang name, hell nah
                for (var r : GameRule.values()) {
                    if (r.getName().equalsIgnoreCase(location.path())) {
                        g = r;
                        break;
                    }
                }
            }
            if (g != null) {
                return new BukkitGameRuleType1_13(g);
            }
        } else {
            var str = compat.stream().filter(s -> s.equalsIgnoreCase(location.path().replace("_", ""))).findFirst();
            if (str.isPresent()) {
                return new BukkitGameRuleType1_8(str.get());
            }
        }

        return null;
    }


    @Override
    protected @NotNull RegistryItemStream<@NotNull GameRuleType> getRegistryItemStream0() {
        if (BukkitFeature.GAME_RULE_API.isSupported()) {
            return new SimpleRegistryItemStream<>(
                    () -> Arrays.stream(GameRule.values()),
                    BukkitGameRuleType1_13::new,
                    gameRule -> ResourceLocation.of(gameRule.getName()),
                    (gameRule, literal) -> gameRule.getName().toLowerCase(Locale.ROOT).contains(literal),
                    (gameRule, namespace) -> "minecraft".equals(namespace),
                    List.of()
            );
        } else {
            return new SimpleRegistryItemStream<>(
                    compat::stream,
                    BukkitGameRuleType1_8::new,
                    ResourceLocation::of,
                    (environment, literal) -> environment.toLowerCase(Locale.ROOT).contains(literal),
                    (environment, namespace) -> "minecraft".equals(namespace),
                    List.of()
            );
        }
    }
}
