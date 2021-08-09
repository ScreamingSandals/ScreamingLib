package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.entity.BlockProjectileShooter;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.BlockMapper;

import java.util.Optional;

public class BukkitBlockProjectileSource extends BasicWrapper<BlockProjectileSource> implements BlockProjectileShooter {
    protected BukkitBlockProjectileSource(BlockProjectileSource wrappedObject) {
        super(wrappedObject);
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType) {
        return EntityMapper.wrapEntity(wrappedObject.launchProjectile((Class<Projectile>) projectileType.as(EntityType.class).getEntityClass()));
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType, Vector3D velocity) {
        return EntityMapper.wrapEntity(wrappedObject.launchProjectile((Class<Projectile>) projectileType.as(EntityType.class).getEntityClass(), new Vector(velocity.getX(), velocity.getY(), velocity.getZ())));
    }

    @Override
    public BlockHolder getBlock() {
        return BlockMapper.wrapBlock(wrappedObject.getBlock());
    }
}
