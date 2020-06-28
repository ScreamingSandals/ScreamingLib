package org.screamingsandals.lib.nms.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Supplier;
import org.screamingsandals.lib.reflection.Reflection;


public class ClassStorage {

	public static final boolean NMS_BASED_SERVER = getNMSClassSafe("org.bukkit.craftbukkit.Main") != null;
	public static final String NMS_VERSION = checkNMSVersion();

	public static final class NMS {
		public static final Class<?> ChatSerializer = getNMSClassSafe("{nms}.IChatBaseComponent$ChatSerializer", "{nms}.ChatSerializer", "{f:util}.text.ITextComponent$Serializer");
		public static final Class<?> DataWatcher = getNMSClassSafe("{nms}.DataWatcher", "{f:net}.datasync.EntityDataManager");
		public static final Class<?> Entity = getNMSClassSafe("{nms}.Entity", "{f:ent}.Entity");
		public static final Class<?> EntityArmorStand = getNMSClassSafe("{nms}.EntityArmorStand", "{f:ent}.item.ArmorStandEntity", "{f:ent}.item.EntityArmorStand");
		public static final Class<?> EntityCreature = getNMSClassSafe("{nms}.EntityCreature", "{f:ent}.CreatureEntity", "{f:ent}.EntityCreature");
		public static final Class<?> EntityInsentient = getNMSClassSafe("{nms}.EntityInsentient", "{f:ent}.MobEntity", "{f:ent}.EntityLiving");
		public static final Class<?> EntityLiving = getNMSClassSafe("{nms}.EntityLiving", "{f:ent}.LivingEntity", "{f:ent}.EntityLivingBase");
		public static final Class<?> EntityPlayer = getNMSClassSafe("{nms}.EntityPlayer", "{f:ent}.player.ServerPlayerEntity", "{f:ent}.player.EntityPlayerMP");
		public static final Class<?> EnumClientCommand = getNMSClassSafe("{nms}.PacketPlayInClientCommand$EnumClientCommand", "{nms}.EnumClientCommand", "{f:net}.play.client.CClientStatusPacket$State", "{f:net}.play.client.CPacketClientStatus$State");
		public static final Class<?> EnumParticle = getNMSClassSafe("{nms}.EnumParticle");
		public static final Class<?> EnumItemSlot = getNMSClassSafe("{nms}.EnumItemSlot");
		public static final Class<?> EnumTitleAction = getNMSClassSafe("{nms}.PacketPlayOutTitle$EnumTitleAction", "{nms}.EnumTitleAction", "{f:net}.play.server.STitlePacket$Type", "{f:net}.play.server.SPacketTitle$Type");
		public static final Class<?> GenericAttributes = getNMSClassSafe("{nms}.GenericAttributes", "{f:ent}.SharedMonsterAttributes");
		public static final Class<?> IChatBaseComponent = getNMSClassSafe("{nms}.IChatBaseComponent", "{f:util}.text.ITextComponent");
		public static final Class<?> IAttribute = getNMSClassSafe("{nms}.IAttribute", "{nms}.AttributeBase", "{f:ent}.ai.attributes.IAttribute", "{f:ent}.ai.attributes.Attribute"); // since 1.16, IAttribute no longer exists
		public static final Class<?> ItemStack = getNMSClassSafe("{nms}.ItemStack");
		public static final Class<?> CraftItemStack = getNMSClassSafe("{obc}.inventory.CraftItemStack");
		public static final Class<?> MinecraftServer = getNMSClassSafe("{nms}.MinecraftServer", "{f:nms}.MinecraftServer");
		public static final Class<?> NBTTagCompound = getNMSClassSafe("{nms}.NBTTagCompound", "{f:nbt}.CompoundNBT", "{f:nbt}.NBTTagCompound");
		public static final Class<?> NetworkManager = getNMSClassSafe("{nms}.NetworkManager", "{f:net}.NetworkManager");
		public static final Class<?> Packet = getNMSClassSafe("{nms}.Packet", "{f:net}.IPacket", "{f:net}.Packet");
		public static final Class<?> PacketLoginInStart = getNMSClassSafe("{nms}.PacketLoginInStart", "{f:net}.login.client.CLoginStartPacket", "{f:net}.login.client.CPacketLoginStart");
		public static final Class<?> PacketPlayInClientCommand = getNMSClassSafe("{nms}.PacketPlayInClientCommand", "{f:net}.play.client.CClientStatusPacket", "{f:net}.play.client.CPacketClientStatus");
		public static final Class<?> PacketPlayInUseEntity = getNMSClassSafe("{nms}.PacketPlayInUseEntity", "{f:net}.play.client.CUseEntityPacket", "{f:net}.play.client.CPacketUseEntity");
		public static final Class<?> PacketPlayOutEntityDestroy = getNMSClassSafe("{nms}.PacketPlayOutEntityDestroy", "{f:net}.play.server.SDestroyEntitiesPacket", "{f:net}.play.server.SPacketDestroyEntities");
		public static final Class<?> PacketPlayOutEntityMetadata = getNMSClassSafe("{nms}.PacketPlayOutEntityMetadata", "{f:net}.play.server.SEntityMetadataPacket", "{f:net}.play.server.SPacketEntityMetadata");
		public static final Class<?> PacketPlayOutEntityTeleport = getNMSClassSafe("{nms}.PacketPlayOutEntityTeleport", "{f:net}.play.server.SEntityTeleportPacket", "{f:net}.play.server.SPacketEntityTeleport");
		public static final Class<?> PacketPlayOutExperience = getNMSClassSafe("{nms}.PacketPlayOutExperience", "{f:net}.play.server.SSetExperiencePacket", "{f:net}.play.server.SPacketSetExperience");
		public static final Class<?> PacketPlayOutSpawnEntityLiving = getNMSClassSafe("{nms}.PacketPlayOutSpawnEntityLiving", "{f:net}.play.server.SSpawnMobPacket", "{f:net}.play.server.SPacketSpawnMob");
		public static final Class<?> PacketPlayOutTitle = getNMSClassSafe("{nms}.PacketPlayOutTitle", "{f:net}.play.server.STitlePacket", "{f:net}.play.server.SPacketTitle");
		public static final Class<?> PacketPlayOutWorldParticles = getNMSClassSafe("{nms}.PacketPlayOutWorldParticles", "{f:net}.play.server.SSpawnParticlePacket", "{f:net}.play.server.SPacketParticles");
		public static final Class<?> PathfinderGoal = getNMSClassSafe("{nms}.PathfinderGoal", "{f:goal}.Goal", "{f:ent}.ai.EntityAIBase");
		public static final Class<?> PathfinderGoalSelector = getNMSClassSafe("{nms}.PathfinderGoalSelector", "{f:goal}.GoalSelector", "{f:ent}.ai.EntityAITasks");
		public static final Class<?> PathfinderGoalMeleeAttack = getNMSClassSafe("{nms}.PathfinderGoalMeleeAttack", "{f:goal}.MeleeAttackGoal", "{f:ent}.ai.EntityAIAttackMelee");
		public static final Class<?> PathfinderGoalNearestAttackableTarget = getNMSClassSafe("{nms}.PathfinderGoalNearestAttackableTarget", "{f:goal}.NearestAttackableTargetGoal", "{f:ent}.ai.EntityAINearestAttackableTarget");
		public static final Class<?> PlayerConnection = getNMSClassSafe("{nms}.PlayerConnection", "{f:net}.play.ServerPlayNetHandler", "{f:net}.NetHandlerPlayServer");
		public static final Class<?> ServerConnection = getNMSClassSafe("{nms}.ServerConnection", "{f:net}.NetworkSystem");
		public static final Class<?> World = getNMSClassSafe("{nms}.World", "{f:world}.World");

