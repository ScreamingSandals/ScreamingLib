package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.bukkit.entity.type.BukkitEntityTypeMapping;
import org.screamingsandals.lib.bukkit.material.meta.BukkitPotionEffectMapping;
import org.screamingsandals.lib.bukkit.world.BukkitLocationMapper;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.annotations.Service;
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

    @Override
    public Optional<EntityItem> dropItem0(org.screamingsandals.lib.material.Item item, LocationHolder locationHolder) {
        var bukkitLoc = locationHolder.as(Location.class);
        var itemEntity = bukkitLoc.getWorld().dropItem(bukkitLoc, item.as(ItemStack.class));
        return Optional.of(new BukkitEntityItem(itemEntity));
    }
}
