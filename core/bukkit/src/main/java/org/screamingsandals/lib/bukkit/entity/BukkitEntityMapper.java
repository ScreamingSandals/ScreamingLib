package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.listener.*;
import org.screamingsandals.lib.bukkit.entity.type.BukkitEntityTypeMapping;
import org.screamingsandals.lib.bukkit.material.meta.BukkitPotionEffectMapping;
import org.screamingsandals.lib.bukkit.world.BukkitLocationMapper;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SEntityPotionEffectEvent;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;

@Service(dependsOn = {
        BukkitEntityTypeMapping.class,
        BukkitLocationMapper.class,
        BukkitPotionEffectMapping.class
})
public class BukkitEntityMapper extends EntityMapper {

    public static void init() {
        EntityMapper.init(BukkitEntityMapper::new);
    }

    public BukkitEntityMapper() {
        InitUtils.doIfNot(BukkitEntityTypeMapping::isInitialized, BukkitEntityTypeMapping::init);
        InitUtils.doIfNot(BukkitLocationMapper::isInitialized, BukkitLocationMapper::init);
        InitUtils.doIfNot(BukkitPotionEffectMapping::isInitialized, BukkitPotionEffectMapping::init);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends EntityBasic> Optional<T> wrapEntity0(Object entity) {
        if (!(entity instanceof Entity)) {
            return Optional.empty();
        }

        // order is important here
        if (entity instanceof HumanEntity) {
            return Optional.of((T) new BukkitEntityHuman((HumanEntity) entity));
        }

        if (entity instanceof LivingEntity) {
            return Optional.of((T) new BukkitEntityLiving((LivingEntity) entity));
        }

        if (entity instanceof Item) {
            return Optional.of((T) new BukkitEntityItem((Item) entity));
        }

        return Optional.of((T) new BukkitEntityBasic((Entity) entity));
    }

    @Override
    public <T extends EntityBasic> Optional<T> spawn0(EntityTypeHolder entityType, LocationHolder locationHolder) {
        return entityType.asOptional(EntityType.class).flatMap(entityType1 -> {
            var world = locationHolder.getWorld().as(World.class);
            if (world != null) {
                // TODO: test all entity types
                var entity = world.spawnEntity(locationHolder.as(Location.class), entityType1);
                return wrapEntity0(entity);
            }
            return Optional.empty();
        });
    }

    private void registerListeners(Plugin plugin) {
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
    }
}
