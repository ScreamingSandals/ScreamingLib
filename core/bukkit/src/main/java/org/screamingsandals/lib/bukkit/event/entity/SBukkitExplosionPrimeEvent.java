package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SExplosionPrimeEvent;

@Data
public class SBukkitExplosionPrimeEvent implements SExplosionPrimeEvent, BukkitCancellable {
    private final ExplosionPrimeEvent event;

    // Internal cache
    private EntityBasic entity;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public float getRadius() {
        return event.getRadius();
    }

    @Override
    public void setRadius(float radius) {
        event.setRadius(radius);
    }

    @Override
    public boolean isFire() {
        return event.getFire();
    }

    @Override
    public void setFire(boolean fire) {
        event.setFire(fire);
    }
}
