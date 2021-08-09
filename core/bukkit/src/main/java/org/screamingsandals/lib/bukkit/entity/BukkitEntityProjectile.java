package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.entity.ProjectileShooter;

public class BukkitEntityProjectile extends BukkitEntityBasic implements EntityProjectile {
    protected BukkitEntityProjectile(Projectile wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public ProjectileShooter getShooter() {
        var source = ((Projectile) wrappedObject).getShooter();
        if (source instanceof Entity) {
            return EntityMapper.<EntityLiving>wrapEntity(source).orElseThrow();
        } else if (source instanceof BlockProjectileSource) {
            return new BukkitBlockProjectileSource((BlockProjectileSource) source);
        }
        return null;
    }

    @Override
    public void setShooter(ProjectileShooter shooter) {
        ((Projectile) wrappedObject).setShooter(shooter.as(ProjectileSource.class));
    }

    @Override
    public boolean doesBounce() {
        return ((Projectile) wrappedObject).doesBounce();
    }

    @Override
    public void setBounce(boolean bounce) {
        ((Projectile) wrappedObject).setBounce(bounce);
    }
}
