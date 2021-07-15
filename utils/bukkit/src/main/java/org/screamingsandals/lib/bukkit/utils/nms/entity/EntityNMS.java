package org.screamingsandals.lib.bukkit.utils.nms.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftComponentSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.nms.accessors.ComponentAccessor;
import org.screamingsandals.lib.nms.accessors.EntityAccessor;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityNMS {

	public synchronized static int incrementAndGetId() {
		final var entityCount = Reflect.getField(EntityAccessor.getFieldField_70152_a());
		if (entityCount != null) {
			final var newCount = ((int)entityCount) + 1;
			Reflect.setField(EntityAccessor.getFieldField_70152_a(), newCount);
			return newCount;
		}

		final var entityCounter = Reflect.getField(EntityAccessor.getFieldENTITY_COUNTER());
		if (entityCounter instanceof AtomicInteger) {
			return ((AtomicInteger) entityCounter).incrementAndGet();
		}

		throw new UnsupportedOperationException("Can't obtain new Entity id");
	}

	protected Object handler;

	public EntityNMS(Object handler) {
		this.handler = handler;
	}

	public EntityNMS(Entity entity) {
		this(ClassStorage.getHandle(entity));
	}

	public Location getLocation() {
		if (Version.isVersion(1, 16)) {
			double locX = (double) Reflect.fastInvoke(handler, EntityAccessor.getMethodGetX1());
			double locY = (double) Reflect.fastInvoke(handler, EntityAccessor.getMethodGetY1());
			double locZ = (double) Reflect.fastInvoke(handler, EntityAccessor.getMethodGetZ1());
			float yaw, pitch;
			if (Version.isVersion(1,17)) {
				yaw = (float) Reflect.fastInvoke(handler, EntityAccessor.getMethodGetXRot1());
				pitch = (float) Reflect.fastInvoke(handler, EntityAccessor.getMethodGetYRot1());
			} else {
				yaw = (float) Reflect.getField(handler, EntityAccessor.getFieldField_70177_z());
				pitch = (float) Reflect.getField(handler, EntityAccessor.getFieldField_70125_A());
			}

			Object world = Reflect.fastInvoke(handler, EntityAccessor.getMethodGetCommandSenderWorld1());
			World craftWorld = (World) Reflect.getMethod(world, "getWorld").invoke();

			return new Location(craftWorld, locX, locY, locZ, yaw, pitch);
		} else {
			double locX = (double) Reflect.getField(handler, EntityAccessor.getFieldField_70165_t());
			double locY = (double) Reflect.getField(handler, EntityAccessor.getFieldField_70163_u());
			double locZ = (double) Reflect.getField(handler, EntityAccessor.getFieldField_70161_v());
			float yaw = (float) Reflect.getField(handler, EntityAccessor.getFieldField_70177_z());
			float pitch = (float) Reflect.getField(handler, EntityAccessor.getFieldField_70125_A());
			Object world = Reflect.fastInvoke(handler, EntityAccessor.getMethodGetCommandSenderWorld1());
			World craftWorld = (World) Reflect.getMethod(world, "getWorld").invoke();

			return new Location(craftWorld, locX, locY, locZ, yaw, pitch);
		}
	}

	public Object getEntityType() {
		return Reflect.fastInvoke(handler, EntityAccessor.getMethodGetType1());
	}

	public void setLocation(Location location) {
		final var craftWorld = (World) Reflect.fastInvokeResulted(handler, EntityAccessor.getMethodGetCommandSenderWorld1()).fastInvoke("getWorld");
		if (!location.getWorld().equals(craftWorld)) {
			Reflect.setField(handler, EntityAccessor.getFieldField_70170_p(), ClassStorage.getHandle(location.getWorld()));
		}
		Reflect.fastInvoke(handler, EntityAccessor.getMethodAbsMoveTo1(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public Object getHandler() {
		return handler;
	}

	public int getId() {
		return (int) Reflect.fastInvoke(handler, EntityAccessor.getMethodGetId1());
	}

	public void setId(int id) {
		Reflect.setField(handler, EntityAccessor.getFieldId(), id);
	}

	public Object getDataWatcher() {
		return Reflect.fastInvoke(handler, EntityAccessor.getMethodGetEntityData1());
	}

	public void setCustomName(Component name) {
		final var method = EntityAccessor.getMethodSetCustomName1();
		if (method != null) {
			try {
				Reflect.fastInvoke(method, MinecraftComponentSerializer.get().serialize(name));
			} catch (Exception ignored) { // current Adventure is facing some weird bug on non-adventure native server software, let's do temporary workaround
				Reflect.fastInvoke(method, ClassStorage.asMinecraftComponent(name));
			}
		} else {
			Reflect.fastInvoke(handler, EntityAccessor.getMethodFunc_96094_a1(), AdventureHelper.toLegacy(name));
		}
	}

	public Component getCustomName() {
		final var textComponent = Reflect.fastInvoke(handler, EntityAccessor.getMethodGetCustomName1());
		final var stored = ComponentAccessor.getType();
		if (stored == null) {
			return Component.empty();
		}

		if (stored.isInstance(textComponent)) {
			try {
				try {
					return MinecraftComponentSerializer.get().deserialize(textComponent);
				} catch (Exception ignored) { // current Adventure is facing some weird bug on non-adventure native server software, let's do temporary workaround
					return AdventureHelper.toComponent((String) Reflect.fastInvoke(textComponent, ComponentAccessor.getMethodFunc_150254_d1()));
				}
			} catch (Throwable t) {
				throw new UnsupportedOperationException("Cannot deserialize " + textComponent.toString(), t);
			}
		}

		return Component.empty();
	}

	public void setCustomNameVisible(boolean visible) {
		Reflect.fastInvoke(handler, EntityAccessor.getMethodSetCustomNameVisible1(), visible);
	}

	public boolean isCustomNameVisible() {
		return (boolean) Reflect.fastInvoke(handler, EntityAccessor.getMethodIsCustomNameVisible1());
	}

	public void setInvisible(boolean invisible) {
		Reflect.fastInvoke(handler, EntityAccessor.getMethodSetInvisible1(), invisible);
	}

	public boolean isInvisible() {
		return (boolean) Reflect.fastInvoke(handler, EntityAccessor.getMethodIsInvisible1());
	}

	public void setGravity(boolean gravity) {
		Reflect.fastInvoke(handler, EntityAccessor.getMethodSetNoGravity1(), !gravity);
	}

	public boolean isGravity() {
		return !((boolean) Reflect.fastInvoke(handler, EntityAccessor.getMethodIsNoGravity1()));
	}

	public boolean isOnGround() {
		final var onGround_field = Reflect.getField(handler, EntityAccessor.getFieldOnGround());
		if (onGround_field != null) {
			return (boolean) onGround_field;
		}
		return (boolean) Reflect.fastInvoke(handler, EntityAccessor.getMethodIsOnGround1());
	}

	public UUID getUniqueId() {
		return (UUID) Reflect.fastInvoke(handler, EntityAccessor.getMethodGetUUID1());
	}

	public Vector3D getVelocity() {
		if (Reflect.getField(handler, EntityAccessor.getFieldField_70159_w()) != null) {
			double motX = (double) Reflect.getField(handler, EntityAccessor.getFieldField_70159_w());
			double motY = (double) Reflect.getField(handler, EntityAccessor.getFieldField_70181_x());
			double motZ = (double) Reflect.getField(handler, EntityAccessor.getFieldField_70179_y());
			return new Vector3D(
					motX,
					motY,
					motZ
			);
		} else {
			final var mot = Reflect.fastInvoke(handler, EntityAccessor.getMethodGetDeltaMovement1());
			final var bukkitVector = (Vector) Reflect.getMethod(ClassStorage.NMS.CraftVector, "toBukkit").invokeStatic(mot);
			if (bukkitVector == null) {
				return new Vector3D(0, 0, 0);
			}
			return new Vector3D(
					bukkitVector.getX(),
					bukkitVector.getY(),
					bukkitVector.getZ()
			);
		}
	}

}