		// 1.16 only (ok, it was here before, but before we don't have to use it)
		public static final Class<?> AttributeModifiable = getNMSClassSafe("{nms}.AttributeModifiable", "{f:ent}.ai.attributes.ModifiableAttributeInstance");
	}
	
	private static String checkNMSVersion() {
		/* if NMS is not found, finding class will fail, but we still need some string */
		var nmsVersion = "nms_not_found";
		
		if (NMS_BASED_SERVER) {
			var packName = Bukkit.getServer().getClass().getPackage().getName();
			nmsVersion = packName.substring(packName.lastIndexOf('.') + 1);
		}
		
		return nmsVersion;
	}
	
	public static Class<?> getNMSClassSafe(String... clazz) {
		return Reflection.getClassSafe(Map.of("{obc}", "org.bukkit.craftbukkit." + NMS_VERSION, "{nms}", "net.minecraft.server." + NMS_VERSION,
				"{f:ent}", "net.minecraft.entity", "{f:goal}", "net.minecraft.entity.ai.goal", "{f:nbt}", "net.minecraft.nbt", "{f:net}", "net.minecraft.network",
				"{f:nms}", "net.minecraft.server", "{f:util}", "net.minecraft.util", "{f:world}", "net.minecraft.world"), clazz);
	}

	public static Object getHandle(Object obj) {
		return Reflection.fastInvoke(obj, "getHandle");
	}
	
	public static Object getPlayerConnection(Player player) {
		var handler = Reflection.fastInvoke(player, "getHandle");
		if (handler != null) {
			return Reflection.getField(handler, "playerConnection,field_71135_a");
		}
		return null;
	}
	
	public static boolean sendPacket(Player player, Object packet) {
		if (!NMS.Packet.isInstance(packet)) {
			return false;
		}
		var connection = getPlayerConnection(player);
		if (connection != null) {
			Reflection.getMethod(connection, "sendPacket,func_147359_a", NMS.Packet).invoke(packet);
			return true;
		}
		return false;
	}

	public static Object getMethodProfiler(World world) {
		return getMethodProfiler(getHandle(world));
	}

	public static Object getMethodProfiler(Object handler) {
		var methodProfiler = Reflection.fastInvoke(handler, "getMethodProfiler,func_217381_Z");
		if (methodProfiler == null) {
			methodProfiler = Reflection.getField(handler, "methodProfiler,field_72984_F");
		}
		return methodProfiler;
	}
	
	public static Object obtainNewPathfinderSelector(Object handler) {
		try {
			var world = Reflection.fastInvoke(handler, "getWorld,func_130014_f_");
			try {
				// 1.16
				return NMS.PathfinderGoalSelector.getConstructor(Supplier.class).newInstance((Supplier<?>) () -> getMethodProfiler(world));
			} catch (Throwable ignored) {
				// Pre 1.16
				return NMS.PathfinderGoalSelector.getConstructors()[0].newInstance(getMethodProfiler(world));
			}
		} catch (Throwable t) {
		}
		return null;
	}

	//TODO: NMS item stacks
}
