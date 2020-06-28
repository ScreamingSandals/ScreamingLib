package org.screamingsandals.lib.nms.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.function.Consumer;

import static org.screamingsandals.lib.nms.utils.ClassStorage.NMS.*;
import static org.screamingsandals.lib.nms.utils.ClassStorage.*;
import static org.screamingsandals.lib.reflection.Reflection.*;

public class EntityLivingNMS extends EntityNMS {

	public EntityLivingNMS(Object handler) {
		super(handler);
		if (!EntityInsentient.isInstance(handler)) {
			throw new IllegalArgumentException("Entity must be instance of EntityInsentient!!");
		}
	}

	public EntityLivingNMS(LivingEntity entity) {
		this(getHandle(entity));
	}

	public TargetSelector getTargetSelector() {
		return new TargetSelector(handler);
	}

	public GoalSelector getGoalSelector() {
		return new GoalSelector(handler);
	}
	
	public boolean hasAttribute(Attribute attribute) {
		return hasAttribute(attribute.getKeys());
	}
	
	public boolean hasAttribute(String keys) {
		try {
			var attr = getField(GenericAttributes, keys);
			var attr0 = getMethod(handler, "getAttributeInstance,func_110148_a", IAttribute)
				.invoke(attr);
			return attr0 != null;
		} catch (Throwable ignored) {
		}
		return false;
	}
	
	public double getAttribute(Attribute attribute) {
		return getAttribute(attribute.getKeys());
	}
	
	public double getAttribute(String keys) {
		try {
			var attr = getField(GenericAttributes, keys);
			var attr0 = getMethod(handler, "getAttributeInstance,func_110148_a", IAttribute)
				.invoke(attr);
			return (double) fastInvoke(attr0, "getValue,func_111126_e");
		} catch (Throwable ignored) {
		}
		return 0;
	}
	
	public void setAttribute(Attribute attribute, double value) {
		setAttribute(attribute.getKeys(), value);
	}
	
	public void setAttribute(String keys, double value) {
		try {
			if (value >= 0) {
				var attr = getField(GenericAttributes, keys);
				var attr0 = getMethod(handler, "getAttributeInstance,func_110148_a", IAttribute)
						.invoke(attr);
				if (attr0 == null) {
					var attrMap = fastInvoke(handler, "getAttributeMap,func_110140_aT");
					// Pre 1.16
					attr0 = getMethod(attrMap, "b,func_111150_b", IAttribute).invoke(attr);
					if (attr0 instanceof Boolean) {
						// 1.16
						var provider = getField(attrMap,"d,field_233777_d_");
						Map<Object, Object> all = Maps
								.newHashMap((Map<?, ?>) getField(provider, "a,field_233802_a_"));
						attr0 = AttributeModifiable.getConstructor(IAttribute, Consumer.class).newInstance(attr, (Consumer) o -> {
							// do nothing
						});
						all.put(attr, attr0);
						setField(provider, "a,field_233802_a_", ImmutableMap.copyOf(all));
					}
				}
				getMethod(attr0, "setValue,func_111128_a", double.class).invoke(value);
			}
		} catch (Throwable ignore) {
		}
	}
}
