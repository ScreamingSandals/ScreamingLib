package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.ProjectileMeta;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.entity.ProjectileShooter;

public class MinestomEntityProjectile extends MinestomEntityBasic implements EntityProjectile {
    protected MinestomEntityProjectile(net.minestom.server.entity.EntityProjectile wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public ProjectileShooter getShooter() {
        final var source = ((net.minestom.server.entity.EntityProjectile) wrappedObject).getShooter();
        if (source != null) {
            return EntityMapper.<EntityLiving>wrapEntity(source).orElseThrow();
        }
        return null;
    }

    @Override
    public void setShooter(ProjectileShooter shooter) {
        if (wrappedObject.getEntityMeta() instanceof ProjectileMeta) {
            ((ProjectileMeta) wrappedObject.getEntityMeta()).setShooter(shooter.as(LivingEntity.class));
        }
    }

    @Override
    public boolean doesBounce() {
        return false;
    }

    @Override
    public void setBounce(boolean bounce) {
        // empty stub
    }
}
