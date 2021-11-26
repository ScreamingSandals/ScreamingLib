package org.screamingsandals.lib.bukkit;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.*;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.event.block.*;
import org.screamingsandals.lib.bukkit.event.chunk.*;
import org.screamingsandals.lib.bukkit.event.player.*;
import org.screamingsandals.lib.bukkit.event.world.*;
import org.screamingsandals.lib.bukkit.listener.*;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.event.block.*;
import org.screamingsandals.lib.event.chunk.*;
import org.screamingsandals.lib.event.player.*;
import org.screamingsandals.lib.event.world.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;

import java.util.*;
import java.util.function.Function;

import static org.screamingsandals.lib.utils.reflect.Reflect.has;

@Service
@RequiredArgsConstructor
public class BukkitCore extends Core {
    private static BukkitAudiences provider;
    private final Plugin plugin;

    @OnEnable
    public void onEnable() {
        provider = BukkitAudiences.create(plugin);

        // entity
        new AreaEffectCloudApplyEventListener(plugin);
        if (has("org.bukkit.event.entity.ArrowBodyCountChangeEvent")) {
            new ArrowBodyCountChangeEventListener(plugin);
        }
        if (has("org.bukkit.event.entity.BatToggleSleepEvent")) {
            new BatToggleSleepEventListener(plugin);
        }
        new CreatureSpawnEventListener(plugin);
        new CreeperPowerEventListener(plugin);
        new EnderDragonChangePhaseEventListener(plugin);
        if (has("org.bukkit.event.entity.EntityAirChangeEvent")) {
            new EntityAirChangeEventListener(plugin);
        }
        if (has("org.bukkit.event.entity.EntityBreedEvent")) {
            new EntityBreedEventListener(plugin);
        }
        new EntityChangeBlockEventListener(plugin);
        new EntityCombustEventListener(plugin);
        new EntityCreatePortalEventListener(plugin);
        new EntityDamageEventListener(plugin);
        new EntityDeathEventListener(plugin);
        if (has("org.bukkit.event.entity.EntityDropItemEvent")) {
            new EntityDropItemEventListener(plugin);
        }
        if (has("org.bukkit.event.entity.EntityEnterBlockEvent")) {
            new EntityEnterBlockEventListener(plugin);
        }
        if (has("org.bukkit.event.entity.EntityEnterLoveModeEvent")) {
            new EntityEnterLoveModeEventListener(plugin);
        }
        if (has("org.bukkit.event.entity.EntityExhaustionEvent")) {
            new EntityExhaustionEventListener(plugin);
        }
        new EntityExplodeEventListener(plugin);
        new EntityInteractEventListener(plugin);
        if (has("org.bukkit.event.entity.EntityPickupItemEvent")) {
            new EntityPickupItemEventListener(plugin);
        } else {
            new LegacyPlayerPickupItemListener(plugin);
        }
        if (has("org.bukkit.event.entity.EntityPlaceEvent")) {
            new EntityPlaceEventListener(plugin);
        }
        new EntityTeleportEventListener(plugin);
        new EntityPortalEnterEventListener(plugin);
        new EntityPortalEnterExitListener(plugin);
        if (has("org.bukkit.event.entity.EntityPoseChangeEvent")) {
            new EntityPoseChangeEventListener(plugin);
        }
        if (has("org.bukkit.event.entity.EntityPotionEffectEvent")) {
            new EntityPotionEffectEventListener(plugin);
        }
        new EntityRegainHealthEventListener(plugin);
        if (has("org.bukkit.event.entity.EntityResurrectEvent")) {
            new EntityResurrectEventListener(plugin);
        }
        new EntityShootBowEventListener(plugin);
        new EntitySpawnEventListener(plugin);
        new EntityTameEventListener(plugin);
        new EntityTargetEventListener(plugin);
        new FoodLevelChangeEventListener(plugin);
        new HorseJumpEventListener(plugin);
        new ItemDespawnEventListener(plugin);
        new ItemMergeEventListener(plugin);
        new ProjectileHitEventListener(plugin);
        new SheepDyeWoolEventListener(plugin);
        new SheepRegrowWoolEventListener(plugin);
        new SlimeSplitEventListener(plugin);
        if (has("org.bukkit.event.entity.StriderTemperatureChangeEvent")) {
            new StriderTemperatureChangeEventListener(plugin);
        }
        new VillagerAcquireTradeEventListener(plugin);
        if (has("org.bukkit.event.entity.VillagerCareerChangeEvent")) {
            new VillagerCareerChangeEventListener(plugin);
        }
        new VillagerReplenishTradeEventListener(plugin);
        new EntityToggleGlideEventListener(plugin);
        if (has("org.bukkit.event.entity.EntityToggleSwimEvent")) {
            new EntityToggleSwimEventListener(plugin);
        }
        new EntityUnleashEventListener(plugin);
        new ExplosionPrimeEventListener(plugin);
        new FireworkExplodeEventListener(plugin);
        new VehicleCreateEventListener(plugin);

        // player
        new AsyncPlayerPreLoginEventListener(plugin);
        new AsyncPlayerChatEventListener(plugin);
        new PlayerJoinEventListener(plugin);
        new PlayerLeaveEventListener(plugin);
        new PlayerBlockPlaceEventListener(plugin);
        new PlayerBlockDamageEventListener(plugin);
        new PlayerMoveEventListener(plugin);
        new PlayerTeleportEventListener(plugin);
        constructDefaultListener(PlayerChangedWorldEvent.class, SPlayerWorldChangeEvent.class, SBukkitPlayerWorldChangeEvent::new);
        constructDefaultListener(SignChangeEvent.class, SPlayerUpdateSignEvent.class, SBukkitPlayerUpdateSignEvent::new);
        new PlayerDeathEventListener(plugin); // TODO: will be mapped with EntityDeathEvent because it's children
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
            constructDefaultListener(BlockReceiveGameEvent.class, SSculkSensorReceiveEvent.class, SBukkitSculkSensorReceiveEvent::new);
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

    public static BukkitAudiences audiences() {
        return provider;
    }

    /**
     * @param bukkitEvent the bukkit event
     * @param screamingEvent screaming event class, must be the abstract class from core module!!!
     * @param function which returns the constructed screaming event
     */
    private <S extends SEvent, B extends Event> void constructDefaultListener(Class<B> bukkitEvent, Class<S> screamingEvent, Function<B, ? extends S> function) {
        new AbstractBukkitEventHandlerFactory<>(bukkitEvent, screamingEvent, plugin) {
            @Override
            protected S wrapEvent(B event, EventPriority priority) {
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
