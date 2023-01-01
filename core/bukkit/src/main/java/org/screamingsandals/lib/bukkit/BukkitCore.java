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
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.world.*;
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
import org.screamingsandals.lib.event.chunk.*;
import org.screamingsandals.lib.event.entity.*;
import org.screamingsandals.lib.event.player.*;
import org.screamingsandals.lib.event.world.*;
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
        constructDefaultListener(AreaEffectCloudApplyEvent.class, SAreaEffectCloudApplyEvent.class, SBukkitAreaEffectCloudApplyEvent::new);
        if (has("org.bukkit.event.entity.ArrowBodyCountChangeEvent")) {
            constructDefaultListener(ArrowBodyCountChangeEvent.class, SArrowBodyCountChangeEvent.class, SBukkitArrowBodyCountChangeEvent::new);
        }
        if (has("org.bukkit.event.entity.BatToggleSleepEvent")) {
            constructDefaultListener(BatToggleSleepEvent.class, SBatToggleSleepEvent.class, SBukkitBatToggleSleepEvent::new);
        }
        constructDefaultListener(CreeperPowerEvent.class, SCreeperPowerEvent.class, SBukkitCreeperPowerEvent::new);
        constructDefaultListener(EnderDragonChangePhaseEvent.class, SEnderDragonChangePhaseEvent.class, SBukkitEnderDragonChangePhaseEvent::new);
        if (has("org.bukkit.event.entity.EntityAirChangeEvent")) {
            constructDefaultListener(EntityAirChangeEvent.class, SEntityAirChangeEvent.class, SBukkitEntityAirChangeEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityBreedEvent")) {
            constructDefaultListener(EntityBreedEvent.class, SEntityBreedEvent.class, SBukkitEntityBreedEvent::new);
        }
        constructDefaultListener(EntityChangeBlockEvent.class, SEntityChangeBlockEvent.class, SBukkitEntityChangeBlockEvent::new);
        constructDefaultListener(EntityCombustEvent.class, SEntityCombustEvent.class, factory(SBukkitEntityCombustEvent::new)
                .sub(EntityCombustByBlockEvent.class, SBukkitEntityCombustByBlockEvent::new)
                .sub(EntityCombustByEntityEvent.class, SBukkitEntityCombustByEntityEvent::new)
        );
        constructDefaultListener(EntityCreatePortalEvent.class, SEntityCreatePortalEvent.class, SBukkitEntityCreatePortalEvent::new);
        constructDefaultListener(EntityDamageEvent.class, SEntityDamageEvent.class, factory(SBukkitEntityDamageEvent::new)
                .sub(EntityDamageByEntityEvent.class, SBukkitEntityDamageByEntityEvent::new)
                .sub(EntityDamageByBlockEvent.class, SBukkitEntityDamageByBlockEvent::new)
        );
        constructDefaultListener(EntityDeathEvent.class, SEntityDeathEvent.class, factory(SBukkitEntityDeathEvent::new)
                .sub(PlayerDeathEvent.class, SBukkitPlayerDeathEvent::new)
        );
        if (has("org.bukkit.event.entity.EntityDropItemEvent")) {
            constructDefaultListener(EntityDropItemEvent.class, SEntityDropItemEvent.class, SBukkitEntityDropItemEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityEnterBlockEvent")) {
            constructDefaultListener(EntityEnterBlockEvent.class, SEntityEnterBlockEvent.class, SBukkitEntityEnterBlockEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityEnterLoveModeEvent")) {
            constructDefaultListener(EntityEnterLoveModeEvent.class, SEntityEnterLoveModeEvent.class, SBukkitEntityEnterLoveModeEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityExhaustionEvent")) {
            constructDefaultListener(EntityExhaustionEvent.class, SEntityExhaustionEvent.class, SBukkitEntityExhaustionEvent::new);
        }
        constructDefaultListener(EntityExplodeEvent.class, SEntityExplodeEvent.class, SBukkitEntityExplodeEvent::new);
        constructDefaultListener(EntityInteractEvent.class, SEntityInteractEvent.class, SBukkitEntityInteractEvent::new);
        if (has("org.bukkit.event.entity.EntityPickupItemEvent")) {
            constructDefaultListener(EntityPickupItemEvent.class, SEntityPickupItemEvent.class, event -> {
                if (event.getEntity() instanceof Player) {
                    return new SBukkitModernPlayerPickupItemEvent(event);
                }
                return new SBukkitEntityPickupItemEvent(event);
            });
        } else {
            constructDefaultListener(PlayerPickupItemEvent.class, SEntityPickupItemEvent.class, SBukkitLegacyPlayerPickupItemEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityPlaceEvent")) {
            constructDefaultListener(EntityPlaceEvent.class, SEntityPlaceEvent.class, SBukkitEntityPlaceEvent::new);
        }

        // EntityTeleportEvent is a weird event, the child has its own HandlerList
        constructDefaultListener(EntityTeleportEvent.class, SEntityTeleportEvent.class, SBukkitEntityTeleportEvent::new);
        constructDefaultListener(EntityPortalEvent.class, SEntityTeleportEvent.class, SBukkitEntityPortalEvent::new);

        constructDefaultListener(EntityPortalEnterEvent.class, SEntityPortalEnterEvent.class, SBukkitEntityPortalEnterEvent::new);
        constructDefaultListener(EntityPortalExitEvent.class, SEntityPortalExitEvent.class, SBukkitEntityPortalExitEvent::new);
        if (has("org.bukkit.event.entity.EntityPoseChangeEvent")) {
            constructDefaultListener(EntityPoseChangeEvent.class, SEntityPoseChangeEvent.class, SBukkitEntityPoseChangeEvent::new);
        }
        if (has("org.bukkit.event.entity.EntityPotionEffectEvent")) {
            constructDefaultListener(EntityPotionEffectEvent.class, SEntityPotionEffectEvent.class, SBukkitEntityPotionEffectEvent::new);
        }
        constructDefaultListener(EntityRegainHealthEvent.class, SEntityRegainHealthEvent.class, SBukkitEntityRegainHealthEvent::new);
        if (has("org.bukkit.event.entity.EntityResurrectEvent")) {
            constructDefaultListener(EntityResurrectEvent.class, SEntityResurrectEvent.class, SBukkitEntityResurrectEvent::new);
        }
        constructDefaultListener(EntityShootBowEvent.class, SEntityShootBowEvent.class, SBukkitEntityShootBowEvent::new);
        constructDefaultListener(EntitySpawnEvent.class, SEntitySpawnEvent.class, factory(SBukkitEntitySpawnEvent::new)
                .sub(ItemSpawnEvent.class, SBukkitItemSpawnEvent::new)
                .sub(ProjectileLaunchEvent.class, projectileLaunchEvent -> {
                    if (projectileLaunchEvent.getEntity().getShooter() instanceof Player) {
                        return new SBukkitPlayerProjectileLaunchEvent(projectileLaunchEvent);
                    }
                    return new SBukkitProjectileLaunchEvent(projectileLaunchEvent);
                })
                .sub(CreatureSpawnEvent.class, SBukkitCreatureSpawnEvent::new)
        );
        constructDefaultListener(EntityTameEvent.class, SEntityTameEvent.class, SBukkitEntityTameEvent::new);
        constructDefaultListener(EntityTargetEvent.class, SEntityTargetEvent.class, factory(SBukkitEntityTargetEvent::new)
                .sub(EntityTargetLivingEntityEvent.class, SBukkitEntityTargetLivingEntityEvent::new)
        );
        constructDefaultListener(FoodLevelChangeEvent.class, SFoodLevelChangeEvent.class, SBukkitFoodLevelChangeEvent::new);
        constructDefaultListener(HorseJumpEvent.class, SHorseJumpEvent.class, SBukkitHorseJumpEvent::new);
        constructDefaultListener(ItemDespawnEvent.class, SItemDespawnEvent.class, SBukkitItemDespawnEvent::new);
        constructDefaultListener(ItemMergeEvent.class, SItemMergeEvent.class, SBukkitItemMergeEvent::new);

        // ProjectileHitEvent is a weird event, the child has its own HandlerList
        constructDefaultListener(ProjectileHitEvent.class, SProjectileHitEvent.class, SBukkitProjectileHitEvent::new);
        if (has("org.bukkit.event.entity.ExpBottleEvent")) {
            constructDefaultListener(ExpBottleEvent.class, SProjectileHitEvent.class, SBukkitExpBottleEvent::new);
        }

        constructDefaultListener(SheepDyeWoolEvent.class, SSheepDyeWoolEvent.class, SBukkitSheepDyeWoolEvent::new);
        constructDefaultListener(SheepRegrowWoolEvent.class, SSheepRegrowWoolEvent.class, SBukkitSheepRegrowWoolEvent::new);
        constructDefaultListener(SlimeSplitEvent.class, SSlimeSplitEvent.class, SBukkitSlimeSplitEvent::new);
        if (has("org.bukkit.event.entity.StriderTemperatureChangeEvent")) {
            constructDefaultListener(StriderTemperatureChangeEvent.class, SStriderTemperatureChangeEvent.class, SBukkitStriderTemperatureChangeEvent::new);
        }
        constructDefaultListener(VillagerAcquireTradeEvent.class, SVillagerAcquireTradeEvent.class, SBukkitVillagerAcquireTradeEvent::new);
        if (has("org.bukkit.event.entity.VillagerCareerChangeEvent")) {
            constructDefaultListener(VillagerCareerChangeEvent.class, SVillagerCareerChangeEvent.class, SBukkitVillagerCareerChangeEvent::new);
        }
        constructDefaultListener(VillagerReplenishTradeEvent.class, SVillagerReplenishTradeEvent.class, SBukkitVillagerReplenishTradeEvent::new);
        constructDefaultListener(EntityToggleGlideEvent.class, SEntityToggleGlideEvent.class, SBukkitEntityToggleGlideEvent::new);
        if (has("org.bukkit.event.entity.EntityToggleSwimEvent")) {
            constructDefaultListener(EntityToggleSwimEvent.class, SEntityToggleSwimEvent.class, SBukkitEntityToggleSwimEvent::new);
        }
        constructDefaultListener(EntityUnleashEvent.class, SEntityUnleashEvent.class, factory(SBukkitEntityUnleashEvent::new)
                .sub(PlayerUnleashEntityEvent.class, SBukkitPlayerUnleashEntityEvent::new)
        );
        constructDefaultListener(ExplosionPrimeEvent.class, SExplosionPrimeEvent.class, SBukkitExplosionPrimeEvent::new);
        constructDefaultListener(FireworkExplodeEvent.class, SFireworkExplodeEvent.class, SBukkitFireworkExplodeEvent::new);
        constructDefaultListener(VehicleCreateEvent.class, SVehicleCreateEvent.class, SBukkitVehicleCreateEvent::new);

        // player
        constructDefaultListener(AsyncPlayerPreLoginEvent.class, SAsyncPlayerPreLoginEvent.class, SBukkitAsyncPlayerPreLoginEvent::new);
        constructDefaultListener(AsyncPlayerChatEvent.class, SPlayerChatEvent.class, SBukkitPlayerChatEvent::new);
        constructDefaultListener(PlayerJoinEvent.class, SPlayerJoinEvent.class, SBukkitPlayerJoinEvent::new);
        constructDefaultListener(PlayerQuitEvent.class, SPlayerLeaveEvent.class, SBukkitPlayerLeaveEvent::new);
        constructDefaultListener(BlockPlaceEvent.class, SPlayerBlockPlaceEvent.class, SBukkitPlayerBlockPlaceEvent::new);
        constructDefaultListener(BlockDamageEvent.class, SPlayerBlockDamageEvent.class, SBukkitPlayerBlockDamageEvent::new);
        /* we should register this only if someone exactly wants PlayerMoveEvent and not PlayerTeleportEvent */
        new AbstractBukkitEventHandlerFactory<>(PlayerMoveEvent.class, SPlayerMoveEvent.class, plugin, false, true) {
            @Override
            protected SPlayerMoveEvent wrapEvent(@NotNull PlayerMoveEvent event, @NotNull EventPriority priority) {
                return new SBukkitPlayerMoveEvent(event);
            }
        };
        // although PlayerTeleportEvent extends PlayerMoveEvent, it has its own HandlerList
        constructDefaultListener(PlayerTeleportEvent.class, SPlayerMoveEvent.class, SBukkitPlayerTeleportEvent::new);
        // although PlayerPortalEvent extends PlayerTeleportEvent, it has its own HandlerList
        constructDefaultListener(PlayerPortalEvent.class, SPlayerMoveEvent.class, SBukkitPlayerPortalEvent::new);
        constructDefaultListener(PlayerChangedWorldEvent.class, SPlayerWorldChangeEvent.class, SBukkitPlayerWorldChangeEvent::new);
        constructDefaultListener(SignChangeEvent.class, SPlayerUpdateSignEvent.class, SBukkitPlayerUpdateSignEvent::new);
        constructDefaultListener(PlayerRespawnEvent.class, SPlayerRespawnEvent.class, SBukkitPlayerRespawnEvent::new);
        constructDefaultListener(PlayerCommandPreprocessEvent.class, SPlayerCommandPreprocessEvent.class, SBukkitPlayerCommandPreprocessEvent::new);
        constructDefaultListener(InventoryClickEvent.class, SPlayerInventoryClickEvent.class, factory(SBukkitPlayerInventoryClickEvent::new)
                        .sub(CraftItemEvent.class, SBukkitPlayerCraftItemEvent::new)
        );
        constructDefaultListener(FoodLevelChangeEvent.class, SPlayerFoodLevelChangeEvent.class, SBukkitPlayerFoodLevelChangeEvent::new);
        constructDefaultListener(PlayerDropItemEvent.class, SPlayerDropItemEvent.class, SBukkitPlayerDropItemEvent::new);
        constructDefaultListener(PlayerBedEnterEvent.class, SPlayerBedEnterEvent.class, SBukkitPlayerBedEnterEvent::new);
        constructDefaultListener(PlayerAnimationEvent.class, SPlayerAnimationEvent.class, SBukkitPlayerAnimationEvent::new);

        // PlayerInteractEntityEvent is a weird event, each child has its own HandlerList
        constructDefaultListener(PlayerInteractEntityEvent.class, SPlayerInteractEntityEvent.class, SBukkitPlayerInteractEntityEvent::new);
        constructDefaultListener(PlayerInteractAtEntityEvent.class, SPlayerInteractEntityEvent.class, SBukkitPlayerInteractAtEntityEvent::new);
        if (has("org.bukkit.event.player.PlayerArmorStandManipulateEvent")) {
            constructDefaultListener(PlayerArmorStandManipulateEvent.class, SPlayerInteractEntityEvent.class, SBukkitPlayerArmorStandManipulateEvent::new);
        }
        constructDefaultListener(PlayerBedLeaveEvent.class, SPlayerBedLeaveEvent.class, SBukkitPlayerBedLeaveEvent::new);

        // PlayerBucketEvent is abstract and doesn't have implemented handler list
        constructDefaultListener(PlayerBucketEmptyEvent.class, SPlayerBucketEvent.class, SBukkitPlayerBucketEvent::new);
        constructDefaultListener(PlayerBucketFillEvent.class, SPlayerBucketEvent.class, SBukkitPlayerBucketEvent::new);

        if (has("org.bukkit.event.player.PlayerCommandSendEvent")) {
            constructDefaultListener(PlayerCommandSendEvent.class, SPlayerCommandSendEvent.class, SBukkitPlayerCommandSendEvent::new);
        }
        constructDefaultListener(PlayerEggThrowEvent.class, SPlayerEggThrowEvent.class, SBukkitPlayerEggThrowEvent::new);
        constructDefaultListener(PlayerExpChangeEvent.class, SPlayerExpChangeEvent.class, SBukkitPlayerExpChangeEvent::new);
        constructDefaultListener(PlayerFishEvent.class, SPlayerFishEvent.class, SBukkitPlayerFishEvent::new);
        constructDefaultListener(PlayerGameModeChangeEvent.class, SPlayerGameModeChangeEvent.class, SBukkitPlayerGameModeChangeEvent::new);
        if (has("org.bukkit.event.player.PlayerHarvestBlockEvent")) {
            constructDefaultListener(PlayerHarvestBlockEvent.class, SPlayerHarvestBlockEvent.class, SBukkitPlayerHarvestBlockEvent::new);
        }
        constructDefaultListener(PlayerInteractEvent.class, SPlayerInteractEvent.class, SBukkitPlayerInteractEvent::new);
        constructDefaultListener(PlayerItemConsumeEvent.class, SPlayerItemConsumeEvent.class, SBukkitPlayerItemConsumeEvent::new);
        constructDefaultListener(PlayerItemDamageEvent.class, SPlayerItemDamageEvent.class, SBukkitPlayerItemDamageEvent::new);
        constructDefaultListener(PlayerItemHeldEvent.class, SPlayerItemHeldEvent.class, SBukkitPlayerItemHeldEvent::new);
        if (has("org.bukkit.event.player.PlayerItemMendEvent")) {
            constructDefaultListener(PlayerItemMendEvent.class, SPlayerItemMendEvent.class, SBukkitPlayerItemMendEvent::new);
        }
        constructDefaultListener(PlayerKickEvent.class, SPlayerKickEvent.class, SBukkitPlayerKickEvent::new);
        constructDefaultListener(PlayerLevelChangeEvent.class, SPlayerLevelChangeEvent.class, SBukkitPlayerLevelChangeEvent::new);
        if (has("org.bukkit.event.player.PlayerLocaleChangeEvent")) {
            constructDefaultListener(PlayerLocaleChangeEvent.class, SPlayerLocaleChangeEvent.class, SBukkitPlayerLocaleChangeEvent::new);
        }
        constructDefaultListener(PlayerLoginEvent.class, SPlayerLoginEvent.class, SBukkitPlayerLoginEvent::new);
        constructDefaultListener(PlayerShearEntityEvent.class, SPlayerShearEntityEvent.class, SBukkitPlayerShearEntityEvent::new);
        if (has("org.bukkit.event.player.PlayerSwapHandItemsEvent")) {
            constructDefaultListener(PlayerSwapHandItemsEvent.class, SPlayerSwapHandItemsEvent.class, SBukkitPlayerSwapHandItemsEvent::new);
        }
        constructDefaultListener(PlayerToggleFlightEvent.class, SPlayerToggleFlightEvent.class, SBukkitPlayerToggleFlightEvent::new);
        constructDefaultListener(PlayerToggleSneakEvent.class, SPlayerToggleSneakEvent.class, SBukkitPlayerToggleSneakEvent::new);
        constructDefaultListener(PlayerToggleSprintEvent.class, SPlayerToggleSprintEvent.class, SBukkitPlayerToggleSprintEvent::new);
        constructDefaultListener(PlayerVelocityEvent.class, SPlayerVelocityChangeEvent.class, SBukkitPlayerVelocityChangeEvent::new);
        constructDefaultListener(InventoryOpenEvent.class, SPlayerInventoryOpenEvent.class, SBukkitPlayerInventoryOpenEvent::new);
        constructDefaultListener(InventoryCloseEvent.class, SPlayerInventoryCloseEvent.class, SBukkitPlayerInventoryCloseEvent::new);

        // block
        constructDefaultListener(BlockBurnEvent.class, SBlockBurnEvent.class, SBukkitBlockBurnEvent::new);
        if (has("org.bukkit.event.block.BlockCookEvent")) {
            constructDefaultListener(BlockCookEvent.class, SBlockCookEvent.class, SBukkitBlockCookEvent::new);
        }
        constructDefaultListener(BlockDispenseEvent.class, SBlockDispenseEvent.class, SBukkitBlockDispenseEvent::new);
        if (has("org.bukkit.event.block.BlockDropItemEvent")) {
            constructDefaultListener(BlockDropItemEvent.class, SBlockDropItemEvent.class, SBukkitBlockDropItemEvent::new);
        }
        constructDefaultListener(BlockExpEvent.class, SBlockExperienceEvent.class, factory(SBukkitBlockExperienceEvent::new)
                .sub(BlockBreakEvent.class, SBukkitPlayerBlockBreakEvent::new)
        );
        constructDefaultListener(BlockExplodeEvent.class, SBlockExplodeEvent.class, SBukkitBlockExplodeEvent::new);
        constructDefaultListener(BlockFadeEvent.class, SBlockFadeEvent.class, SBukkitBlockFadeEvent::new);
        if (has("org.bukkit.event.block.BlockFertilizeEvent")) {
            constructDefaultListener(BlockFertilizeEvent.class, SBlockFertilizeEvent.class, SBukkitBlockFertilizeEvent::new);
        }

        // children of BlockGrowEvent have their own HandlerList's (Bukkit is retarded, change my mind)
        constructDefaultListener(BlockGrowEvent.class, SBlockGrowEvent.class, SBukkitBlockGrowEvent::new);
        constructDefaultListener(BlockFormEvent.class, SBlockGrowEvent.class, factory(SBukkitBlockFormEvent::new)
                .sub(EntityBlockFormEvent.class, SBukkitBlockFormedByEntityEvent::new)
        );
        constructDefaultListener(BlockSpreadEvent.class, SBlockGrowEvent.class, SBukkitBlockSpreadEvent::new);

        constructDefaultListener(BlockFromToEvent.class, SBlockFromToEvent.class, SBukkitBlockFromToEvent::new);
        constructDefaultListener(BlockIgniteEvent.class, SBlockIgniteEvent.class, SBukkitBlockIgniteEvent::new);
        constructDefaultListener(BlockPhysicsEvent.class, SBlockPhysicsEvent.class, SBukkitBlockPhysicsEvent::new);
        constructDefaultListener(BlockRedstoneEvent.class, SRedstoneEvent.class, SBukkitRedstoneEvent::new);
        constructDefaultListener(LeavesDecayEvent.class, SLeavesDecayEvent.class, SBukkitLeavesDecayEvent::new);

        // BlockPistonEvent is abstract and doesn't have implemented handler list
        constructDefaultListener(BlockPistonExtendEvent.class, SBlockPistonEvent.class, SBukkitBlockPistonExtendEvent::new);
        constructDefaultListener(BlockPistonRetractEvent.class, SBlockPistonEvent.class, SBukkitBlockPistonRetractEvent::new);

        if (has("org.bukkit.event.block.BlockShearEntityEvent")) {
            constructDefaultListener(BlockShearEntityEvent.class, SBlockShearEntityEvent.class, SBukkitBlockShearEntityEvent::new);
        }
        constructDefaultListener(CauldronLevelChangeEvent.class, SCauldronLevelChangeEvent.class, SBukkitCauldronLevelChangeEvent::new);
        if (has("org.bukkit.event.block.FluidLevelChangeEvent")) {
            constructDefaultListener(FluidLevelChangeEvent.class, SFluidLevelChangeEvent.class, SBukkitFluidLevelChangeEvent::new);
        }
        if (has("org.bukkit.event.block.MoistureChangeEvent")) {
            constructDefaultListener(MoistureChangeEvent.class, SMoistureChangeEvent.class, SBukkitMoistureChangeEvent::new);
        }
        constructDefaultListener(StructureGrowEvent.class, SPlantGrowEvent.class, SBukkitPlantGrowEvent::new);
        if (has("org.bukkit.event.block.SpongeAbsorbEvent")) {
            constructDefaultListener(SpongeAbsorbEvent.class, SSpongeAbsorbEvent.class, SBukkitSpongeAbsorbEvent::new);
        }
        if (has("org.bukkit.event.block.BlockReceiveGameEvent")) {
            constructDefaultListener(BlockReceiveGameEvent.class, SBlockReceivedGameEvent.class, SBukkitBlockReceivedGameEvent::new);
        }

        // world
        constructDefaultListener(SpawnChangeEvent.class, SSpawnChangeEvent.class, SBukkitSpawnChangeEvent::new);
        if (has("org.bukkit.event.world.TimeSkipEvent")) {
            constructDefaultListener(TimeSkipEvent.class, STimeSkipEvent.class, SBukkitTimeSkipEvent::new);
        }
        constructDefaultListener(WorldInitEvent.class, SWorldInitEvent.class, SBukkitWorldInitEvent::new);
        constructDefaultListener(WorldLoadEvent.class, SWorldLoadEvent.class, SBukkitWorldLoadEvent::new);
        constructDefaultListener(WorldSaveEvent.class, SWorldSaveEvent.class, SBukkitWorldSaveEvent::new);
        constructDefaultListener(WorldUnloadEvent.class, SWorldUnloadEvent.class, SBukkitWorldUnloadEvent::new);

        // chunk
        constructDefaultListener(ChunkLoadEvent.class, SChunkLoadEvent.class, SBukkitChunkLoadEvent::new);
        constructDefaultListener(ChunkPopulateEvent.class, SChunkPopulateEvent.class, SBukkitChunkPopulateEvent::new);
        constructDefaultListener(ChunkUnloadEvent.class, SChunkUnloadEvent.class, SBukkitChunkUnloadEvent::new);
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
