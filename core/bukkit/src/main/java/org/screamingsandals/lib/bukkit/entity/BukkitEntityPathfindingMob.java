package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.*;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.EntityPathfindingMob;

import java.util.Optional;

public class BukkitEntityPathfindingMob extends BukkitEntityLiving implements EntityPathfindingMob {
    public BukkitEntityPathfindingMob(LivingEntity wrappedObject) {
        super(wrappedObject);

        if (BukkitEntityMapper.HAS_MOB_INTERFACE) {
            if (!(wrappedObject instanceof Mob)) {
                throw new UnsupportedOperationException("Wrapped object is not instance of Mob!");
            }
        } else if (!(wrappedObject instanceof Slime || wrappedObject instanceof Creature)) {
            throw new UnsupportedOperationException("Wrapped object is not instance of Slime or Creature!");
        }
    }

    @Override
    public void setCurrentTarget(@Nullable EntityLiving target) {
        var living = target == null ? null : target.as(LivingEntity.class);
        if (BukkitEntityMapper.HAS_MOB_INTERFACE) {
            ((Mob) wrappedObject).setTarget(living);
        } else if (wrappedObject instanceof Slime) {
            ((Slime) wrappedObject).setTarget(living);
        } else {
            ((Creature) wrappedObject).setTarget(living);
        }
    }

    @Override
    public Optional<EntityLiving> getCurrentTarget() {
        LivingEntity living;
        if (BukkitEntityMapper.HAS_MOB_INTERFACE) {
            living = ((Mob) wrappedObject).getTarget();
        } else if (wrappedObject instanceof Slime) {
            living = ((Slime) wrappedObject).getTarget();
        } else {
            living = ((Creature) wrappedObject).getTarget();
        }
        return EntityMapper.wrapEntity(living);
    }
}
