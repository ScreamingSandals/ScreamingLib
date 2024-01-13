/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.bukkit.utils.nms;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.nms.accessors.*;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@UtilityClass
public class ClassStorage {
	public static final @NotNull String CB_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

	// CraftBukkit classes
	@UtilityClass
	public static final class CB {
		public static final Class<?> CraftAttributeMap = Reflect.getClassSafe(CB_PACKAGE + ".attribute.CraftAttributeMap");
		public static final Class<?> CraftItemStack = Reflect.getClassSafe(CB_PACKAGE + ".inventory.CraftItemStack");
		public static final Class<?> CraftMagicNumbers = Reflect.getClassSafe(CB_PACKAGE + ".util.CraftMagicNumbers");
		public static final Class<?> CraftVector = Reflect.getClassSafe(CB_PACKAGE + ".util.CraftVector");
		public static final Class<?> CraftPersistentDataContainer = Reflect.getClassSafe(CB_PACKAGE + ".persistence.CraftPersistentDataContainer");
		public static final Class<?> CraftCustomItemTagContainer = Reflect.getClassSafe(CB_PACKAGE + ".inventory.tags.CraftCustomItemTagContainer");
		public static final Class<?> CraftMetaItem = Reflect.getClassSafe(CB_PACKAGE + ".inventory.CraftMetaItem");
		public static final Class<?> CraftPersistentDataTypeRegistry = Reflect.getClassSafe(CB_PACKAGE + ".persistence.CraftPersistentDataTypeRegistry");
		public static final Class<?> CraftCustomTagTypeRegistry = Reflect.getClassSafe(CB_PACKAGE + ".inventory.CraftCustomTagTypeRegistry");
		public static final Class<?> CraftSound = Reflect.getClassSafe(CB_PACKAGE + ".CraftSound");
	}
	
	public static Object getHandle(Object obj) {
		return Reflect.getMethod(obj, "getHandle").invoke();
	}

	public static Object getHandleOfItemStack(Object obj) {
		return Reflect.getField(obj, "handle");
	}
	
	public static Object getPlayerConnection(Player player) {
		return Reflect
				.getMethod(player, "getHandle")
				.invokeResulted()
				.getField(ServerPlayerAccessor.getFieldConnection());
	}

	public static Object getMethodProfiler(World world) {
		return getMethodProfiler(getHandle(world));
	}

	public static Object getMethodProfiler(Object handler) {
		Object methodProfiler = Reflect.fastInvoke(handler, LevelAccessor.getMethodGetProfiler1());
		if (methodProfiler == null) {
			methodProfiler = Reflect.getField(handler, LevelAccessor.getFieldField_72984_F());
		}
		return methodProfiler;
	}

