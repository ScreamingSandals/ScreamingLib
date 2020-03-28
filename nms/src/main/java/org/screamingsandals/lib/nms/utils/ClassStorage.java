package org.screamingsandals.lib.nms.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassStorage {

	public static final boolean NMS_BASED_SERVER = getClassSafe("org.bukkit.craftbukkit.Main") != null;
	public static final boolean IS_SPIGOT_SERVER = getClassSafe("org.spigotmc.SpigotConfig") != null;
	public static final boolean IS_PAPER_SERVER = getClassSafe("com.destroystokyo.paper.PaperConfig") != null;
	public static final String NMS_VERSION = checkNMSVersion();

	public static final class NMS {
		public static final Class<?> ChatSerializer = getClassSafe("{nms}.IChatBaseComponent$ChatSerializer", "{nms}.ChatSerializer", "{f:util}.text.ITextComponent$Serializer");
		public static final Class<?> DataWatcher = getClassSafe("{nms}.DataWatcher", "{f:net}.datasync.EntityDataManager");
		public static final Class<?> Entity = getClassSafe("{nms}.Entity", "{f:ent}.Entity");
		public static final Class<?> EntityArmorStand = getClassSafe("{nms}.EntityArmorStand", "{f:ent}.item.ArmorStandEntity", "{f:ent}.item.EntityArmorStand");
		public static final Class<?> EntityCreature = getClassSafe("{nms}.EntityCreature", "{f:ent}.CreatureEntity", "{f:ent}.EntityCreature");
		public static final Class<?> EntityInsentient = getClassSafe("{nms}.EntityInsentient", "{f:ent}.MobEntity", "{f:ent}.EntityLiving");
		public static final Class<?> EntityLiving = getClassSafe("{nms}.EntityLiving", "{f:ent}.LivingEntity", "{f:ent}.EntityLivingBase");
		public static final Class<?> EntityPlayer = getClassSafe("{nms}.EntityPlayer", "{f:ent}.player.ServerPlayerEntity", "{f:ent}.player.EntityPlayerMP");
		public static final Class<?> EnumClientCommand = getClassSafe("{nms}.PacketPlayInClientCommand$EnumClientCommand", "{nms}.EnumClientCommand", "{f:net}.play.client.CClientStatusPacket$State", "{f:net}.play.client.CPacketClientStatus$State");
		public static final Class<?> EnumParticle = getClassSafe("{nms}.EnumParticle");
		public static final Class<?> EnumTitleAction = getClassSafe("{nms}.PacketPlayOutTitle$EnumTitleAction", "{nms}.EnumTitleAction", "{f:net}.play.server.STitlePacket$Type", "{f:net}.play.server.SPacketTitle$Type");
		public static final Class<?> GenericAttributes = getClassSafe("{nms}.GenericAttributes", "{f:ent}.SharedMonsterAttributes");
		public static final Class<?> IChatBaseComponent = getClassSafe("{nms}.IChatBaseComponent", "{f:util}.text.ITextComponent");
		public static final Class<?> IAttribute = getClassSafe("{nms}.IAttribute", "{f:ent}.ai.attributes.IAttribute");
		public static final Class<?> MinecraftServer = getClassSafe("{nms}.MinecraftServer", "{f:nms}.MinecraftServer");
		public static final Class<?> NBTTagCompound = getClassSafe("{nms}.NBTTagCompound", "{f:nbt}.CompoundNBT", "{f:nbt}.NBTTagCompound");
		public static final Class<?> NetworkManager = getClassSafe("{nms}.NetworkManager", "{f:net}.NetworkManager");
		public static final Class<?> Packet = getClassSafe("{nms}.Packet", "{f:net}.IPacket", "{f:net}.Packet");
		public static final Class<?> PacketLoginInStart = getClassSafe("{nms}.PacketLoginInStart", "{f:net}.login.client.CLoginStartPacket", "{f:net}.login.client.CPacketLoginStart");
		public static final Class<?> PacketPlayInClientCommand = getClassSafe("{nms}.PacketPlayInClientCommand", "{f:net}.play.client.CClientStatusPacket", "{f:net}.play.client.CPacketClientStatus");
		public static final Class<?> PacketPlayInUseEntity = getClassSafe("{nms}.PacketPlayInUseEntity", "{f:net}.play.client.CUseEntityPacket", "{f:net}.play.client.CPacketUseEntity");
		public static final Class<?> PacketPlayOutEntityDestroy = getClassSafe("{nms}.PacketPlayOutEntityDestroy", "{f:net}.play.server.SDestroyEntitiesPacket", "{f:net}.play.server.SPacketDestroyEntities");
		public static final Class<?> PacketPlayOutEntityMetadata = getClassSafe("{nms}.PacketPlayOutEntityMetadata", "{f:net}.play.server.SEntityMetadataPacket", "{f:net}.play.server.SPacketEntityMetadata");
		public static final Class<?> PacketPlayOutEntityTeleport = getClassSafe("{nms}.PacketPlayOutEntityTeleport", "{f:net}.play.server.SEntityTeleportPacket", "{f:net}.play.server.SPacketEntityTeleport");
		public static final Class<?> PacketPlayOutExperience = getClassSafe("{nms}.PacketPlayOutExperience", "{f:net}.play.server.SSetExperiencePacket", "{f:net}.play.server.SPacketSetExperience");
		public static final Class<?> PacketPlayOutSpawnEntityLiving = getClassSafe("{nms}.PacketPlayOutSpawnEntityLiving", "{f:net}.play.server.SSpawnMobPacket", "{f:net}.play.server.SPacketSpawnMob");
		public static final Class<?> PacketPlayOutTitle = getClassSafe("{nms}.PacketPlayOutTitle", "{f:net}.play.server.STitlePacket", "{f:net}.play.server.SPacketTitle");
		public static final Class<?> PacketPlayOutWorldParticles = getClassSafe("{nms}.PacketPlayOutWorldParticles", "{f:net}.play.server.SSpawnParticlePacket", "{f:net}.play.server.SPacketParticles");
		public static final Class<?> PathfinderGoal = getClassSafe("{nms}.PathfinderGoal", "{f:goal}.Goal", "{f:ent}.ai.EntityAIBase");
		public static final Class<?> PathfinderGoalSelector = getClassSafe("{nms}.PathfinderGoalSelector", "{f:goal}.GoalSelector", "{f:ent}.ai.EntityAITasks");
		public static final Class<?> PathfinderGoalMeleeAttack = getClassSafe("{nms}.PathfinderGoalMeleeAttack", "{f:goal}.MeleeAttackGoal", "{f:ent}.ai.EntityAIAttackMelee");
		public static final Class<?> PathfinderGoalNearestAttackableTarget = getClassSafe("{nms}.PathfinderGoalNearestAttackableTarget", "{f:goal}.NearestAttackableTargetGoal", "{f:ent}.ai.EntityAINearestAttackableTarget");
		public static final Class<?> PlayerConnection = getClassSafe("{nms}.PlayerConnection", "{f:net}.play.ServerPlayNetHandler", "{f:net}.NetHandlerPlayServer");
		public static final Class<?> ServerConnection = getClassSafe("{nms}.ServerConnection", "{f:net}.NetworkSystem");
		public static final Class<?> World = getClassSafe("{nms}.World", "{f:world}.World");
	}
	
	private static String checkNMSVersion() {
		/* if NMS is not found, finding class will fail, but we still need some string */
		String nmsVersion = "nms_not_found"; 
		
		if (NMS_BASED_SERVER) {
			String packName = Bukkit.getServer().getClass().getPackage().getName();
			nmsVersion = packName.substring(packName.lastIndexOf('.') + 1);
		}
		
		return nmsVersion;
	}
	
	public static Class<?> getClassSafe(String... clazz) {
		for (String claz : clazz) {
			try {
				return Class.forName(claz
					// CRAFTBUKKIT/SPIGOT/PAPER
					.replace("{obc}", "org.bukkit.craftbukkit." + NMS_VERSION)
					.replace("{nms}", "net.minecraft.server." + NMS_VERSION)
					// CAULDRON BASED
					.replace("{f:ent}", "net.minecraft.entity")
					.replace("{f:goal}", "net.minecraft.entity.ai.goal")
					.replace("{f:nbt}", "net.minecraft.nbt")
					.replace("{f:net}", "net.minecraft.network")
					.replace("{f:nms}", "net.minecraft.server")
					.replace("{f:util}", "net.minecraft.util")
					.replace("{f:world}", "net.minecraft.world")
				);
			} catch (ClassNotFoundException ignored) {
			}
		}
		return null;
	}
	
	public static ClassMethod getMethod(String className, String names, Class<?>... params) {
		return getMethod(getClassSafe(className), names.split(","), params);
	}
	
	public static ClassMethod getMethod(Class<?> clazz, String names, Class<?>... params) {
		return getMethod(clazz, names.split(","), params); 
	}
	
	public static ClassMethod getMethod(String className, String[] names, Class<?>... params) {
		return getMethod(getClassSafe(className), names, params);
	}
	
	public static ClassMethod getMethod(Class<?> clazz, String[] names, Class<?>... params) {
		for (String name : names) {
			try {
				Method method = clazz.getMethod(name.trim(), params);
				return new ClassMethod(method);
			} catch (Throwable t) {
				Class<?> claz2 = clazz;
				do {
					try {
						Method method = claz2.getDeclaredMethod(name.trim(), params);
						method.setAccessible(true);
						return new ClassMethod(method);
					} catch (Throwable t2) {
					}
				} while ((claz2 = claz2.getSuperclass()) != null && claz2 != Object.class);
			}
		}
		return new ClassMethod(null);
	}
	public static InstanceMethod getMethod(Object instance, String names, Class<?>...params) {
		ClassMethod method = getMethod(instance.getClass(), names.split(","), params);
		return new InstanceMethod(instance, method.getReflectedMethod());
	}
	
	public static InstanceMethod getMethod(Object instance, String[] names, Class<?>...params) {
		ClassMethod method = getMethod(instance.getClass(), names, params);
		return new InstanceMethod(instance, method.getReflectedMethod());
	}
	
	public static Object getField(Object instance, String names) {
		return getField(instance.getClass(), names.split(","), instance);
	}
	
	public static Object getField(Object instance, String[] names) {
		return getField(instance.getClass(), names, instance);
	}
	
	public static Object getField(Class<?> clazz, String names) {
		return getField(clazz, names.split(","), null);
	}
	
	public static Object getField(Class<?> clazz, String[] names) {
		return getField(clazz, names, null);
	}

	public static Object getField(Class<?> clazz, String names, Object instance) {
		return getField(clazz, names.split(","), instance);
	}
	
	public static Object getField(Class<?> clazz, String[] names, Object instance) {
		for (String name : names) {
			try {
				Field field = clazz.getField(name.trim());
				Object result = field.get(instance);
				return result;
			} catch (Throwable t) {
				Class<?> claz2 = clazz;
				do {
					try {
						Field field = claz2.getDeclaredField(name.trim());
						field.setAccessible(true);
						Object result = field.get(instance);
						return result;
					} catch (Throwable t2) {
					}
				} while ((claz2 = claz2.getSuperclass()) != null && claz2 != Object.class);
			}
		}
		return null;
	}
	
	public static Object setField(Object instance, String names, Object set) {
		return setField(instance.getClass(), names.split(","), instance, set);
	}
	
	public static Object setField(Object instance, String[] names, Object set) {
		return setField(instance.getClass(), names, instance, set);
	}
	
	public static Object setField(Class<?> clazz, String names, Object set) {
		return setField(clazz, names.split(","), null, set);
	}
	
	public static Object setField(Class<?> clazz, String[] names, Object set) {
		return setField(clazz, names, null, set);
	}

	public static Object setField(Class<?> clazz, String names, Object instance, Object set) {
		return setField(clazz, names.split(","), instance, set);
	}
	
	public static Object setField(Class<?> clazz, String[] names, Object instance, Object set) {
		for (String name : names) {
			try {
				Field field = clazz.getField(name.trim());
				field.set(instance, set);
				Object result = field.get(instance);
				return result;
			} catch (Throwable t) {
				Class<?> claz2 = clazz;
				do {
					try {
						Field field = claz2.getDeclaredField(name.trim());
						field.setAccessible(true);
						field.set(instance, set);
						Object result = field.get(instance);
						return result;
					} catch (Throwable t2) {
					}
				} while ((claz2 = claz2.getSuperclass()) != null && claz2 != Object.class);
			}
		}
		return null;
	}
	
	public static Object getHandle(Object obj) {
		return getMethod(obj, "getHandle").invoke();
	}
	
	public static Object getPlayerConnection(Player player) {
		Object handler = getMethod(player, "getHandle").invoke();
		if (handler != null) {
			return getField(handler, "playerConnection,field_71135_a");
		}
		return null;
	}
	
	public static boolean sendPacket(Player player, Object packet) {
		if (!NMS.Packet.isInstance(packet)) {
			return false;
		}
		Object connection = getPlayerConnection(player);
		if (connection != null) {
			getMethod(connection, "sendPacket,func_147359_a", NMS.Packet).invoke(packet);
			return true;
		}
		return false;
	}

	public static Object findEnumConstant(Class<?> enumClass, String constantNames) {
		return findEnumConstant(enumClass, constantNames.split(","));
	}
	
	public static Object findEnumConstant(Class<?> enumClass, String[] constantNames) {
		Object[] enums = enumClass.getEnumConstants();
		if (enums != null) {
			for (Object enumeration : enums) {
				Object name = getMethod(enumeration, "name").invoke();
				for (String constant : constantNames) {
					if (constant.equals(name)) {
						return enumeration;
					}
				}
			}
		}
		return null;
	}

	public static Object getMethodProfiler(World world) {
		return getMethodProfiler(getHandle(world));
	}

	public static Object getMethodProfiler(Object handler) {
		Object methodProfiler = getMethod(handler, "getMethodProfiler,func_217381_Z").invoke();
		if (methodProfiler == null) {
			methodProfiler = getField(handler, "methodProfiler,field_72984_F");
		}
		return methodProfiler;
	}
	
	public static Object obtainNewPathfinderSelector(Object handler) {
		try {
			Object world = getMethod(handler, "getWorld,func_130014_f_").invoke();
			return NMS.PathfinderGoalSelector.getConstructors()[0].newInstance(getMethodProfiler(world));
		} catch (Throwable t) {
		}
		return null;
	}
}
