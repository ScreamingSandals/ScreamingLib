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

package org.screamingsandals.lib.bukkit;

import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.event.block.*;
import org.screamingsandals.lib.bukkit.event.chunk.*;
import org.screamingsandals.lib.bukkit.event.entity.*;
import org.screamingsandals.lib.bukkit.event.player.*;
import org.screamingsandals.lib.bukkit.event.world.*;
import org.screamingsandals.lib.bukkit.spectator.SpigotBackend;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.event.block.*;
import org.screamingsandals.lib.event.block.BlockBurnEvent;
import org.screamingsandals.lib.event.block.BlockCookEvent;
import org.screamingsandals.lib.event.block.BlockDispenseEvent;
import org.screamingsandals.lib.event.block.BlockDropItemEvent;
import org.screamingsandals.lib.event.block.BlockExplodeEvent;
import org.screamingsandals.lib.event.block.BlockFadeEvent;
import org.screamingsandals.lib.event.block.BlockFertilizeEvent;
import org.screamingsandals.lib.event.block.BlockFromToEvent;
import org.screamingsandals.lib.event.block.BlockGrowEvent;
import org.screamingsandals.lib.event.block.BlockIgniteEvent;
import org.screamingsandals.lib.event.block.BlockPhysicsEvent;
import org.screamingsandals.lib.event.block.BlockPistonEvent;
import org.screamingsandals.lib.event.block.BlockShearEntityEvent;
import org.screamingsandals.lib.event.block.CauldronLevelChangeEvent;
import org.screamingsandals.lib.event.block.FluidLevelChangeEvent;
import org.screamingsandals.lib.event.block.LeavesDecayEvent;
import org.screamingsandals.lib.event.block.MoistureChangeEvent;
import org.screamingsandals.lib.event.chunk.ChunkLoadEvent;
import org.screamingsandals.lib.event.chunk.ChunkPopulateEvent;
import org.screamingsandals.lib.event.chunk.ChunkUnloadEvent;
import org.screamingsandals.lib.event.entity.*;
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
import org.screamingsandals.lib.event.entity.VillagerAcquireTradeEvent;
import org.screamingsandals.lib.event.entity.VillagerCareerChangeEvent;
import org.screamingsandals.lib.event.entity.VillagerReplenishTradeEvent;
import org.screamingsandals.lib.event.player.*;
import org.screamingsandals.lib.event.player.AsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.event.player.PlayerAnimationEvent;
import org.screamingsandals.lib.event.player.PlayerBedEnterEvent;
import org.screamingsandals.lib.event.player.PlayerBedLeaveEvent;
import org.screamingsandals.lib.event.player.PlayerBucketEvent;
import org.screamingsandals.lib.event.player.PlayerChatEvent;
import org.screamingsandals.lib.event.player.PlayerCommandPreprocessEvent;
import org.screamingsandals.lib.event.player.PlayerCommandSendEvent;
import org.screamingsandals.lib.event.player.PlayerDropItemEvent;
import org.screamingsandals.lib.event.player.PlayerEggThrowEvent;
import org.screamingsandals.lib.event.player.PlayerExpChangeEvent;
import org.screamingsandals.lib.event.player.PlayerFishEvent;
import org.screamingsandals.lib.event.player.PlayerGameModeChangeEvent;
import org.screamingsandals.lib.event.player.PlayerHarvestBlockEvent;
import org.screamingsandals.lib.event.player.PlayerInteractEntityEvent;
import org.screamingsandals.lib.event.player.PlayerInteractEvent;
import org.screamingsandals.lib.event.player.PlayerItemConsumeEvent;
import org.screamingsandals.lib.event.player.PlayerItemDamageEvent;
import org.screamingsandals.lib.event.player.PlayerItemHeldEvent;
import org.screamingsandals.lib.event.player.PlayerItemMendEvent;
import org.screamingsandals.lib.event.player.PlayerJoinEvent;
import org.screamingsandals.lib.event.player.PlayerKickEvent;
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
import org.screamingsandals.lib.event.world.*;
import org.screamingsandals.lib.event.world.SpawnChangeEvent;
import org.screamingsandals.lib.event.world.SpongeAbsorbEvent;
import org.screamingsandals.lib.event.world.TimeSkipEvent;
import org.screamingsandals.lib.event.world.WorldInitEvent;
import org.screamingsandals.lib.event.world.WorldLoadEvent;
import org.screamingsandals.lib.event.world.WorldSaveEvent;
import org.screamingsandals.lib.event.world.WorldUnloadEvent;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.*;
import java.util.function.Function;