	public static Object obtainNewPathfinderSelector(Object handler) {
		try {
			Object world = Reflect.fastInvoke(handler, EntityAccessor.getMethodGetCommandSenderWorld1());
			try {
				// 1.17
				return GoalSelectorAccessor.getConstructor0().newInstance(Reflect.fastInvoke(world, LevelAccessor.getMethodGetProfilerSupplier1()));
			} catch (Throwable ignored) {
				try {
					// 1.16
					return GoalSelectorAccessor.getConstructor0().newInstance((Supplier<?>) () -> getMethodProfiler(world));
				} catch (Throwable ignore) {
					// Pre 1.16
					return GoalSelectorAccessor.getType().getConstructors()[0].newInstance(getMethodProfiler(world));
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}


	public static Object getVectorToNMS(Vector3Df vector3f) {
		try {
			return RotationsAccessor.getConstructor0().newInstance(vector3f.getX(), vector3f.getY(), vector3f.getZ());
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	public static Vector3Df getVectorFromNMS(Object vector3f) {
		Preconditions.checkNotNull(vector3f, "Vector is null!");
		try {
			return new Vector3Df(
					(float) Reflect.fastInvoke(vector3f, RotationsAccessor.getMethodGetX1()),
					(float) Reflect.fastInvoke(vector3f, RotationsAccessor.getMethodGetY1()),
					(float) Reflect.fastInvoke(vector3f, RotationsAccessor.getMethodGetZ1())
			);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * Unlike packets module, this method just send packet created by NMS itself (used by Spectator so packets module is not required by core)
	 */
	public static boolean sendNMSConstructedPacket(Player player, Object packet) {
		if (!PacketAccessor.getType().isInstance(packet)) {
			return false;
		}
		Object connection = getPlayerConnection(player);
		if (connection != null) {
			Reflect.fastInvoke(connection, ServerCommonPacketListenerImplAccessor.getMethodSend1() != null ? ServerCommonPacketListenerImplAccessor.getMethodSend1() /* 1.20.2+ */: ServerGamePacketListenerImplAccessor.getMethodSend1() /* <= 1.20.1 */, packet);
			return true;
		}
		return false;
	}

	public static @NotNull Object asMinecraftComponent(@NotNull Component component) {
		return asMinecraftComponent(component.toJavaJson());
	}

	public static @NotNull Object asMinecraftComponent(@NotNull String javaJson) {
		if (Component_i_SerializerAccessor.getMethodM_130701_1() != null) { // 1.16.1+
			return Reflect.fastInvoke(Component_i_SerializerAccessor.getMethodM_130701_1(), (Object) javaJson);
		} else {
			return Reflect.fastInvoke(Component_i_SerializerAccessor.getMethodFunc_150699_a1(), (Object) javaJson);
		}
	}

	public static Object stackAsNMS(ItemStack item) {
		Preconditions.checkNotNull(item, "Item is null!");
		return Reflect.getMethod(CB.CraftItemStack, "asNMSCopy", ItemStack.class).invokeStatic(item);
	}

	public static ItemStack asCBStack(ItemStack item) {
		Preconditions.checkNotNull(item, "Item is null!");
		return (ItemStack) Reflect.getMethod(CB.CraftItemStack, "asCraftCopy", ItemStack.class).invokeStatic(item);
	}

	public static Object getDataWatcher(Object handler) {
		Preconditions.checkNotNull(handler, "Handler is null!");
		return Reflect.fastInvoke(handler, EntityAccessor.getMethodGetEntityData1());
	}

	public static int getEntityTypeId(String key, Class<?> clazz) {
		var registry1_19_3 = Reflect.getFieldResulted(BuiltInRegistriesAccessor.getFieldENTITY_TYPE());
		if (registry1_19_3.isPresent()) {
			// 1.19.3+
			var optional = Reflect.fastInvoke(EntityTypeAccessor.getMethodByString1(), (Object) key);

			return registry1_19_3.fastInvokeResulted(RegistryAccessor.getMethodGetId1(), ((Optional<?>) optional).orElse(null)).asOptional(Integer.class).orElse(0);
		} else {
			// <= 1.19.2
			var registry = Reflect.getFieldResulted(RegistryAccessor.getFieldENTITY_TYPE());

			if (registry.isPresent()) {
				// 1.14+
				var optional = Reflect.fastInvoke(EntityTypeAccessor.getMethodByString1(), (Object) key);

				if (optional instanceof Optional) {
					return registry.fastInvokeResulted(RegistryAccessor.getMethodGetId1(), ((Optional<?>) optional).orElse(null)).asOptional(Integer.class).orElse(0);
				}

				// 1.13.X
				var nullable = Reflect.fastInvoke(EntityTypeAccessor.getMethodFunc_200713_a1(), (Object) key);
				return registry.fastInvokeResulted(RegistryAccessor.getMethodGetId1(), nullable).asOptional(Integer.class).orElse(0);
			} else {
				// 1.11 - 1.12.2
				if (EntityTypeAccessor.getFieldField_191308_b() != null) {
					return Reflect.getFieldResulted(EntityTypeAccessor.getFieldField_191308_b()).fastInvokeResulted(MappedRegistryAccessor.getMethodFunc_148757_b1(), clazz).asOptional(Integer.class).orElse(0);
				}

				// 1.8.8 - 1.10.2
				return (int) Reflect.getFieldResulted(EntityTypeAccessor.getFieldField_75624_e()).as(Map.class).get(clazz);
			}
		}
	}
}
