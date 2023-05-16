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

package org.screamingsandals.lib.impl.bukkit;

import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.impl.bukkit.attribute.BukkitAttributeTypeRegistry;
import org.screamingsandals.lib.impl.bukkit.attribute.BukkitAttributes;
import org.screamingsandals.lib.impl.bukkit.block.snapshot.BukkitBlockSnapshots;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockRegistry;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacements;
import org.screamingsandals.lib.impl.bukkit.container.BukkitContainerFactory;
import org.screamingsandals.lib.impl.bukkit.container.type.BukkitInventoryTypeRegistry;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitEntities;
import org.screamingsandals.lib.impl.bukkit.entity.damage.BukkitDamageTypeRegistry;
import org.screamingsandals.lib.impl.bukkit.entity.pose.BukkitEntityPoseRegistry;
import org.screamingsandals.lib.impl.bukkit.entity.type.BukkitEntityTypeRegistry;
import org.screamingsandals.lib.impl.bukkit.entity.villager.BukkitProfessionRegistry;
import org.screamingsandals.lib.impl.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.impl.bukkit.event.BukkitEventManager;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockBurnEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockCookEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockDispenseEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockDropItemEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockExperienceEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockExplodeEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockFadeEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockFertilizeEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockFormEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockFormedByEntityEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockFromToEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockGrowEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockIgniteEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockPhysicsEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockPistonExtendEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockPistonRetractEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockReceivedGameEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockShearEntityEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitBlockSpreadEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitCauldronLevelChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitFluidLevelChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitLeavesDecayEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitMoistureChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.block.BukkitRedstoneEvent;
import org.screamingsandals.lib.impl.bukkit.event.chunk.BukkitChunkLoadEvent;
import org.screamingsandals.lib.impl.bukkit.event.chunk.BukkitChunkPopulateEvent;
import org.screamingsandals.lib.impl.bukkit.event.chunk.BukkitChunkUnloadEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitAreaEffectCloudApplyEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitArrowBodyCountChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitBatToggleSleepEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitCreatureSpawnEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitCreeperPowerEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEnderDragonChangePhaseEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityAirChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityBreedEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityChangeBlockEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityCombustByBlockEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityCombustByEntityEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityCombustEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityCreatePortalEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityDamageByBlockEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityDamageByEntityEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityDamageEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityDeathEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityDropItemEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityEnterBlockEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityEnterLoveModeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityExhaustionEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityExplodeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityInteractEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityPickupItemEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityPlaceEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityPortalEnterEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityPortalEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityPortalExitEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityPoseChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityPotionEffectEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityRegainHealthEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityResurrectEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityShootBowEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntitySpawnEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityTameEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityTargetEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityTargetLivingEntityEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityTeleportEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityToggleGlideEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityToggleSwimEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitEntityUnleashEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitExpBottleEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitExplosionPrimeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitFireworkExplodeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitFoodLevelChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitHorseJumpEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitItemDespawnEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitItemMergeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitItemSpawnEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitLegacyPlayerPickupItemEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitModernPlayerPickupItemEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitProjectileHitEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitProjectileLaunchEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitSheepDyeWoolEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitSheepRegrowWoolEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitSlimeSplitEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitStriderTemperatureChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitVehicleCreateEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitVillagerAcquireTradeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitVillagerCareerChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.entity.BukkitVillagerReplenishTradeEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitAsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerAnimationEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerArmorStandManipulateEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerBedEnterEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerBedLeaveEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerBlockBreakEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerBlockDamageEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerBlockPlaceEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerBucketEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerChatEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerCommandPreprocessEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerCommandSendEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerCraftItemEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerDeathEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerDropItemEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerEggThrowEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerExpChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerFishEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerFoodLevelChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerGameModeChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerHarvestBlockEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerInteractAtEntityEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerInteractEntityEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerInteractEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerInventoryClickEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerInventoryCloseEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerInventoryOpenEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerItemConsumeEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerItemDamageEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerItemHeldEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerItemMendEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerJoinEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerKickEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerLeaveEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerLevelChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerLocaleChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerLoginEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerMoveEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerPortalEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerRespawnEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerShearEntityEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerSwapHandItemsEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerTeleportEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerToggleFlightEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerToggleSneakEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerToggleSprintEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerUnleashEntityEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerUpdateSignEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerVelocityChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.player.BukkitPlayerWorldChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.world.BukkitPlantGrowEvent;
import org.screamingsandals.lib.impl.bukkit.event.world.BukkitSpawnChangeEvent;
import org.screamingsandals.lib.impl.bukkit.event.world.BukkitSpongeAbsorbEvent;
import org.screamingsandals.lib.impl.bukkit.event.world.BukkitTimeSkipEvent;
import org.screamingsandals.lib.impl.bukkit.event.world.BukkitWorldInitEvent;
import org.screamingsandals.lib.impl.bukkit.event.world.BukkitWorldLoadEvent;
import org.screamingsandals.lib.impl.bukkit.event.world.BukkitWorldSaveEvent;
import org.screamingsandals.lib.impl.bukkit.event.world.BukkitWorldUnloadEvent;
import org.screamingsandals.lib.impl.bukkit.firework.BukkitFireworkEffectRegistry;
import org.screamingsandals.lib.impl.bukkit.firework.BukkitFireworkEffectTypeRegistry;
import org.screamingsandals.lib.impl.bukkit.gameevent.BukkitGameEventRegistry;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItemTypeRegistry;
import org.screamingsandals.lib.impl.bukkit.item.builder.BukkitItemStackFactory;
import org.screamingsandals.lib.impl.bukkit.item.meta.BukkitEnchantmentRegistry;
import org.screamingsandals.lib.impl.bukkit.item.meta.BukkitPotionEffectRegistry;
import org.screamingsandals.lib.impl.bukkit.item.meta.BukkitPotionRegistry;
import org.screamingsandals.lib.impl.bukkit.particle.BukkitParticleTypeRegistry;
import org.screamingsandals.lib.impl.bukkit.player.BukkitPlayers;
import org.screamingsandals.lib.impl.bukkit.player.gamemode.BukkitGameModeRegistry;
import org.screamingsandals.lib.impl.bukkit.plugin.BukkitPlugin;
import org.screamingsandals.lib.impl.bukkit.slot.BukkitEquipmentSlotRegistry;
import org.screamingsandals.lib.impl.bukkit.spectator.SpigotBackend;
import org.screamingsandals.lib.impl.bukkit.tasker.BukkitTasker;
import org.screamingsandals.lib.impl.bukkit.world.BukkitLocations;
import org.screamingsandals.lib.impl.bukkit.world.BukkitWorlds;
import org.screamingsandals.lib.impl.bukkit.world.chunk.BukkitChunks;
import org.screamingsandals.lib.impl.bukkit.world.difficulty.BukkitDifficultyRegistry;
import org.screamingsandals.lib.impl.bukkit.world.dimension.BukkitDimensionRegistry;
import org.screamingsandals.lib.impl.bukkit.world.gamerule.BukkitGameRuleRegistry;
import org.screamingsandals.lib.impl.bukkit.world.weather.BukkitWeatherRegistry;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.Event;
import org.screamingsandals.lib.event.block.BlockBurnEvent;
import org.screamingsandals.lib.event.block.BlockCookEvent;
import org.screamingsandals.lib.event.block.BlockDispenseEvent;
import org.screamingsandals.lib.event.block.BlockDropItemEvent;
import org.screamingsandals.lib.event.block.BlockExperienceEvent;
import org.screamingsandals.lib.event.block.BlockExplodeEvent;
import org.screamingsandals.lib.event.block.BlockFadeEvent;
import org.screamingsandals.lib.event.block.BlockFertilizeEvent;
import org.screamingsandals.lib.event.block.BlockFromToEvent;
import org.screamingsandals.lib.event.block.BlockGrowEvent;
import org.screamingsandals.lib.event.block.BlockIgniteEvent;
import org.screamingsandals.lib.event.block.BlockPhysicsEvent;
import org.screamingsandals.lib.event.block.BlockPistonEvent;
import org.screamingsandals.lib.event.block.BlockReceivedGameEvent;
import org.screamingsandals.lib.event.block.BlockShearEntityEvent;
import org.screamingsandals.lib.event.block.CauldronLevelChangeEvent;
import org.screamingsandals.lib.event.block.FluidLevelChangeEvent;
import org.screamingsandals.lib.event.block.LeavesDecayEvent;
import org.screamingsandals.lib.event.block.MoistureChangeEvent;
import org.screamingsandals.lib.event.block.RedstoneEvent;
import org.screamingsandals.lib.event.chunk.ChunkLoadEvent;
import org.screamingsandals.lib.event.chunk.ChunkPopulateEvent;
import org.screamingsandals.lib.event.chunk.ChunkUnloadEvent;
import org.screamingsandals.lib.event.entity.AreaEffectCloudApplyEvent;
import org.screamingsandals.lib.event.entity.ArrowBodyCountChangeEvent;
import org.screamingsandals.lib.event.entity.BatToggleSleepEvent;
import org.screamingsandals.lib.event.entity.CreeperPowerEvent;
import org.screamingsandals.lib.event.entity.EnderDragonChangePhaseEvent;
import org.screamingsandals.lib.event.entity.EntityAirChangeEvent;
import org.screamingsandals.lib.event.entity.EntityBreedEvent;
import org.screamingsandals.lib.event.entity.EntityChangeBlockEvent;
import org.screamingsandals.lib.event.entity.EntityCombustEvent;
import org.screamingsandals.lib.event.entity.EntityCreatePortalEvent;
import org.screamingsandals.lib.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.event.entity.EntityDeathEvent;
import org.screamingsandals.lib.event.entity.EntityDropItemEvent;
import org.screamingsandals.lib.event.entity.EntityEnterBlockEvent;
import org.screamingsandals.lib.event.entity.EntityEnterLoveModeEvent;
import org.screamingsandals.lib.event.entity.EntityExhaustionEvent;
import org.screamingsandals.lib.event.entity.EntityExplodeEvent;
import org.screamingsandals.lib.event.entity.EntityInteractEvent;
import org.screamingsandals.lib.event.entity.EntityPickupItemEvent;
import org.screamingsandals.lib.event.entity.EntityPlaceEvent;
import org.screamingsandals.lib.event.entity.EntityPortalEnterEvent;
import org.screamingsandals.lib.event.entity.EntityPortalExitEvent;
import org.screamingsandals.lib.event.entity.EntityPoseChangeEvent;
import org.screamingsandals.lib.event.entity.EntityPotionEffectEvent;
import org.screamingsandals.lib.event.entity.EntityRegainHealthEvent;
import org.screamingsandals.lib.event.entity.EntityResurrectEvent;
import org.screamingsandals.lib.event.entity.EntityShootBowEvent;
import org.screamingsandals.lib.event.entity.EntitySpawnEvent;
import org.screamingsandals.lib.event.entity.EntityTameEvent;
import org.screamingsandals.lib.event.entity.EntityTargetEvent;
import org.screamingsandals.lib.event.entity.EntityTeleportEvent;
import org.screamingsandals.lib.event.entity.EntityToggleGlideEvent;
import org.screamingsandals.lib.event.entity.EntityToggleSwimEvent;
import org.screamingsandals.lib.event.entity.EntityUnleashEvent;
import org.screamingsandals.lib.event.entity.ExplosionPrimeEvent;
import org.screamingsandals.lib.event.entity.FireworkExplodeEvent;
import org.screamingsandals.lib.event.entity.FoodLevelChangeEvent;
import org.screamingsandals.lib.event.entity.HorseJumpEvent;
import org.screamingsandals.lib.event.entity.ItemDespawnEvent;
import org.screamingsandals.lib.event.entity.ItemMergeEvent;
import org.screamingsandals.lib.event.entity.ProjectileHitEvent;
import org.screamingsandals.lib.event.entity.SheepDyeWoolEvent;
import org.screamingsandals.lib.event.entity.SheepRegrowWoolEvent;
import org.screamingsandals.lib.event.entity.SlimeSplitEvent;
import org.screamingsandals.lib.event.entity.StriderTemperatureChangeEvent;
import org.screamingsandals.lib.event.entity.VehicleCreateEvent;
import org.screamingsandals.lib.event.entity.VillagerAcquireTradeEvent;
import org.screamingsandals.lib.event.entity.VillagerCareerChangeEvent;
import org.screamingsandals.lib.event.entity.VillagerReplenishTradeEvent;
import org.screamingsandals.lib.event.player.AsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.event.player.PlayerAnimationEvent;
import org.screamingsandals.lib.event.player.PlayerBedEnterEvent;
import org.screamingsandals.lib.event.player.PlayerBedLeaveEvent;
import org.screamingsandals.lib.event.player.PlayerBlockDamageEvent;
import org.screamingsandals.lib.event.player.PlayerBlockPlaceEvent;
import org.screamingsandals.lib.event.player.PlayerBucketEvent;
import org.screamingsandals.lib.event.player.PlayerChatEvent;
import org.screamingsandals.lib.event.player.PlayerCommandPreprocessEvent;
import org.screamingsandals.lib.event.player.PlayerCommandSendEvent;
import org.screamingsandals.lib.event.player.PlayerDropItemEvent;
import org.screamingsandals.lib.event.player.PlayerEggThrowEvent;
import org.screamingsandals.lib.event.player.PlayerExpChangeEvent;
import org.screamingsandals.lib.event.player.PlayerFishEvent;
import org.screamingsandals.lib.event.player.PlayerFoodLevelChangeEvent;
import org.screamingsandals.lib.event.player.PlayerGameModeChangeEvent;
import org.screamingsandals.lib.event.player.PlayerHarvestBlockEvent;
import org.screamingsandals.lib.event.player.PlayerInteractEntityEvent;
import org.screamingsandals.lib.event.player.PlayerInteractEvent;
import org.screamingsandals.lib.event.player.PlayerInventoryClickEvent;
import org.screamingsandals.lib.event.player.PlayerInventoryCloseEvent;
import org.screamingsandals.lib.event.player.PlayerInventoryOpenEvent;
import org.screamingsandals.lib.event.player.PlayerItemConsumeEvent;
import org.screamingsandals.lib.event.player.PlayerItemDamageEvent;
import org.screamingsandals.lib.event.player.PlayerItemHeldEvent;
import org.screamingsandals.lib.event.player.PlayerItemMendEvent;
import org.screamingsandals.lib.event.player.PlayerJoinEvent;
import org.screamingsandals.lib.event.player.PlayerKickEvent;
import org.screamingsandals.lib.event.player.PlayerLeaveEvent;
import org.screamingsandals.lib.event.player.PlayerLevelChangeEvent;
import org.screamingsandals.lib.event.player.PlayerLocaleChangeEvent;
import org.screamingsandals.lib.event.player.PlayerLoginEvent;
import org.screamingsandals.lib.event.player.PlayerMoveEvent;
import org.screamingsandals.lib.event.player.PlayerRespawnEvent;
import org.screamingsandals.lib.event.player.PlayerShearEntityEvent;
import org.screamingsandals.lib.event.player.PlayerSwapHandItemsEvent;
import org.screamingsandals.lib.event.player.PlayerToggleFlightEvent;
import org.screamingsandals.lib.event.player.PlayerToggleSneakEvent;
import org.screamingsandals.lib.event.player.PlayerToggleSprintEvent;
import org.screamingsandals.lib.event.player.PlayerUpdateSignEvent;
import org.screamingsandals.lib.event.player.PlayerVelocityChangeEvent;
import org.screamingsandals.lib.event.player.PlayerWorldChangeEvent;
import org.screamingsandals.lib.event.world.PlantGrowEvent;
import org.screamingsandals.lib.event.world.SpawnChangeEvent;
import org.screamingsandals.lib.event.world.SpongeAbsorbEvent;
import org.screamingsandals.lib.event.world.TimeSkipEvent;
import org.screamingsandals.lib.event.world.WorldInitEvent;
import org.screamingsandals.lib.event.world.WorldLoadEvent;
import org.screamingsandals.lib.event.world.WorldSaveEvent;
import org.screamingsandals.lib.event.world.WorldUnloadEvent;
import org.screamingsandals.lib.impl.spectator.Spectator;
import org.screamingsandals.lib.plugin.event.PluginDisabledEvent;
import org.screamingsandals.lib.plugin.event.PluginEnabledEvent;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.internal.AccessPluginClasses;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@ServiceDependencies(
        dependsOn = {
            BukkitServer.class,
            BukkitCustomPayload.class,
            BukkitEventManager.class,
            BukkitTasker.class,
            BukkitEntityTypeRegistry.class,
            BukkitEntities.class,
            BukkitAttributeTypeRegistry.class,
            BukkitAttributes.class,
            BukkitFireworkEffectTypeRegistry.class,
            BukkitFireworkEffectRegistry.class,
            BukkitEnchantmentRegistry.class,
            BukkitPotionEffectRegistry.class,
            BukkitPotionRegistry.class,
            BukkitEquipmentSlotRegistry.class,
            BukkitItemTypeRegistry.class,
            BukkitBlockRegistry.class,
            BukkitItemBlockIdsRemapper.class,
            BukkitItemStackFactory.class,
            BukkitPlayers.class,
            BukkitLocations.class,
            BukkitBlockPlacements.class,
            BukkitBlockSnapshots.class,
            BukkitDamageTypeRegistry.class,
            BukkitGameModeRegistry.class,
            BukkitInventoryTypeRegistry.class,
            BukkitEntityPoseRegistry.class,
            BukkitDifficultyRegistry.class,
            BukkitDimensionRegistry.class,
            BukkitChunks.class,
            BukkitGameRuleRegistry.class,
            BukkitWeatherRegistry.class,
            BukkitParticleTypeRegistry.class,
            BukkitGameRuleRegistry.class,
            BukkitWorlds.class,
            BukkitContainerFactory.class,
            BukkitGameEventRegistry.class,
            BukkitProfessionRegistry.class
    }
)
@AccessPluginClasses({"ViaVersion", "ProtocolSupport"})
public class BukkitCore extends Core {
    @Getter
    private static SpigotBackend spectatorBackend;
    @Getter
    private static Plugin plugin;