import static org.screamingsandals.lib.utils.reflect.Reflect.has;

@Service
public class BukkitCore extends Core {
    @Getter
    private static SpigotBackend spectatorBackend;
    @Getter
    private static Plugin plugin;

    public BukkitCore(Plugin plugin) {
        BukkitCore.plugin = plugin;
        spectatorBackend = new SpigotBackend();
        Spectator.setBackend(spectatorBackend);
    }

    @OnEnable
    public void onEnable() {
        // entity
        if (has("org.bukkit.event.entity.AreaCloudApplyEvent")) {
            constructDefaultListener(org.bukkit.event.entity.AreaEffectCloudApplyEvent.class, AreaEffectCloudApplyEvent.class, BukkitAreaEffectCloudApplyEvent::new);
        }
        if (has("org.bukkit.event.entity.ArrowBodyCountChangeEvent")) {
            constructDefaultListener(org.bukkit.event.entity.ArrowBodyCountChangeEvent.class, ArrowBodyCountChangeEvent.class, BukkitArrowBodyCountChangeEvent::new);
        }
        if (has("org.bukkit.event.entity.BatToggleSleepEvent")) {
            constructDefaultListener(org.bukkit.event.entity.BatToggleSleepEvent.class, BatToggleSleepEvent.class, BukkitBatToggleSleepEvent::new);
        }
        constructDefaultListener(org.bukkit.event.entity.CreeperPowerEvent.class, CreeperPowerEvent.class, BukkitCreeperPowerEvent::new);
        if (has("org.bukkit.event.entity.EnderDragonChangePhaseEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EnderDragonChangePhaseEvent.class, EnderDragonChangePhaseEvent.class, BukkitEnderDragonChangePhaseEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityAirChangeEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityAirChangeEvent.class, EntityAirChangeEvent.class, BukkitEntityAirChangeEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityBreedEvent")) {
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
        if (has("org.bukkit.event.entity.EntityDropItemEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityDropItemEvent.class, EntityDropItemEvent.class, BukkitEntityDropItemEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityEnterBlockEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityEnterBlockEvent.class, EntityEnterBlockEvent.class, BukkitEntityEnterBlockEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityEnterLoveModeEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityEnterLoveModeEvent.class, EntityEnterLoveModeEvent.class, BukkitEntityEnterLoveModeEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityExhaustionEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityExhaustionEvent.class, EntityExhaustionEvent.class, BukkitEntityExhaustionEvent::new);
        }
        constructDefaultListener(org.bukkit.event.entity.EntityExplodeEvent.class, EntityExplodeEvent.class, BukkitEntityExplodeEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntityInteractEvent.class, EntityInteractEvent.class, BukkitEntityInteractEvent::new);
        if (has("org.bukkit.event.entity.EntityPickupItemEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityPickupItemEvent.class, EntityPickupItemEvent.class, event -> {
                if (event.getEntity() instanceof Player) {
                    return new BukkitModernPlayerPickupItemEvent(event);
                }
                return new BukkitEntityPickupItemEvent(event);
            });
        } else {
            constructDefaultListener(org.bukkit.event.player.PlayerPickupItemEvent.class, EntityPickupItemEvent.class, BukkitLegacyPlayerPickupItemEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityPlaceEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityPlaceEvent.class, EntityPlaceEvent.class, BukkitEntityPlaceEvent::new);
        }

        // EntityTeleportEvent is a weird event, the child has its own HandlerList
        constructDefaultListener(org.bukkit.event.entity.EntityTeleportEvent.class, EntityTeleportEvent.class, BukkitEntityTeleportEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntityPortalEvent.class, EntityTeleportEvent.class, BukkitEntityPortalEvent::new);

        constructDefaultListener(org.bukkit.event.entity.EntityPortalEnterEvent.class, EntityPortalEnterEvent.class, BukkitEntityPortalEnterEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntityPortalExitEvent.class, EntityPortalExitEvent.class, BukkitEntityPortalExitEvent::new);
        if (has("org.bukkit.event.entity.EntityPoseChangeEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityPoseChangeEvent.class, EntityPoseChangeEvent.class, BukkitEntityPoseChangeEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityPotionEffectEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityPotionEffectEvent.class, EntityPotionEffectEvent.class, BukkitEntityPotionEffectEvent::new);
        }
        constructDefaultListener(org.bukkit.event.entity.EntityRegainHealthEvent.class, EntityRegainHealthEvent.class, BukkitEntityRegainHealthEvent::new);
        if (has("org.bukkit.event.entity.EntityResurrectEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityResurrectEvent.class, EntityResurrectEvent.class, BukkitEntityResurrectEvent::new);
        }
        constructDefaultListener(org.bukkit.event.entity.EntityShootBowEvent.class, EntityShootBowEvent.class, BukkitEntityShootBowEvent::new);
        constructDefaultListener(org.bukkit.event.entity.EntitySpawnEvent.class, EntitySpawnEvent.class, factory(BukkitEntitySpawnEvent::new)
                .sub(org.bukkit.event.entity.ItemSpawnEvent.class, BukkitItemSpawnEvent::new)
                .sub(org.bukkit.event.entity.ProjectileLaunchEvent.class, projectileLaunchEvent -> {
                    if (projectileLaunchEvent.getEntity().getShooter() instanceof Player) {
                        return new BukkitPlayerProjectileLaunchEvent(projectileLaunchEvent);
                    }
                    return new BukkitProjectileLaunchEvent(projectileLaunchEvent);
                })
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
        if (has("org.bukkit.event.entity.ExpBottleEvent")) {
            constructDefaultListener(org.bukkit.event.entity.ExpBottleEvent.class, ProjectileHitEvent.class, BukkitExpBottleEvent::new);
        }

        constructDefaultListener(org.bukkit.event.entity.SheepDyeWoolEvent.class, SheepDyeWoolEvent.class, BukkitSheepDyeWoolEvent::new);
        constructDefaultListener(org.bukkit.event.entity.SheepRegrowWoolEvent.class, SheepRegrowWoolEvent.class, BukkitSheepRegrowWoolEvent::new);
        constructDefaultListener(org.bukkit.event.entity.SlimeSplitEvent.class, SlimeSplitEvent.class, BukkitSlimeSplitEvent::new);
        if (has("org.bukkit.event.entity.StriderTemperatureChangeEvent")) {
            constructDefaultListener(org.bukkit.event.entity.StriderTemperatureChangeEvent.class, StriderTemperatureChangeEvent.class, BukkitStriderTemperatureChangeEvent::new);
        }
        if (has("org.bukkit.event.entity.VillagerAcquireTradeEvent")) {
            constructDefaultListener(org.bukkit.event.entity.VillagerAcquireTradeEvent.class, VillagerAcquireTradeEvent.class, BukkitVillagerAcquireTradeEvent::new);
        }
        if (has("org.bukkit.event.entity.VillagerCareerChangeEvent")) {
            constructDefaultListener(org.bukkit.event.entity.VillagerCareerChangeEvent.class, VillagerCareerChangeEvent.class, BukkitVillagerCareerChangeEvent::new);
        }
        if (has("org.bukkit.event.entity.VillagerReplenishTradeEvent")) {
            constructDefaultListener(org.bukkit.event.entity.VillagerReplenishTradeEvent.class, VillagerReplenishTradeEvent.class, BukkitVillagerReplenishTradeEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityToggleGlideEvent")) {
            constructDefaultListener(org.bukkit.event.entity.EntityToggleGlideEvent.class, EntityToggleGlideEvent.class, BukkitEntityToggleGlideEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityToggleSwimEvent")) {
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
        if (has("org.bukkit.event.player.PlayerArmorStandManipulateEvent")) {
            constructDefaultListener(org.bukkit.event.player.PlayerArmorStandManipulateEvent.class, PlayerInteractEntityEvent.class, BukkitPlayerArmorStandManipulateEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerBedLeaveEvent.class, PlayerBedLeaveEvent.class, BukkitPlayerBedLeaveEvent::new);

        // PlayerBucketEvent is abstract and doesn't have implemented handler list
        constructDefaultListener(org.bukkit.event.player.PlayerBucketEmptyEvent.class, PlayerBucketEvent.class, BukkitPlayerBucketEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerBucketFillEvent.class, PlayerBucketEvent.class, BukkitPlayerBucketEvent::new);

        if (has("org.bukkit.event.player.PlayerCommandSendEvent")) {
            constructDefaultListener(org.bukkit.event.player.PlayerCommandSendEvent.class, PlayerCommandSendEvent.class, BukkitPlayerCommandSendEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerEggThrowEvent.class, PlayerEggThrowEvent.class, BukkitPlayerEggThrowEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerExpChangeEvent.class, PlayerExpChangeEvent.class, BukkitPlayerExpChangeEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerFishEvent.class, PlayerFishEvent.class, BukkitPlayerFishEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerGameModeChangeEvent.class, PlayerGameModeChangeEvent.class, BukkitPlayerGameModeChangeEvent::new);
        if (has("org.bukkit.event.player.PlayerHarvestBlockEvent")) {
            constructDefaultListener(org.bukkit.event.player.PlayerHarvestBlockEvent.class, PlayerHarvestBlockEvent.class, BukkitPlayerHarvestBlockEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerInteractEvent.class, PlayerInteractEvent.class, BukkitPlayerInteractEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerItemConsumeEvent.class, PlayerItemConsumeEvent.class, BukkitPlayerItemConsumeEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerItemDamageEvent.class, PlayerItemDamageEvent.class, BukkitPlayerItemDamageEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerItemHeldEvent.class, PlayerItemHeldEvent.class, BukkitPlayerItemHeldEvent::new);
        if (has("org.bukkit.event.player.PlayerItemMendEvent")) {
            constructDefaultListener(org.bukkit.event.player.PlayerItemMendEvent.class, PlayerItemMendEvent.class, BukkitPlayerItemMendEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerKickEvent.class, PlayerKickEvent.class, BukkitPlayerKickEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerLevelChangeEvent.class, PlayerLevelChangeEvent.class, BukkitPlayerLevelChangeEvent::new);
        if (has("org.bukkit.event.player.PlayerLocaleChangeEvent")) {
            constructDefaultListener(org.bukkit.event.player.PlayerLocaleChangeEvent.class, PlayerLocaleChangeEvent.class, BukkitPlayerLocaleChangeEvent::new);
        }
        constructDefaultListener(org.bukkit.event.player.PlayerLoginEvent.class, PlayerLoginEvent.class, BukkitPlayerLoginEvent::new);
        constructDefaultListener(org.bukkit.event.player.PlayerShearEntityEvent.class, PlayerShearEntityEvent.class, BukkitPlayerShearEntityEvent::new);
        if (has("org.bukkit.event.player.PlayerSwapHandItemsEvent")) {
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
        if (has("org.bukkit.event.block.BlockCookEvent")) {
            constructDefaultListener(org.bukkit.event.block.BlockCookEvent.class, BlockCookEvent.class, BukkitBlockCookEvent::new);
        }
        constructDefaultListener(org.bukkit.event.block.BlockDispenseEvent.class, BlockDispenseEvent.class, BukkitBlockDispenseEvent::new);
        if (has("org.bukkit.event.block.BlockDropItemEvent")) {
            constructDefaultListener(org.bukkit.event.block.BlockDropItemEvent.class, BlockDropItemEvent.class, BukkitBlockDropItemEvent::new);
        }
        constructDefaultListener(org.bukkit.event.block.BlockExpEvent.class, BlockExperienceEvent.class, factory(BukkitBlockExperienceEvent::new)
                .sub(org.bukkit.event.block.BlockBreakEvent.class, BukkitPlayerBlockBreakEvent::new)
        );
        constructDefaultListener(org.bukkit.event.block.BlockExplodeEvent.class, BlockExplodeEvent.class, BukkitBlockExplodeEvent::new);
        constructDefaultListener(org.bukkit.event.block.BlockFadeEvent.class, BlockFadeEvent.class, BukkitBlockFadeEvent::new);
        if (has("org.bukkit.event.block.BlockFertilizeEvent")) {
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

        if (has("org.bukkit.event.block.BlockShearEntityEvent")) {
            constructDefaultListener(org.bukkit.event.block.BlockShearEntityEvent.class, BlockShearEntityEvent.class, BukkitBlockShearEntityEvent::new);
        }
        if (has("org.bukkit.event.block.CauldronLevelChangeEvent")) {
            constructDefaultListener(org.bukkit.event.block.CauldronLevelChangeEvent.class, CauldronLevelChangeEvent.class, BukkitCauldronLevelChangeEvent::new);
        }
        if (has("org.bukkit.event.block.FluidLevelChangeEvent")) {
            constructDefaultListener(org.bukkit.event.block.FluidLevelChangeEvent.class, FluidLevelChangeEvent.class, BukkitFluidLevelChangeEvent::new);
        }
        if (has("org.bukkit.event.block.MoistureChangeEvent")) {
            constructDefaultListener(org.bukkit.event.block.MoistureChangeEvent.class, MoistureChangeEvent.class, BukkitMoistureChangeEvent::new);
        }
        constructDefaultListener(org.bukkit.event.world.StructureGrowEvent.class, PlantGrowEvent.class, BukkitPlantGrowEvent::new);
        if (has("org.bukkit.event.block.SpongeAbsorbEvent")) {
            constructDefaultListener(org.bukkit.event.block.SpongeAbsorbEvent.class, SpongeAbsorbEvent.class, BukkitSpongeAbsorbEvent::new);
        }
        if (has("org.bukkit.event.block.BlockReceiveGameEvent")) {
            constructDefaultListener(org.bukkit.event.block.BlockReceiveGameEvent.class, BlockReceivedGameEvent.class, BukkitBlockReceivedGameEvent::new);
        }

        // world
        constructDefaultListener(org.bukkit.event.world.SpawnChangeEvent.class, SpawnChangeEvent.class, BukkitSpawnChangeEvent::new);
        if (has("org.bukkit.event.world.TimeSkipEvent")) {
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
    }

    /**
     * @param bukkitEvent the bukkit event
     * @param screamingEvent screaming event class, must be the interface from core module!!! (if it's a child event, you should specify the parent here)
     * @param function which returns the constructed screaming event
     */
    private <S extends SEvent, B extends Event> void constructDefaultListener(Class<B> bukkitEvent, Class<S> screamingEvent, Function<B, ? extends S> function) {
        new AbstractBukkitEventHandlerFactory<>(bukkitEvent, screamingEvent, plugin) {
            @Override
            protected S wrapEvent(@NotNull B event, @NotNull EventPriority priority) {
                return function.apply(event);
            }
        };
    }

    /**
     * @param bukkitEvent the bukkit event
     * @param screamingEvent screaming event class, must be the abstract class from core module!!!
     * @param factory which constructs screaming event
     */
    private <S extends SEvent, B extends Event> void constructDefaultListener(Class<B> bukkitEvent, Class<S> screamingEvent, EventFactory<? extends S, B> factory) {
        constructDefaultListener(bukkitEvent, screamingEvent, factory.finish());
    }

    private static <S extends SEvent, B extends Event> EventFactory<S, B> factory(Function<B, S> function) {
        return new EventFactory<>(function);
    }

    @Data
    private static class EventFactory<S extends SEvent, B extends Event>  {
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
                        return ((Function<B,S>) entry.getValue()).apply(event);
                    }
                }
                return defaultOne.apply(event);
            };
        }
    }
}
