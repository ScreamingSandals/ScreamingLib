package org.screamingsandals.lib.bukkit;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.bukkit.listener.*;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;
import org.screamingsandals.lib.utils.reflect.Reflect;

@Service
@RequiredArgsConstructor
public class BukkitCore extends Core {
    private final Plugin plugin;

    @OnEnable
    public void onEnable() {
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
        if (Reflect.has("org.bukkit.event.entity.EntityPickupItemEventListener"))
            new EntityPickupItemEventListener(plugin);
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

        // player
        new AsyncPlayerPreLoginEventListener(plugin);
        new AsyncPlayerChatEventListener(plugin);
        new PlayerJoinEventListener(plugin);
        new PlayerLeaveEventListener(plugin);
        new PlayerBlockPlaceEventListener(plugin);
        new PlayerBlockDamageEventListener(plugin);
        new PlayerBlockBreakEventListener(plugin);
        new PlayerMoveEventListener(plugin);
        new PlayerTeleportEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityPickupItemEvent")) {
            new PlayerPickupItemListener(plugin);
        } else {
            new LegacyPlayerPickupItemListener(plugin);
        }
        new PlayerChangeWorldEventListener(plugin);
        new PlayerSignChangeEventListener(plugin);
        new PlayerDeathEventListener(plugin);
        new PlayerRespawnEventListener(plugin);
        new PlayerCommandPreprocessEventListener(plugin);
        new PlayerInventoryClickEventListener(plugin);
        new PlayerFoodLevelChangeListener(plugin);
        new PlayerCraftItemEventListener(plugin);
        new PlayerProjectileLaunchEventListener(plugin);
        new PlayerDropItemEventListener(plugin);
        new PlayerBedEnterEventListener(plugin);
        new PlayerAnimationEventListener(plugin);
        new PlayerInteractEntityEventListener(plugin);
        new PlayerBedLeaveEventListener(plugin);
        new PlayerBucketEventListener(plugin);
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
        if (Reflect.has("org.bukkit.event.player.PlayerLocaleChangeEvent"))
            new PlayerLocaleChangeEventListener(plugin);
        new PlayerLoginEventListener(plugin);
        new PlayerPortalEventListener(plugin);
        new PlayerShearEntityEventListener(plugin);
        if (Reflect.has("org.bukkit.event.player.PlayerSwapHandItemsEvent"))
            new PlayerSwapHandItemsEventListener(plugin);
        new PlayerToggleFlightEventListener(plugin);
        new PlayerToggleSneakEventListener(plugin);
        new PlayerToggleSprintEventListener(plugin);
        new PlayerUnleashEntityEventListener(plugin);
        new PlayerVelocityEventListener(plugin);

        // block
        new BlockBurnEventListener(plugin);
        if (Reflect.has("org.bukkit.event.block.BlockCookEvent"))
            new BlockCookEventListener(plugin);
        new BlockDispenseEventListener(plugin);
        if (Reflect.has("org.bukkit.event.block.BlockDropItemEvent"))
            new BlockDropItemEventListener(plugin);
        new BlockExperienceEventListener(plugin);
        new BlockExplodeEventListener(plugin);
        new BlockFadeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.block.BlockFertilizeEvent"))
            new BlockFertilizeEventListener(plugin);
        new BlockGrowEventListener(plugin);
        new BlockFromToEventListener(plugin);
        new BlockIgniteEventListener(plugin);
        new BlockPhysicsEventListener(plugin);
        new RedstoneEventListener(plugin);
        new LeavesDecayEventListener(plugin);
        new BlockPistonEventListener(plugin);
        if (Reflect.has("org.bukkit.event.block.BlockShearEntityEvent"))
            new BlockShearEntityEventListener(plugin);
        new CauldronLevelChangeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.block.FluidLevelChangeEvent"))
            new FluidLevelChangeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.block.MoistureChangeEvent"))
            new MoistureChangeEventListener(plugin);
        new StructureGrowEventListener(plugin);
        if (Reflect.has("org.bukkit.event.block.SpongeAbsorbEvent"))
            new SpongeAbsorbEventListener(plugin);
        if (Reflect.has("org.bukkit.event.block.BlockReceiveGameEvent"))
            new BlockReceiveGameEventListener(plugin);

        // world
        new SpawnChangeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.world.TimeSkipEvent"))
            new TimeSkipEventListener(plugin);
        new WorldInitEventListener(plugin);
        new WorldLoadEventListener(plugin);
        new WorldSaveEventListener(plugin);
        new WorldUnloadEventListener(plugin);

        // chunk
        new ChunkLoadEventListener(plugin);
        new ChunkPopulateEventListener(plugin);
        new ChunkUnloadEventListener(plugin);
    }
}
