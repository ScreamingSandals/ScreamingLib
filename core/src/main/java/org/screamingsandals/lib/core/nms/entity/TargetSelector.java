package org.screamingsandals.lib.core.nms.entity;

import org.bukkit.entity.LivingEntity;

import static org.screamingsandals.lib.core.reflect.SReflect.setField;
import static org.screamingsandals.lib.core.nms.utils.ClassStorage.NMS.EntityInsentient;
import static org.screamingsandals.lib.core.nms.utils.ClassStorage.NMS.PathfinderGoalNearestAttackableTarget;
import static org.screamingsandals.lib.core.nms.utils.ClassStorage.getHandle;
import static org.screamingsandals.lib.core.nms.utils.ClassStorage.getNMSClassSafe;

public class TargetSelector extends Selector {

    public TargetSelector(Object handler) {
        super(handler, "targetSelector,field_70715_bh");
    }

    public TargetSelector attackTarget(LivingEntity target) {
        setField(handler, "goalTarget,field_70696_bz", target == null ? null : getHandle(target));
        return this;
    }

    // TODO: make enum for entity types
    public TargetSelector attackNearestTarget(int a, String targetClass) {
        return attackNearestTarget(a, getNMSClassSafe("{nms}." + targetClass));
    }

    public TargetSelector attackNearestTarget(int a, Class<?> targetClass) {
        try {
            var targetNear = PathfinderGoalNearestAttackableTarget.getConstructor(EntityInsentient, Class.class, Boolean.TYPE)
                    .newInstance(handler, targetClass, false);
            registerPathfinder(a, targetNear);
        } catch (Throwable ignored) {
        }
        return this;
    }

    // And here add new targets if it's needed
}
