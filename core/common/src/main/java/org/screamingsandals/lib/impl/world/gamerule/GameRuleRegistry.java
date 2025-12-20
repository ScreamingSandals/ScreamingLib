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

package org.screamingsandals.lib.impl.world.gamerule;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ProvidedService;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistry;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.world.gamerule.GameRuleType;

@ProvidedService
@ApiStatus.Internal
public abstract class GameRuleRegistry extends SimpleRegistry<GameRuleType> {
    private static @Nullable GameRuleRegistry registry;

    public GameRuleRegistry() {
        super(GameRuleType.class);
        Preconditions.checkArgument(registry == null, "GameRuleRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull GameRuleRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "GameRuleRegistry is not initialized yet!");
    }

    @OnPostConstruct
    public void mapAliases() {
        // 1.21.10 <-> 1.21.11
        mapAlias("doDaylightCycle", "advance_time");
        mapAlias("doWeatherCycle", "advance_weather");
        mapAlias("allowEnteringNetherUsingPortals", "allow_entering_nether_using_portals");
        mapAlias("doTileDrops", "block_drops");
        mapAlias("blockExplosionDropDecay", "block_explosion_drop_decay");
        mapAlias("commandBlockOutput", "command_block_output");
        mapAlias("commandBlocksEnabled", "command_blocks_work");
        mapAlias("drowningDamage", "drowning_damage");
        mapAlias("enderPearlsVanishOnDeath", "ender_pearls_vanish_on_death");
        mapAlias("doEntityDrops", "entity_drops");
        mapAlias("fallDamage", "fall_damage");
        mapAlias("fireDamage", "fire_damage");
        mapAlias("forgiveDeadPlayers", "forgive_dead_players");
        mapAlias("freezeDamage", "freeze_damage");
        mapAlias("globalSoundEvents", "global_sound_events");
        mapAlias("doImmediateRespawn", "immediate_respawn");
        mapAlias("keepInventory", "keep_inventory");
        mapAlias("lavaSourceConversion", "lava_source_conversion");
        mapAlias("doLimitedCrafting", "limited_crafting");
        mapAlias("locatorBar", "locator_bar");
        mapAlias("logAdminCommands", "log_admin_commands");
        mapAlias("command_modification_block_limit", "max_block_modifications");
        mapAlias("maxCommandForkCount", "max_command_forks");
        mapAlias("maxCommandChainLength", "max_command_sequence_length");
        mapAlias("maxEntityCramming", "max_entity_cramming");
        mapAlias("minecartMaxSpeed", "max_minecart_speed");
        mapAlias("snowAccumulationHeight", "max_snow_accumulation_height");
        mapAlias("doMobLoot", "mob_drops");
        mapAlias("mobExplosionDropDecay", "mob_explosion_drop_decay");
        mapAlias("mobGriefing", "mob_griefing");
        mapAlias("naturalRegeneration", "natural_health_regeneration");
        mapAlias("playersNetherPortalCreativeDelay", "players_nether_portal_creative_delay");
        mapAlias("playersNetherPortalDefaultDelay", "players_nether_portal_default_delay");
        mapAlias("playersSleepingPercentage", "players_sleeping_percentage");
        mapAlias("projectilesCanBreakBlocks", "projectiles_can_break_blocks");
        mapAlias("randomTickSpeed", "random_tick_speed");
        mapAlias("reducedDebugInfo", "reduced_debug_info");
        mapAlias("spawnRadius", "respawn_radius");
        mapAlias("sendCommandFeedback", "send_command_feedback");
        mapAlias("announceAdvancements", "show_advancement_messages");
        mapAlias("showDeathMessages", "show_death_messages");
        mapAlias("spawnerBlocksEnabled", "spawner_blocks_work");
        mapAlias("doMobSpawning", "spawn_mobs");
        mapAlias("spawnMonsters", "spawn_monsters");
        mapAlias("doPatrolSpawning", "spawn_patrols");
        mapAlias("doInsomnia", "spawn_phantoms");
        mapAlias("doTraderSpawning", "spawn_wandering_traders");
        mapAlias("doWardenSpawning", "spawn_wardens");
        mapAlias("spectatorsGenerateChunks", "spectators_generate_chunks");
        mapAlias("doVinesSpread", "spread_vines");
        mapAlias("tntExplodes", "tnt_explodes");
        mapAlias("tntExplosionDropDecay", "tnt_explosion_drop_decay");
        mapAlias("universalAnger", "universal_anger");
        mapAlias("waterSourceConversion", "water_source_conversion");

        // TODO: solve these aliases (inverted values)
        //mapAlias("disableElytraMovementCheck", "elytra_movement_check");
        //mapAlias("disablePlayerMovementCheck", "player_movement_check");
        //mapAlias("disableRaids", "raids");

    }
}
