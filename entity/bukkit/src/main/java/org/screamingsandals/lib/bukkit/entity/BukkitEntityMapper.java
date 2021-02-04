package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.screamingsandals.lib.bukkit.entity.type.BukkitEntityTypeMapping;
import org.screamingsandals.lib.bukkit.material.meta.BukkitPotionEffectMapping;
import org.screamingsandals.lib.bukkit.world.BukkitLocationMapper;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;

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
        if (entity instanceof LivingEntity) {
            return Optional.of((T) new BukkitEntityLiving((LivingEntity) entity));
        }

        if (entity instanceof Item) {
            return Optional.of((T) new BukkitEntityItem((Item) entity));
        }

        return Optional.of((T) new BukkitEntityBasic((Entity) entity));
    }
}