    public BukkitCore(@NotNull Plugin plugin) {
        BukkitCore.plugin = plugin;
        spectatorBackend = new SpigotBackend();
        Spectator.setBackend(spectatorBackend);
    }

    @OnEnable
    public void onEnable() {
        // entity
        if (BukkitFeature.AREA_CLOUD_APPLY_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.AreaEffectCloudApplyEvent.class, AreaEffectCloudApplyEvent.class, BukkitAreaEffectCloudApplyEvent::new);
        }
        if (BukkitFeature.ARROW_BODY_COUNT_CHANGE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.ArrowBodyCountChangeEvent.class, ArrowBodyCountChangeEvent.class, BukkitArrowBodyCountChangeEvent::new);
        }
        if (BukkitFeature.BAT_TOGGLE_SLEEP_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.BatToggleSleepEvent.class, BatToggleSleepEvent.class, BukkitBatToggleSleepEvent::new);
        }
        constructDefaultListener(org.bukkit.event.entity.CreeperPowerEvent.class, CreeperPowerEvent.class, BukkitCreeperPowerEvent::new);
        if (BukkitFeature.ENDER_DRAGON_CHANGE_PHASE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EnderDragonChangePhaseEvent.class, EnderDragonChangePhaseEvent.class, BukkitEnderDragonChangePhaseEvent::new);
        }
        if (BukkitFeature.ENTITY_AIR_CHANGE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityAirChangeEvent.class, EntityAirChangeEvent.class, BukkitEntityAirChangeEvent::new);
        }
        if (BukkitFeature.ENTITY_BREED_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityBreedEvent.class, EntityBreedEvent.class, BukkitEntityBreedEvent::new);
        }
        constructDefaultListener(org.bukkit.event.entity.EntityChangeBlockEvent.class, EntityChangeBlockEvent.class, BukkitEntityChangeBlockEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntityCombustEvent.class, EntityCombustEvent.class, factory(BukkitEntityCombustEvent::new)
                .sub(org.bukkit.event.entity.EntityCombustByBlockEvent.class, BukkitEntityCombustByBlockEvent::new)
                .sub(org.bukkit.event.entity.EntityCombustByEntityEvent.class, BukkitEntityCombustByEntityEvent::new)
        );
        constructDefaultListener(org.bukkit.event.entity.EntityCreatePortalEvent.class, EntityCreatePortalEvent.class, BukkitEntityCreatePortalEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntityDamageEvent.class, EntityDamageEvent.class, factory(BukkitEntityDamageEvent::new)
                .sub(org.bukkit.event.entity.EntityDamageByEntityEvent.class, BukkitEntityDamageByEntityEvent::new)
                .sub(org.bukkit.event.entity.EntityDamageByBlockEvent.class, BukkitEntityDamageByBlockEvent::new)
        );
        constructDefaultListener(org.bukkit.event.entity.EntityDeathEvent.class, EntityDeathEvent.class, factory(BukkitEntityDeathEvent::new)
                .sub(org.bukkit.event.entity.PlayerDeathEvent.class, BukkitPlayerDeathEvent::new)
        );
        if (BukkitFeature.ENTITY_DROP_ITEM_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityDropItemEvent.class, EntityDropItemEvent.class, BukkitEntityDropItemEvent::new);
        }
        if (BukkitFeature.ENTITY_ENTER_BLOCK_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityEnterBlockEvent.class, EntityEnterBlockEvent.class, BukkitEntityEnterBlockEvent::new);
        }
        if (BukkitFeature.ENTITY_ENTER_LOVE_MODE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityEnterLoveModeEvent.class, EntityEnterLoveModeEvent.class, BukkitEntityEnterLoveModeEvent::new);
        }
        if (BukkitFeature.ENTITY_EXHAUSTION_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityExhaustionEvent.class, EntityExhaustionEvent.class, BukkitEntityExhaustionEvent::new);
        }
        constructDefaultListener(org.bukkit.event.entity.EntityExplodeEvent.class, EntityExplodeEvent.class, BukkitEntityExplodeEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntityInteractEvent.class, EntityInteractEvent.class, BukkitEntityInteractEvent::new);
        if (BukkitFeature.ENTITY_PICKUP_ITEM_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityPickupItemEvent.class, EntityPickupItemEvent.class, event -> {
                if (event.getEntity() instanceof Player) {
                    return new BukkitModernPlayerPickupItemEvent(event);
                }
                return new BukkitEntityPickupItemEvent(event);
            });
        } else {
            constructDefaultListener(org.bukkit.event.player.PlayerPickupItemEvent.class, EntityPickupItemEvent.class, BukkitLegacyPlayerPickupItemEvent::new);
        }
        if (BukkitFeature.ENTITY_PLACE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityPlaceEvent.class, EntityPlaceEvent.class, BukkitEntityPlaceEvent::new);
        }

        // EntityTeleportEvent is a weird event, the child has its own HandlerList
        constructDefaultListener(org.bukkit.event.entity.EntityTeleportEvent.class, EntityTeleportEvent.class, BukkitEntityTeleportEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntityPortalEvent.class, EntityTeleportEvent.class, BukkitEntityPortalEvent::new);

        constructDefaultListener(org.bukkit.event.entity.EntityPortalEnterEvent.class, EntityPortalEnterEvent.class, BukkitEntityPortalEnterEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntityPortalExitEvent.class, EntityPortalExitEvent.class, BukkitEntityPortalExitEvent::new);
        if (BukkitFeature.ENTITY_POSE_CHANGE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityPoseChangeEvent.class, EntityPoseChangeEvent.class, BukkitEntityPoseChangeEvent::new);
        }
        if (BukkitFeature.ENTITY_POTION_EFFECT_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityPotionEffectEvent.class, EntityPotionEffectEvent.class, BukkitEntityPotionEffectEvent::new);
        }
        constructDefaultListener(org.bukkit.event.entity.EntityRegainHealthEvent.class, EntityRegainHealthEvent.class, BukkitEntityRegainHealthEvent::new);
        if (BukkitFeature.ENTITY_RESURRECT_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityResurrectEvent.class, EntityResurrectEvent.class, BukkitEntityResurrectEvent::new);
        }
        constructDefaultListener(org.bukkit.event.entity.EntityShootBowEvent.class, EntityShootBowEvent.class, BukkitEntityShootBowEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntitySpawnEvent.class, EntitySpawnEvent.class, factory(BukkitEntitySpawnEvent::new)
                .sub(org.bukkit.event.entity.ItemSpawnEvent.class, BukkitItemSpawnEvent::new)
                .sub(org.bukkit.event.entity.ProjectileLaunchEvent.class, BukkitProjectileLaunchEvent::new)
                .sub(org.bukkit.event.entity.CreatureSpawnEvent.class, BukkitCreatureSpawnEvent::new)
        );
        constructDefaultListener(org.bukkit.event.entity.EntityTameEvent.class, EntityTameEvent.class, BukkitEntityTameEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntityTargetEvent.class, EntityTargetEvent.class, factory(BukkitEntityTargetEvent::new)
                .sub(org.bukkit.event.entity.EntityTargetLivingEntityEvent.class, BukkitEntityTargetLivingEntityEvent::new)
        );
        constructDefaultListener(org.bukkit.event.entity.FoodLevelChangeEvent.class, FoodLevelChangeEvent.class, BukkitFoodLevelChangeEvent::new);
        constructDefaultListener(org.bukkit.event.entity.HorseJumpEvent.class, HorseJumpEvent.class, BukkitHorseJumpEvent::new);
        constructDefaultListener(org.bukkit.event.entity.ItemDespawnEvent.class, ItemDespawnEvent.class, BukkitItemDespawnEvent::new);
        constructDefaultListener(org.bukkit.event.entity.ItemMergeEvent.class, ItemMergeEvent.class, BukkitItemMergeEvent::new);

        // ProjectileHitEvent is a weird event, the child has its own HandlerList
        constructDefaultListener(org.bukkit.event.entity.ProjectileHitEvent.class, ProjectileHitEvent.class, BukkitProjectileHitEvent::new);
        if (BukkitFeature.EXP_BOTTLE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.ExpBottleEvent.class, ProjectileHitEvent.class, BukkitExpBottleEvent::new);
        }

        constructDefaultListener(org.bukkit.event.entity.SheepDyeWoolEvent.class, SheepDyeWoolEvent.class, BukkitSheepDyeWoolEvent::new);
        constructDefaultListener(org.bukkit.event.entity.SheepRegrowWoolEvent.class, SheepRegrowWoolEvent.class, BukkitSheepRegrowWoolEvent::new);
        constructDefaultListener(org.bukkit.event.entity.SlimeSplitEvent.class, SlimeSplitEvent.class, BukkitSlimeSplitEvent::new);
        if (BukkitFeature.STRIDER_TEMPERATURE_CHANGE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.StriderTemperatureChangeEvent.class, StriderTemperatureChangeEvent.class, BukkitStriderTemperatureChangeEvent::new);
        }
        if (BukkitFeature.VILLAGER_ACQUIRE_TRADE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.VillagerAcquireTradeEvent.class, VillagerAcquireTradeEvent.class, BukkitVillagerAcquireTradeEvent::new);
        }
        if (BukkitFeature.VILLAGER_CAREER_CHANGE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.VillagerCareerChangeEvent.class, VillagerCareerChangeEvent.class, BukkitVillagerCareerChangeEvent::new);
        }
        if (BukkitFeature.VILLAGER_REPLENISH_TRADE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.VillagerReplenishTradeEvent.class, VillagerReplenishTradeEvent.class, BukkitVillagerReplenishTradeEvent::new);
        }
        if (BukkitFeature.ENTITY_TOGGLE_GLIDE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityToggleGlideEvent.class, EntityToggleGlideEvent.class, BukkitEntityToggleGlideEvent::new);
        }
        if (BukkitFeature.ENTITY_TOGGLE_SWIM_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.entity.EntityToggleSwimEvent.class, EntityToggleSwimEvent.class, BukkitEntityToggleSwimEvent::new);
        }
        constructDefaultListener(org.bukkit.event.entity.EntityUnleashEvent.class, EntityUnleashEvent.class, factory(BukkitEntityUnleashEvent::new)
                .sub(org.bukkit.event.player.PlayerUnleashEntityEvent.class, BukkitPlayerUnleashEntityEvent::new)
        );
        constructDefaultListener(org.bukkit.event.entity.ExplosionPrimeEvent.class, ExplosionPrimeEvent.class, BukkitExplosionPrimeEvent::new);
        constructDefaultListener(org.bukkit.event.entity.FireworkExplodeEvent.class, FireworkExplodeEvent.class, BukkitFireworkExplodeEvent::new);
        constructDefaultListener(org.bukkit.event.vehicle.VehicleCreateEvent.class, VehicleCreateEvent.class, BukkitVehicleCreateEvent::new);

        // player
        constructDefaultListener(org.bukkit.event.player.AsyncPlayerPreLoginEvent.class, AsyncPlayerPreLoginEvent.class, BukkitAsyncPlayerPreLoginEvent::new);
        constructDefaultListener(org.bukkit.event.player.AsyncPlayerChatEvent.class, PlayerChatEvent.class, BukkitPlayerChatEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerJoinEvent.class, PlayerJoinEvent.class, BukkitPlayerJoinEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerQuitEvent.class, PlayerLeaveEvent.class, BukkitPlayerLeaveEvent::new);
        constructDefaultListener(org.bukkit.event.block.BlockPlaceEvent.class, PlayerBlockPlaceEvent.class, BukkitPlayerBlockPlaceEvent::new);
        constructDefaultListener(org.bukkit.event.block.BlockDamageEvent.class, PlayerBlockDamageEvent.class, BukkitPlayerBlockDamageEvent::new);
        /* we should register this only if someone exactly wants PlayerMoveEvent and not PlayerTeleportEvent */
        new AbstractBukkitEventHandlerFactory<>(org.bukkit.event.player.PlayerMoveEvent.class, PlayerMoveEvent.class, plugin, false, true) {
            @Override
            protected PlayerMoveEvent wrapEvent(@NotNull org.bukkit.event.player.PlayerMoveEvent event, @NotNull EventPriority priority) {
                return new BukkitPlayerMoveEvent(event);
            }
        };
        // although PlayerTeleportEvent extends PlayerMoveEvent, it has its own HandlerList
        constructDefaultListener(org.bukkit.event.player.PlayerTeleportEvent.class, PlayerMoveEvent.class, BukkitPlayerTeleportEvent::new);
        // although PlayerPortalEvent extends PlayerTeleportEvent, it has its own HandlerList
        constructDefaultListener(org.bukkit.event.player.PlayerPortalEvent.class, PlayerMoveEvent.class, BukkitPlayerPortalEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerChangedWorldEvent.class, PlayerWorldChangeEvent.class, BukkitPlayerWorldChangeEvent::new);
        constructDefaultListener(org.bukkit.event.block.SignChangeEvent.class, PlayerUpdateSignEvent.class, BukkitPlayerUpdateSignEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerRespawnEvent.class, PlayerRespawnEvent.class, BukkitPlayerRespawnEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerCommandPreprocessEvent.class, PlayerCommandPreprocessEvent.class, BukkitPlayerCommandPreprocessEvent::new);
        constructDefaultListener(org.bukkit.event.inventory.InventoryClickEvent.class, PlayerInventoryClickEvent.class, factory(BukkitPlayerInventoryClickEvent::new)
                .sub(org.bukkit.event.inventory.CraftItemEvent.class, BukkitPlayerCraftItemEvent::new)
        );
        constructDefaultListener(org.bukkit.event.entity.FoodLevelChangeEvent.class, PlayerFoodLevelChangeEvent.class, BukkitPlayerFoodLevelChangeEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerDropItemEvent.class, PlayerDropItemEvent.class, BukkitPlayerDropItemEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerBedEnterEvent.class, PlayerBedEnterEvent.class, BukkitPlayerBedEnterEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerAnimationEvent.class, PlayerAnimationEvent.class, BukkitPlayerAnimationEvent::new);

        // PlayerInteractEntityEvent is a weird event, each child has its own HandlerList
        constructDefaultListener(org.bukkit.event.player.PlayerInteractEntityEvent.class, PlayerInteractEntityEvent.class, BukkitPlayerInteractEntityEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerInteractAtEntityEvent.class, PlayerInteractEntityEvent.class, BukkitPlayerInteractAtEntityEvent::new);
        if (BukkitFeature.PLAYER_ARMOR_STAND_MANIPULATE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.player.PlayerArmorStandManipulateEvent.class, PlayerInteractEntityEvent.class, BukkitPlayerArmorStandManipulateEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerBedLeaveEvent.class, PlayerBedLeaveEvent.class, BukkitPlayerBedLeaveEvent::new);

        // PlayerBucketEvent is abstract and doesn't have implemented handler list
        constructDefaultListener(org.bukkit.event.player.PlayerBucketEmptyEvent.class, PlayerBucketEvent.class, BukkitPlayerBucketEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerBucketFillEvent.class, PlayerBucketEvent.class, BukkitPlayerBucketEvent::new);

        if (BukkitFeature.PLAYER_COMMAND_SEND_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.player.PlayerCommandSendEvent.class, PlayerCommandSendEvent.class, BukkitPlayerCommandSendEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerEggThrowEvent.class, PlayerEggThrowEvent.class, BukkitPlayerEggThrowEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerExpChangeEvent.class, PlayerExpChangeEvent.class, BukkitPlayerExpChangeEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerFishEvent.class, PlayerFishEvent.class, BukkitPlayerFishEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerGameModeChangeEvent.class, PlayerGameModeChangeEvent.class, BukkitPlayerGameModeChangeEvent::new);
        if (BukkitFeature.PLAYER_HARVEST_BLOCK_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.player.PlayerHarvestBlockEvent.class, PlayerHarvestBlockEvent.class, BukkitPlayerHarvestBlockEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerInteractEvent.class, PlayerInteractEvent.class, BukkitPlayerInteractEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerItemConsumeEvent.class, PlayerItemConsumeEvent.class, BukkitPlayerItemConsumeEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerItemDamageEvent.class, PlayerItemDamageEvent.class, BukkitPlayerItemDamageEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerItemHeldEvent.class, PlayerItemHeldEvent.class, BukkitPlayerItemHeldEvent::new);
        if (BukkitFeature.PLAYER_ITEM_MEND_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.player.PlayerItemMendEvent.class, PlayerItemMendEvent.class, BukkitPlayerItemMendEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerKickEvent.class, PlayerKickEvent.class, BukkitPlayerKickEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerLevelChangeEvent.class, PlayerLevelChangeEvent.class, BukkitPlayerLevelChangeEvent::new);
        if (BukkitFeature.PLAYER_LOCALE_CHANGE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.player.PlayerLocaleChangeEvent.class, PlayerLocaleChangeEvent.class, BukkitPlayerLocaleChangeEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerLoginEvent.class, PlayerLoginEvent.class, BukkitPlayerLoginEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerShearEntityEvent.class, PlayerShearEntityEvent.class, BukkitPlayerShearEntityEvent::new);
        if (BukkitFeature.PLAYER_SWAP_HAND_ITEMS_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.player.PlayerSwapHandItemsEvent.class, PlayerSwapHandItemsEvent.class, BukkitPlayerSwapHandItemsEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerToggleFlightEvent.class, PlayerToggleFlightEvent.class, BukkitPlayerToggleFlightEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerToggleSneakEvent.class, PlayerToggleSneakEvent.class, BukkitPlayerToggleSneakEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerToggleSprintEvent.class, PlayerToggleSprintEvent.class, BukkitPlayerToggleSprintEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerVelocityEvent.class, PlayerVelocityChangeEvent.class, BukkitPlayerVelocityChangeEvent::new);
        constructDefaultListener(org.bukkit.event.inventory.InventoryOpenEvent.class, PlayerInventoryOpenEvent.class, BukkitPlayerInventoryOpenEvent::new);
        constructDefaultListener(org.bukkit.event.inventory.InventoryCloseEvent.class, PlayerInventoryCloseEvent.class, BukkitPlayerInventoryCloseEvent::new);

        // block
        constructDefaultListener(org.bukkit.event.block.BlockBurnEvent.class, BlockBurnEvent.class, BukkitBlockBurnEvent::new);
        if (BukkitFeature.BLOCK_COOK_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.block.BlockCookEvent.class, BlockCookEvent.class, BukkitBlockCookEvent::new);
        }
        constructDefaultListener(org.bukkit.event.block.BlockDispenseEvent.class, BlockDispenseEvent.class, BukkitBlockDispenseEvent::new);
        if (BukkitFeature.BLOCK_DROP_ITEM_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.block.BlockDropItemEvent.class, BlockDropItemEvent.class, BukkitBlockDropItemEvent::new);
        }
        constructDefaultListener(org.bukkit.event.block.BlockExpEvent.class, BlockExperienceEvent.class, factory(BukkitBlockExperienceEvent::new)
                .sub(org.bukkit.event.block.BlockBreakEvent.class, BukkitPlayerBlockBreakEvent::new)
        );
        constructDefaultListener(org.bukkit.event.block.BlockExplodeEvent.class, BlockExplodeEvent.class, BukkitBlockExplodeEvent::new);
        constructDefaultListener(org.bukkit.event.block.BlockFadeEvent.class, BlockFadeEvent.class, BukkitBlockFadeEvent::new);
        if (BukkitFeature.BLOCK_FERTILIZE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.block.BlockFertilizeEvent.class, BlockFertilizeEvent.class, BukkitBlockFertilizeEvent::new);
        }

        // children of BlockGrowEvent have their own HandlerList's (Bukkit is retarded, change my mind)
        constructDefaultListener(org.bukkit.event.block.BlockGrowEvent.class, BlockGrowEvent.class, BukkitBlockGrowEvent::new);
        constructDefaultListener(org.bukkit.event.block.BlockFormEvent.class, BlockGrowEvent.class, factory(BukkitBlockFormEvent::new)
                .sub(org.bukkit.event.block.EntityBlockFormEvent.class, BukkitBlockFormedByEntityEvent::new)
        );
        constructDefaultListener(org.bukkit.event.block.BlockSpreadEvent.class, BlockGrowEvent.class, BukkitBlockSpreadEvent::new);

        constructDefaultListener(org.bukkit.event.block.BlockFromToEvent.class, BlockFromToEvent.class, BukkitBlockFromToEvent::new);
        constructDefaultListener(org.bukkit.event.block.BlockIgniteEvent.class, BlockIgniteEvent.class, BukkitBlockIgniteEvent::new);
        constructDefaultListener(org.bukkit.event.block.BlockPhysicsEvent.class, BlockPhysicsEvent.class, BukkitBlockPhysicsEvent::new);
        constructDefaultListener(org.bukkit.event.block.BlockRedstoneEvent.class, RedstoneEvent.class, BukkitRedstoneEvent::new);
        constructDefaultListener(org.bukkit.event.block.LeavesDecayEvent.class, LeavesDecayEvent.class, BukkitLeavesDecayEvent::new);

        // BlockPistonEvent is abstract and doesn't have implemented handler list
        constructDefaultListener(org.bukkit.event.block.BlockPistonExtendEvent.class, BlockPistonEvent.class, BukkitBlockPistonExtendEvent::new);
        constructDefaultListener(org.bukkit.event.block.BlockPistonRetractEvent.class, BlockPistonEvent.class, BukkitBlockPistonRetractEvent::new);

        if (BukkitFeature.BLOCK_SHEAR_ENTITY_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.block.BlockShearEntityEvent.class, BlockShearEntityEvent.class, BukkitBlockShearEntityEvent::new);
        }
        if (BukkitFeature.CAULDRON_LEVEL_CHANGE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.block.CauldronLevelChangeEvent.class, CauldronLevelChangeEvent.class, BukkitCauldronLevelChangeEvent::new);
        }
        if (BukkitFeature.FLUID_LEVEL_CHANGE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.block.FluidLevelChangeEvent.class, FluidLevelChangeEvent.class, BukkitFluidLevelChangeEvent::new);
        }
        if (BukkitFeature.MOISTURE_CHANGE_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.block.MoistureChangeEvent.class, MoistureChangeEvent.class, BukkitMoistureChangeEvent::new);
        }
        constructDefaultListener(org.bukkit.event.world.StructureGrowEvent.class, PlantGrowEvent.class, BukkitPlantGrowEvent::new);
        if (BukkitFeature.SPONGE_ABSORB_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.block.SpongeAbsorbEvent.class, SpongeAbsorbEvent.class, BukkitSpongeAbsorbEvent::new);
        }
        if (BukkitFeature.BLOCK_RECEIVE_GAME_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.block.BlockReceiveGameEvent.class, BlockReceivedGameEvent.class, BukkitBlockReceivedGameEvent::new);
        }

        // world
        constructDefaultListener(org.bukkit.event.world.SpawnChangeEvent.class, SpawnChangeEvent.class, BukkitSpawnChangeEvent::new);
        if (BukkitFeature.TIME_SKIP_EVENT.isSupported()) {
            constructDefaultListener(org.bukkit.event.world.TimeSkipEvent.class, TimeSkipEvent.class, BukkitTimeSkipEvent::new);
        }
        constructDefaultListener(org.bukkit.event.world.WorldInitEvent.class, WorldInitEvent.class, BukkitWorldInitEvent::new);
        constructDefaultListener(org.bukkit.event.world.WorldLoadEvent.class, WorldLoadEvent.class, BukkitWorldLoadEvent::new);
        constructDefaultListener(org.bukkit.event.world.WorldSaveEvent.class, WorldSaveEvent.class, BukkitWorldSaveEvent::new);
        constructDefaultListener(org.bukkit.event.world.WorldUnloadEvent.class, WorldUnloadEvent.class, BukkitWorldUnloadEvent::new);

        // chunk
        constructDefaultListener(org.bukkit.event.world.ChunkLoadEvent.class, ChunkLoadEvent.class, BukkitChunkLoadEvent::new);
        constructDefaultListener(org.bukkit.event.world.ChunkPopulateEvent.class, ChunkPopulateEvent.class, BukkitChunkPopulateEvent::new);
        constructDefaultListener(org.bukkit.event.world.ChunkUnloadEvent.class, ChunkUnloadEvent.class, BukkitChunkUnloadEvent::new);

        // plugins
        constructDefaultListener(org.bukkit.event.server.PluginEnableEvent.class, PluginEnabledEvent.class, event -> () -> new BukkitPlugin(event.getPlugin()));
        constructDefaultListener(org.bukkit.event.server.PluginDisableEvent.class, PluginDisabledEvent.class, event -> () -> new BukkitPlugin(event.getPlugin()));
    }

    /**
     * @param bukkitEvent    the bukkit event
     * @param screamingEvent screaming event class, must be the interface from core module!!! (if it's a child event, you should specify the parent here)
     * @param function       which returns the constructed screaming event
     */
    private <S extends Event, B extends org.bukkit.event.Event> void constructDefaultListener(Class<B> bukkitEvent, Class<S> screamingEvent, Function<B, ? extends S> function) {
        new AbstractBukkitEventHandlerFactory<>(bukkitEvent, screamingEvent, plugin) {
            @Override
            protected S wrapEvent(@NotNull B event, @NotNull EventPriority priority) {
                return function.apply(event);
            }
        };
    }

    /**
     * @param bukkitEvent    the bukkit event
     * @param screamingEvent screaming event class, must be the abstract class from core module!!!
     * @param factory        which constructs screaming event
     */
    private <S extends Event, B extends org.bukkit.event.Event> void constructDefaultListener(Class<B> bukkitEvent, Class<S> screamingEvent, EventFactory<? extends S, B> factory) {
        constructDefaultListener(bukkitEvent, screamingEvent, factory.finish());
    }

    private static <S extends Event, B extends org.bukkit.event.Event> EventFactory<S, B> factory(Function<B, S> function) {
        return new EventFactory<>(function);
    }

    @Data
    private static class EventFactory<S extends Event, B extends org.bukkit.event.Event> {
        private final Function<B, S> defaultOne;
        private final List<Map.Entry<Class<?>, Function<? extends B, S>>> customSubEvents = new ArrayList<>();

        private <T extends B> EventFactory<S, B> sub(Class<T> clazz, Function<T, S> func) {
            customSubEvents.add(Map.entry(clazz, func));
            return this;
        }

        private Function<B, S> finish() {
            Collections.reverse(customSubEvents); // the last registered are the one that should run first because why not
            return event -> {
                for (var entry : customSubEvents) {
                    if (entry.getKey().isInstance(event)) {
                        return ((Function<B, S>) entry.getValue()).apply(event);
                    }
                }
                return defaultOne.apply(event);
            };
        }
    }
}
