package org.screamingsandals.lib.bukkit.utils.nms.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityNMS {

	public static int incrementAndGetId() {
		final var entityCount = Reflect.getField(ClassStorage.NMS.Entity, "entityCount,field_70152_a");
		if (entityCount != null) {
			return ((int)entityCount) + 1;
		}

		final var entityCounter = Reflect.getField(ClassStorage.NMS.Entity, "b,f_19843_");
		if (entityCounter instanceof AtomicInteger) {
			return ((AtomicInteger) entityCounter).incrementAndGet();
		}

		throw new UnsupportedOperationException("Can't obtain new Entity id, rip");
	}

	protected Object handler;

	public EntityNMS(Object handler) {
		this.handler = handler;
	}

	public EntityNMS(Entity entity) {
		this(ClassStorage.getHandle(entity));
	}

	public Location getLocation() {
		if (Version.isVersion(1, 17)) {
			double locX = (double) Reflect.getMethod(handler, "locX").invoke();
			double locY = (double) Reflect.getMethod(handler, "locY").invoke();
			double locZ = (double) Reflect.getMethod(handler, "locZ").invoke();
			float yaw = (float) Reflect.getMethod(handler, "getYRot").invoke();
			float pitch = (float) Reflect.getMethod(handler, "getXRot").invoke();
			Object world = Reflect.getMethod(handler, "getWorld,func_130014_f_").invoke();
			World craftWorld = (World) Reflect.getMethod(world, "getWorld").invoke();

			return new Location(craftWorld, locX, locY, locZ, yaw, pitch);
		} else {
			double locX = (double) Reflect.getField(handler, "locX,field_70165_t");
			double locY = (double) Reflect.getField(handler, "locY,field_70163_u");
			double locZ = (double) Reflect.getField(handler, "locZ,field_70161_v");
			float yaw = (float) Reflect.getField(handler, "yaw,field_70177_z");
			float pitch = (float) Reflect.getField(handler, "pitch,field_70125_A");
			Object world = Reflect.getMethod(handler, "getWorld,func_130014_f_").invoke();
			World craftWorld = (World) Reflect.getMethod(world, "getWorld").invoke();

			return new Location(craftWorld, locX, locY, locZ, yaw, pitch);
		}
	}

	public Object getEntityType() {
		return Reflect.getMethod(handler, "getEntityType,func_200600_R").invoke();
	}

	public void setLocation(Location location) {
		final var craftWorld = (World) Reflect.fastInvokeResulted(handler, "getWorld,func_130014_f_").fastInvoke("getWorld");
		if (!location.getWorld().equals(craftWorld)) {
			Reflect.setField(handler, "world,field_70170_p", ClassStorage.getHandle(location.getWorld()));
		}

		Reflect.getMethod(handler, "setLocation,func_70080_a", double.class, double.class, double.class, float.class, float.class)
			.invoke(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public Object getHandler() {
		return handler;
	}

	public int getId() {
		return (int) Reflect.getMethod(handler, "getId,func_145782_y").invoke();
	}

	public Object getDataWatcher() {
		return Reflect.getMethod(handler, "getDataWatcher,func_184212_Q").invoke();
	}

	public void setCustomName(Component name) {
		final var method = Reflect.getMethod(handler, "setCustomName,func_200203_b", ClassStorage.NMS.IChatBaseComponent);
		if (method.getMethod() != null) {
			try {
				method.invoke(MinecraftComponentSerializer.get().serialize(name));
			} catch (Exception ignored) { // current Adventure is facing some weird bug on non-adventure native server software, let's do temporary workaround
				method.invoke(Reflect.getMethod(ClassStorage.NMS.ChatSerializer, "a,field_150700_a", String.class)
						.invokeStatic(GsonComponentSerializer.gson().serialize(name)));
			}
		} else {
			Reflect.getMethod(handler, "setCustomName,func_96094_a", String.class).invoke(AdventureHelper.toLegacy(name));
		}
	}

	public Component getCustomName() {
		final var textComponent = Reflect.getMethod(handler, "getCustomName,func_200201_e,func_95999_t").invoke();
		final var stored = ClassStorage.NMS.IChatBaseComponent;
		if (stored == null) {
			return Component.empty();
		}

		if (stored.isInstance(textComponent)) {
			try {
				try {
					return MinecraftComponentSerializer.get().deserialize(textComponent);
				} catch (Exception ignored) { // current Adventure is facing some weird bug on non-adventure native server software, let's do temporary workaround
					return AdventureHelper.toComponent((String) Reflect.getMethod(textComponent, "getLegacyString,func_150254_d").invoke());
				}
			} catch (Throwable t) {
				throw new UnsupportedOperationException("Cannot deserialize " + textComponent.toString(), t);
			}
		}

		return Component.empty();
	}

	public void setCustomNameVisible(boolean visible) {
		Reflect.getMethod(handler, "setCustomNameVisible,func_174805_g", boolean.class).invoke(visible);
	}

	public boolean isCustomNameVisible() {
		return (boolean) Reflect.getMethod(handler, "getCustomNameVisible,func_174833_aM").invoke();
	}

	public void setInvisible(boolean invisible) {
		Reflect.getMethod(handler, "setInvisible,func_82142_c", boolean.class).invoke(invisible);
	}

	public boolean isInvisible() {
		return (boolean) Reflect.getMethod(handler, "isInvisible,func_82150_aj").invoke();
	}

	public void setGravity(boolean gravity) {
		Reflect.getMethod(handler, "setNoGravity,func_189654_d", boolean.class).invoke(!gravity);
	}

	//TODO: forge names
	public boolean isGravity() {
		return !((boolean) Reflect.getMethod(handler, "isNoGravity,func_189652_ae").invoke());
	}

	public boolean isOnGround() {
		return (boolean) Reflect.getMethod(handler, "isOnGround,func_233570_aj_").invoke();
	}

	public UUID getUniqueId() {
		return (UUID) Reflect.getMethod(handler, "getUniqueID,func_110124_au").invoke();
	}

	public Vector3D getVelocity() {
		final var mot = Reflect.getMethod(handler, "getMot,func_213322_ci").invoke();
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
