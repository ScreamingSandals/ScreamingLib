package org.screamingsandals.lib.bukkit;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
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
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BukkitCore extends Core {
    private static BukkitAudiences provider;
    private final Plugin plugin;

    @OnEnable
    public void onEnable() {
        provider = BukkitAudiences.create(plugin);

        // TODO: this rly needs to be simplified. Lot of events are just child events of another event, so one listener can catch them all

        // entity
        new AreaEffectCloudApplyEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.ArrowBodyCountChangeEvent"))
            new ArrowBodyCountChangeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.BatToggleSleepEvent"))
            new BatToggleSleepEventListener(plugin);
        new CreatureSpawnEventListener(plugin);
        new CreeperPowerEventListener(plugin);
        new EnderDragonChangePhaseEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityAirChangeEvent"))
            new EntityAirChangeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityBreedEvent"))
            new EntityBreedEventListener(plugin);
        new EntityChangeBlockEventListener(plugin);
        new EntityCombustEventListener(plugin);
        new EntityCreatePortalEventListener(plugin);
        new EntityDamageEventListener(plugin);
        new EntityDeathEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityDropItemEvent"))
            new EntityDropItemEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityEnterBlockEvent"))
            new EntityEnterBlockEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityEnterLoveModeEvent"))
            new EntityEnterLoveModeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityExhaustionEvent"))
            new EntityExhaustionEventListener(plugin);
        new EntityExplodeEventListener(plugin);
        new EntityInteractEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityPickupItemEvent")) {
            new EntityPickupItemEventListener(plugin);
        } else {
            new LegacyPlayerPickupItemListener(plugin);
        }
        if (Reflect.has("org.bukkit.event.entity.EntityPlaceEvent"))
            new EntityPlaceEventListener(plugin);
        new EntityTeleportEventListener(plugin);
        new EntityPortalEnterEventListener(plugin);
        new EntityPortalEnterExitListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityPoseChangeEvent"))
            new EntityPoseChangeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityPotionEffectEvent"))
            new EntityPotionEffectEventListener(plugin);
        new EntityRegainHealthEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityResurrectEvent"))
            new EntityResurrectEventListener(plugin);
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
        if (Reflect.has("org.bukkit.event.entity.StriderTemperatureChangeEvent"))
            new StriderTemperatureChangeEventListener(plugin);
        new VillagerAcquireTradeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.VillagerCareerChangeEvent"))
            new VillagerCareerChangeEventListener(plugin);
        new VillagerReplenishTradeEventListener(plugin);
        new EntityToggleGlideEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityToggleSwimEvent"))
            new EntityToggleSwimEventListener(plugin);
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
        new PlayerChangeWorldEventListener(plugin);
        new PlayerSignChangeEventListener(plugin);
        new PlayerDeathEventListener(plugin);
        new PlayerRespawnEventListener(plugin);
        new PlayerCommandPreprocessEventListener(plugin);
        new PlayerInventoryClickEventListener(plugin);
        new PlayerFoodLevelChangeListener(plugin);
        new PlayerCraftItemEventListener(plugin);
        new PlayerDropItemEventListener(plugin);
        new PlayerBedEnterEventListener(plugin);
        new PlayerAnimationEventListener(plugin);
        new PlayerInteractEntityEventListener(plugin, PlayerInteractEntityEvent.class);
        new PlayerInteractEntityEventListener(plugin,  PlayerInteractAtEntityEvent.class);
        if (Reflect.has("org.bukkit.event.player.PlayerArmorStandManipulateEvent")) {
            new PlayerInteractEntityEventListener(plugin,  PlayerArmorStandManipulateEvent.class);
        }
        new PlayerBedLeaveEventListener(plugin);

        // PlayerBucketEvent is abstract and doesn't have implemented handler list
        new PlayerBucketEventListener(plugin, PlayerBucketEmptyEvent.class);
        new PlayerBucketEventListener(plugin, PlayerBucketFillEvent.class);

        if (Reflect.has("org.bukkit.event.player.PlayerCommandSendEvent"))
            new PlayerCommandSendEventListener(plugin);
        new PlayerEggThrowEventListener(plugin);
        new PlayerExpChangeEventListener(plugin);
        new PlayerFishEventListener(plugin);
        new PlayerGameModeChangeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.player.PlayerHarvestBlockEvent"))
            new PlayerHarvestBlockEventListener(plugin);
        new PlayerInteractEventListener(plugin);
        new PlayerItemConsumeEventListener(plugin);
        new PlayerItemDamageEventListener(plugin);
        new PlayerItemHeldEventListener(plugin);
        if (Reflect.has("org.bukkit.event.player.PlayerItemMendEvent"))
            new PlayerItemMendEventListener(plugin);
        new PlayerKickEventListener(plugin);
        new PlayerLevelChangeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.player.PlayerLocaleChangeEvent")) {
            constructDefaultListener(PlayerLocaleChangeEvent.class, SPlayerLocaleChangeEvent.class, SBukkitPlayerLocaleChangeEvent::new);
        }
        constructDefaultListener(PlayerLoginEvent.class, SPlayerLoginEvent.class, SBukkitPlayerLoginEvent::new);
        constructDefaultListener(PlayerShearEntityEvent.class, SPlayerShearEntityEvent.class, SBukkitPlayerShearEntityEvent::new);
        if (Reflect.has("org.bukkit.event.player.PlayerSwapHandItemsEvent")) {
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
        if (Reflect.has("org.bukkit.event.block.BlockCookEvent")) {
            constructDefaultListener(BlockCookEvent.class, SBlockCookEvent.class, SBukkitBlockCookEvent::new);
        }
        constructDefaultListener(BlockDispenseEvent.class, SBlockDispenseEvent.class, SBukkitBlockDispenseEvent::new);
        if (Reflect.has("org.bukkit.event.block.BlockDropItemEvent")) {
            constructDefaultListener(BlockDropItemEvent.class, SBlockDropItemEvent.class, SBukkitBlockDropItemEvent::new);
        }
        new BlockExperienceEventListener(plugin);
        constructDefaultListener(BlockExplodeEvent.class, SBlockExplodeEvent.class, SBukkitBlockExplodeEvent::new);
        constructDefaultListener(BlockFadeEvent.class, SBlockFadeEvent.class, SBukkitBlockFadeEvent::new);
        if (Reflect.has("org.bukkit.event.block.BlockFertilizeEvent")) {
            constructDefaultListener(BlockFertilizeEvent.class, SBlockFertilizeEvent.class, SBukkitBlockFertilizeEvent::new);
        }
        new BlockGrowEventListener(plugin);
        constructDefaultListener(BlockFromToEvent.class, SBlockFromToEvent.class, SBukkitBlockFromToEvent::new);
        constructDefaultListener(BlockIgniteEvent.class, SBlockIgniteEvent.class, SBukkitBlockIgniteEvent::new);
        constructDefaultListener(BlockPhysicsEvent.class, SBlockPhysicsEvent.class, SBukkitBlockPhysicsEvent::new);
        constructDefaultListener(BlockRedstoneEvent.class, SRedstoneEvent.class, SBukkitRedstoneEvent::new);
        constructDefaultListener(LeavesDecayEvent.class, SLeavesDecayEvent.class, SBukkitLeavesDecayEvent::new);
        new BlockPistonEventListener(plugin);
        if (Reflect.has("org.bukkit.event.block.BlockShearEntityEvent")) {
            constructDefaultListener(BlockShearEntityEvent.class, SBlockShearEntityEvent.class, SBukkitBlockShearEntityEvent::new);
        }
        constructDefaultListener(CauldronLevelChangeEvent.class, SCauldronLevelChangeEvent.class, SBukkitCauldronLevelChangeEvent::new);
        if (Reflect.has("org.bukkit.event.block.FluidLevelChangeEvent")) {
            constructDefaultListener(FluidLevelChangeEvent.class, SFluidLevelChangeEvent.class, SBukkitFluidLevelChangeEvent::new);
        }
        if (Reflect.has("org.bukkit.event.block.MoistureChangeEvent")) {
            constructDefaultListener(MoistureChangeEvent.class, SMoistureChangeEvent.class, SBukkitMoistureChangeEvent::new);
        }
        constructDefaultListener(StructureGrowEvent.class, SPlantGrowEvent.class, SBukkitPlantGrowEvent::new);
        if (Reflect.has("org.bukkit.event.block.SpongeAbsorbEvent")) {
            constructDefaultListener(SpongeAbsorbEvent.class, SSpongeAbsorbEvent.class, SBukkitSpongeAbsorbEvent::new);
        }
        if (Reflect.has("org.bukkit.event.block.BlockReceiveGameEvent")) {
            constructDefaultListener(BlockReceiveGameEvent.class, SSculkSensorReceiveEvent.class, SBukkitSculkSensorReceiveEvent::new);
        }

        // world
        constructDefaultListener(SpawnChangeEvent.class, SSpawnChangeEvent.class, SBukkitSpawnChangeEvent::new);
        if (Reflect.has("org.bukkit.event.world.TimeSkipEvent")) {
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
    private <S extends SEvent, B extends Event> void constructDefaultListener(Class<B> bukkitEvent, Class<S> screamingEvent, Function<B, S> function) {
        new AbstractBukkitEventHandlerFactory<>(bukkitEvent, screamingEvent, plugin) {
            @Override
            protected S wrapEvent(B event, EventPriority priority) {
                return function.apply(event);
            }
        };
    }
}
