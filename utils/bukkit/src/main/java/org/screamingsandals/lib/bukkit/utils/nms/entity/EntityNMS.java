package org.screamingsandals.lib.bukkit.utils.nms.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.utils.AdventureHelper;

public class EntityNMS {
	protected Object handler;

	public EntityNMS(Object handler) {
		this.handler = handler;
	}

	public EntityNMS(Entity entity) {
		this(ClassStorage.getHandle(entity));
	}

	public Location getLocation() {
		final var locX = (double) ClassStorage.getField(handler, "locX,field_70165_t");
		final var locY = (double) ClassStorage.getField(handler, "locY,field_70163_u");
		final var locZ = (double) ClassStorage.getField(handler, "locZ,field_70161_v");
		final var yaw = (float) ClassStorage.getField(handler, "yaw,field_70177_z");
		final var pitch = (float) ClassStorage.getField(handler, "pitch,field_70125_A");
		final var world = ClassStorage.getMethod(handler, "getWorld,func_130014_f_").invoke();
		final var craftWorld = (World) ClassStorage.getMethod(world, "getWorld").invoke();

		return new Location(craftWorld, locX, locY, locZ, yaw, pitch);
	}

	public void setLocation(Location location) {
		final var world = ClassStorage.getMethod(handler, "getWorld,func_130014_f_").invoke();
		final var craftWorld = (World) ClassStorage.getMethod(world, "getWorld").invoke();
		if (!location.getWorld().equals(craftWorld)) {
			ClassStorage.setField(handler, "world,field_70170_p", ClassStorage.getHandle(location.getWorld()));
		}

		ClassStorage.getMethod(handler, "setLocation,func_70080_a", double.class, double.class, double.class, float.class, float.class)
			.invoke(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public Object getHandler() {
		return handler;
	}

	public int getId() {
		return (int) ClassStorage.getMethod(handler, "getId,func_145782_y").invoke();
	}

	public Object getDataWatcher() {
		return ClassStorage.getMethod(handler, "getDataWatcher,func_184212_Q").invoke();
	}

	public void setCustomName(Component name) {
		final var method = ClassStorage.getMethod(handler, "setCustomName,func_200203_b", ClassStorage.NMS.IChatBaseComponent);
		if (method.getMethod() != null) {
			try {
				method.invoke(MinecraftComponentSerializer.get().serialize(name));
			} catch (Exception ignored) { // current Adventure is facing some weird bug on non-adventure native server software, let's do temporary workaround
				method.invoke(ClassStorage.getMethod(ClassStorage.NMS.ChatSerializer, "a,field_150700_a", String.class)
						.invokeStatic(GsonComponentSerializer.gson().serialize(name)));
			}
		} else {
			ClassStorage.getMethod(handler, "setCustomName,func_96094_a", String.class).invoke(AdventureHelper.toLegacy(name));
		}
	}

	public Component getCustomName() {
		final var textComponent = ClassStorage.getMethod(handler, "getCustomName,func_200201_e,func_95999_t").invoke();
		final var stored = ClassStorage.NMS.IChatBaseComponent;
		if (stored == null) {
			return Component.empty();
		}

		if (stored.isInstance(textComponent)) {
			try {
				try {
					return MinecraftComponentSerializer.get().deserialize(textComponent);
				} catch (Exception ignored) { // current Adventure is facing some weird bug on non-adventure native server software, let's do temporary workaround
					return AdventureHelper.toComponent((String) ClassStorage.getMethod(textComponent, "getLegacyString,func_150254_d").invoke());
				}
			} catch (Throwable t) {
				throw new UnsupportedOperationException("Cannot deserialize " + textComponent.toString(), t);
			}
		}

		return Component.empty();
	}

	public void setCustomNameVisible(boolean visible) {
		ClassStorage.getMethod(handler, "setCustomNameVisible,func_174805_g", boolean.class).invoke(visible);
	}

	public boolean isCustomNameVisible() {
		return (boolean) ClassStorage.getMethod(handler, "getCustomNameVisible,func_174833_aM").invoke();
	}

	public void setInvisible(boolean invisible) {
		ClassStorage.getMethod(handler, "setInvisible,func_82142_c", boolean.class).invoke(invisible);
	}

	public boolean isInvisible() {
		return (boolean) ClassStorage.getMethod(handler, "isInvisible,func_82150_aj").invoke();
	}

	public void setGravity(boolean gravity) {
		ClassStorage.getMethod(handler, "setNoGravity,func_189654_d", boolean.class).invoke(!gravity);
	}

	public boolean isGravity() {
		return !((boolean) ClassStorage.getMethod(handler, "isNoGravity,func_189652_ae").invoke());
	}
}
