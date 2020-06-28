package org.screamingsandals.lib.nms.entity;

import org.bukkit.entity.LivingEntity;

import static org.screamingsandals.lib.nms.utils.ClassStorage.NMS.*;
import static org.screamingsandals.lib.nms.utils.ClassStorage.getHandle;

public class EntityUtils {

	/*
	 * @return EntityLivingNMS
	 */
	public static EntityLivingNMS makeMobAttackTarget(LivingEntity mob, double speed, double follow,
		double attackDamage) {
		try {
			var handler = getHandle(mob);
			if (!EntityInsentient.isInstance(handler)) {
				throw new IllegalArgumentException("Entity must be instance of EntityInsentient!!");
			}
			
			var entityLiving = new EntityLivingNMS(handler);
			
			var selector = entityLiving.getGoalSelector();
			selector.clearSelector();
			selector.registerPathfinder(0, PathfinderGoalMeleeAttack
				.getConstructor(EntityCreature, double.class, boolean.class).newInstance(handler, 1.0D, false));
			
			entityLiving.setAttribute(Attribute.MOVEMENT_SPEED, speed);
			entityLiving.setAttribute(Attribute.FOLLOW_RANGE, follow);
			entityLiving.setAttribute(Attribute.ATTACK_DAMAGE, attackDamage);
			
			entityLiving.getTargetSelector().clearSelector();
			
			return entityLiving;
		} catch (Throwable ignored) {
		}
		return null;
	}
}
