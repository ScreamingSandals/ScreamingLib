package org.screamingsandals.lib.bukkit.utils.nms;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ClassStorage {

	public static final boolean NMS_BASED_SERVER = safeGetClass("org.bukkit.craftbukkit.Main") != null;
	public static final boolean IS_SPIGOT_SERVER = safeGetClass("org.spigotmc.SpigotConfig") != null;
	public static final boolean IS_PAPER_SERVER = safeGetClass("com.destroystokyo.paper.PaperConfig") != null;
	public static final String NMS_VERSION = checkNMSVersion();

	public static final class NMS {
		public static final Class<?> Vector3f = safeGetClass("{nms}.Vector3f", "{f:net}.Vector3f");
		public static final Class<?> ItemStack = safeGetClass("{nms}.ItemStack", "{f:net}.ItemStack");
		public static final Class<?> ChatSerializer = safeGetClass("{nms}.IChatBaseComponent$ChatSerializer", "{nms}.ChatSerializer", "{f:util}.text.ITextComponent$Serializer");
		public static final Class<?> DataWatcher = safeGetClass("{nms}.DataWatcher", "{f:net}.datasync.EntityDataManager");
		public static final Class<?> Entity = safeGetClass("{nms}.Entity", "{f:ent}.Entity");
		public static final Class<?> EntityArmorStand = safeGetClass("{nms}.EntityArmorStand", "{f:ent}.item.ArmorStandEntity", "{f:ent}.item.EntityArmorStand");
		public static final Class<?> EntityCreature = safeGetClass("{nms}.EntityCreature", "{f:ent}.CreatureEntity", "{f:ent}.EntityCreature");
		public static final Class<?> EntityInsentient = safeGetClass("{nms}.EntityInsentient", "{f:ent}.MobEntity", "{f:ent}.EntityLiving");
		public static final Class<?> EntityLiving = safeGetClass("{nms}.EntityLiving", "{f:ent}.LivingEntity", "{f:ent}.EntityLivingBase");
		public static final Class<?> EntityPlayer = safeGetClass("{nms}.EntityPlayer", "{f:ent}.player.ServerPlayerEntity", "{f:ent}.player.EntityPlayerMP");
		public static final Class<?> EnumChatFormat = safeGetClass("{nms}.EnumChatFormat");
		public static final Class<?> EnumClientCommand = safeGetClass("{nms}.PacketPlayInClientCommand$EnumClientCommand", "{nms}.EnumClientCommand", "{f:net}.play.client.CClientStatusPacket$State", "{f:net}.play.client.CPacketClientStatus$State");
		public static final Class<?> EnumItemSlot = safeGetClass("{nms}.EnumItemSlot");
		public static final Class<?> EnumParticle = safeGetClass("{nms}.EnumParticle");
		public static final Class<?> EnumGamemode = safeGetClass("{nms}.WorldSettings$EnumGamemode");
		public static final Class<?> EnumDifficulty = safeGetClass("{nms}.EnumDifficulty");
		public static final Class<?> EntityTypes = safeGetClass("{nms}.EntityTypes");
		public static final Class<?> EnumBossAction = safeGetClass("{nms}.PacketPlayOutBoss$Action");
		public static final Class<?> EnumBarStyle = safeGetClass("{nms}.BossBattle$BarStyle");
		public static final Class<?> EnumBarColor = safeGetClass("{nms}.BossBattle$BarColor");
		public static final Class<?> EnumPlayerInfoAction = safeGetClass("{nms}.PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
		public static final Class<?> EnumScoreboardAction = safeGetClass("{nms}.ScoreboardServer$Action", "{nms}.PacketPlayOutScoreboardScore$EnumScoreboardAction");
		public static final Class<?> EnumScoreboardHealthDisplay = safeGetClass("{nms}.IScoreboardCriteria$EnumScoreboardHealthDisplay");
		public static final Class<?> EnumTitleAction = safeGetClass("{nms}.PacketPlayOutTitle$EnumTitleAction", "{nms}.EnumTitleAction", "{f:net}.play.server.STitlePacket$Type", "{f:net}.play.server.SPacketTitle$Type");
		public static final Class<?> GenericAttributes = safeGetClass("{nms}.GenericAttributes", "{f:ent}.SharedMonsterAttributes");
		public static final Class<?> IChatBaseComponent = safeGetClass("{nms}.IChatBaseComponent", "{f:util}.text.ITextComponent");
		public static final Class<?> IAttribute = safeGetClass("{nms}.IAttribute", "{nms}.AttributeBase", "{f:ent}.ai.attributes.IAttribute", "{f:ent}.ai.attributes.Attribute"); // since 1.16, IAttribute no longer exists
		public static final Class<?> MinecraftServer = safeGetClass("{nms}.MinecraftServer", "{f:nms}.MinecraftServer");
		public static final Class<?> NBTTagCompound = safeGetClass("{nms}.NBTTagCompound", "{f:nbt}.CompoundNBT", "{f:nbt}.NBTTagCompound");
		public static final Class<?> NetworkManager = safeGetClass("{nms}.NetworkManager", "{f:net}.NetworkManager");
		public static final Class<?> PlayerInfoData = safeGetClass("{nms}.PlayerInfoData");
		public static final Class<?> GameProfile = safeGetClass("com.mojang.authlib.GameProfile");

		//TODO: forge names
		public static final Class<?> DataWatcherRegistry = safeGetClass("{nms}.DataWatcherRegistry");
		public static final Class<?> DataWatcherObject = safeGetClass("{nms}.DataWatcherObject");
		public static final Class<?> DataWatcherSerializer = safeGetClass("{nms}.DataWatcherSerializer");

		public static final Class<?> Packet = safeGetClass("{nms}.Packet", "{f:net}.IPacket", "{f:net}.Packet");
		public static final Class<?> PacketPlayOutEntityHeadRotation = safeGetClass("{nms}.PacketPlayOutEntityHeadRotation", "{f:net}.play.server.SEntityHeadLookPacket", "{f:net}.play.server.SPacketEntityHeadLook");
		public static final Class<?> PacketLoginInStart = safeGetClass("{nms}.PacketLoginInStart", "{f:net}.login.client.CLoginStartPacket", "{f:net}.login.client.CPacketLoginStart");
		public static final Class<?> PacketPlayInClientCommand = safeGetClass("{nms}.PacketPlayInClientCommand", "{f:net}.play.client.CClientStatusPacket", "{f:net}.play.client.CPacketClientStatus");
		public static final Class<?> PacketPlayInUseEntity = safeGetClass("{nms}.PacketPlayInUseEntity", "{f:net}.play.client.CUseEntityPacket", "{f:net}.play.client.CPacketUseEntity");
		public static final Class<?> PacketPlayOutAbilities = safeGetClass("{nms}.PacketPlayOutAbilities", "{f:net}.play.server.SPlayerAbilitiesPacket", "{f:net}.play.server.SPacketPlayerAbilities");
		public static final Class<?> PacketPlayOutAttachEntity = safeGetClass("{nms}.PacketPlayOutAttachEntity", "{f:net}.play.server.SPacketEntityAttach", "{f:net}.play.server.SMountEntityPacket");
		public static final Class<?> PacketPlayOutBlockBreakAnimation = safeGetClass("{nms}.PacketPlayOutBlockBreakAnimation", "{f:net}.play.server.SPacketBlockBreakAnim", "{f:net}.play.server.SAnimateBlockBreakPacket");
		public static final Class<?> PacketPlayOutBlockChange = safeGetClass("{nms}.PacketPlayOutBlockChange", "{f:net}.play.server.SPacketBlockChange", "{f:net}.play.server.SChangeBlockPacket");
		public static final Class<?> PacketPlayOutExplosion = safeGetClass("{nms}.PacketPlayOutExplosion", "{f:net}.play.server.SExplosionPacket", "{f:net}.play.server.SPacketExplosion");
		public static final Class<?> PacketPlayOutEntityStatus = safeGetClass("{nms}.PacketPlayOutEntityStatus", "{f:net}.play.server.SEntityStatusPacket", "{f:net}.play.server.SPacketEntityStatus");
		public static final Class<?> PacketPlayOutEntityDestroy = safeGetClass("{nms}.PacketPlayOutEntityDestroy", "{f:net}.play.server.SDestroyEntitiesPacket", "{f:net}.play.server.SPacketDestroyEntities");
		public static final Class<?> PacketPlayOutEntityMetadata = safeGetClass("{nms}.PacketPlayOutEntityMetadata", "{f:net}.play.server.SEntityMetadataPacket", "{f:net}.play.server.SPacketEntityMetadata");
		public static final Class<?> PacketPlayOutEntityTeleport = safeGetClass("{nms}.PacketPlayOutEntityTeleport", "{f:net}.play.server.SEntityTeleportPacket", "{f:net}.play.server.SPacketEntityTeleport");
		public static final Class<?> PacketPlayOutChat = safeGetClass("{nms}.PacketPlayOutChat", "{f:net}.play.server.SPacketChat", "{f:net}.play.server.SChatPacket");
		public static final Class<?> PacketPlayOutExperience = safeGetClass("{nms}.PacketPlayOutExperience", "{f:net}.play.server.SSetExperiencePacket", "{f:net}.play.server.SPacketSetExperience");
		public static final Class<?> PacketPlayOutEntityEffect = safeGetClass("{nms}.PacketPlayOutEntityEffect", "{f:net}.play.server.SPlayEntityEffectPacket", "{f:net}.play.server.SPacketEntityEffect");
		public static final Class<?> PacketPlayOutSpawnEntityLiving = safeGetClass("{nms}.PacketPlayOutSpawnEntityLiving", "{f:net}.play.server.SSpawnMobPacket", "{f:net}.play.server.SPacketSpawnMob");
		public static final Class<?> PacketPlayOutSpawnEntity = safeGetClass("{nms}.PacketPlayOutSpawnEntity", "{f:net}.play.server.SSpawnObjectPacket", "{f:net}.play.server.SPacketSpawnObject");
		public static final Class<?> PacketPlayOutScoreboardDisplayObjective = safeGetClass("{nms}.PacketPlayOutScoreboardDisplayObjective", "{f:net}.play.server.SPacketDisplayObjective", "{f:net}.play.server.SDisplayObjectivePacket");
		public static final Class<?> PacketPlayOutScoreboardObjective = safeGetClass("{nms}.PacketPlayOutScoreboardObjective", "{f:net}.play.server.SScoreboardObjectivePacket", "{f:net}.play.server.SPacketScoreboardObjective");
		public static final Class<?> PacketPlayOutScoreboardScore = safeGetClass("{nms}.PacketPlayOutScoreboardScore", "{f:net}.play.server.SUpdateScorePacket", "{f:net}.play.server.SPacketUpdateScore");
		public static final Class<?> PacketPlayOutScoreboardTeam = safeGetClass("{nms}.PacketPlayOutScoreboardTeam", "{f:net}.play.server.STeamsPacket", "{f:net}.play.server.SPacketTeams");
		public static final Class<?> PacketPlayOutEntityEquipment = safeGetClass("{nms}.PacketPlayOutEntityEquipment", "{f:net}.play.server.SEntityEquipmentPacket", "{f:net}.play.server.SPacketEntityEquipment");
		public static final Class<?> PacketPlayOutEntityVelocity = safeGetClass("{nms}.PacketPlayOutEntityVelocity", "{f:net}.play.server.SPacketEntityVelocity", "{f:net}.play.server.SEntityVelocityPacket");
		public static final Class<?> PacketPlayOutTitle = safeGetClass("{nms}.PacketPlayOutTitle", "{f:net}.play.server.STitlePacket", "{f:net}.play.server.SPacketTitle");
		public static final Class<?> PacketPlayOutWorldParticles = safeGetClass("{nms}.PacketPlayOutWorldParticles", "{f:net}.play.server.SSpawnParticlePacket", "{f:net}.play.server.SPacketParticles");
		public static final Class<?> PacketPlayOutPlayerListHeaderFooter = safeGetClass("{nms}.PacketPlayOutPlayerListHeaderFooter", "{f:net}.play.server.SPlayerListHeaderFooterPacket", "{f:net}.play.server.SPacketPlayerListHeaderFooter");
		public static final Class<?> PacketPlayOutGameStateChange = safeGetClass("{nms}.PacketPlayOutGameStateChange", "{f:net}.play.server.SChangeGameStatePacket", "{f:net}.play.server.SPacketChangeGameState");
		public static final Class<?> PacketPlayOutHeldItemSlot = safeGetClass("{nms}.PacketPlayOutHeldItemSlot", "{f:net}.play.server.SHeldItemChangePacket", "{f:net}.play.server.SPacketHeldItemChange");
		public static final Class<?> PacketPlayOutKickDisconnect = safeGetClass("{nms}.PacketPlayOutKickDisconnect", "{f:net}.play.server.SDisconnectPacket",  "{f:net}.play.server.SPacketDisconnect");
		public static final Class<?> PacketPlayOutRemoveEntityEffect = safeGetClass("{nms}.PacketPlayOutRemoveEntityEffect", "{f:net}.play.server.SRemoveEntityEffectPacket", "{f:net}.play.server.SPacketRemoveEntityEffect");
		public static final Class<?> PacketPlayOutBoss = safeGetClass("{nms}.PacketPlayOutBoss", "{f:net}.play.server.SUpdateBossInfoPacket", "{f:net}.play.server.SPacketUpdateBossInfo");
		public static final Class<?> PacketPlayOutUnloadChunk = safeGetClass("{nms}.PacketPlayOutUnloadChunk", "{f:net}.play.server.SUnloadChunkPacket", "{f:net}.play.server.SPacketUnloadChunk");
		public static final Class<?> PacketPlayOutAnimation = safeGetClass("{nms}.PacketPlayOutAnimation", "{f:net}.play.server.SAnimateHandPacket", "{f:net}.play.server.SPacketAnimation");
		public static final Class<?> PacketPlayOutBed = safeGetClass("{nms}.PacketPlayOutBed", "{f:net}.play.server.SPacketUseBed");
		public static final Class<?> PacketPlayOutBlockAction = safeGetClass("{nms}.PacketPlayOutBlockAction", "{f:net}.play.server.SBlockActionPacket", "{f:net}.play.server.SPacketBlockAction");
		public static final Class<?> PacketPlayOutCamera = safeGetClass("{nms}.PacketPlayOutCamera", "{f:net}.play.server.SCameraPacket", "{f:net}.play.server.SPacketCamera");
		public static final Class<?> PacketPlayOutCloseWindow = safeGetClass("{nms}.PacketPlayOutCloseWindow", "{f:net}.play.server.SCloseWindowPacket", "{f:net}.play.server.SPacketCloseWindow");
		public static final Class<?> PacketPlayOutCollect = safeGetClass("{nms}.PacketPlayOutCollect", "{f:net}.play.server.SCollectItemPacket", "{f:net}.play.server.SPacketCollectItem");
		public static final Class<?> PacketPlayOutKeepAlive = safeGetClass("{nms}.PacketPlayOutKeepAlive", "{f:net}.play.server.SKeepAlivePacket", "{f:net}.play.server.SPacketKeepAlive");
		public static final Class<?> PacketPlayOutNamedEntitySpawn = safeGetClass("{nms}.PacketPlayOutNamedEntitySpawn", "{f:net}.play.server.SSpawnPlayerPacket", "{f:net}.play.server.SPacketSpawnPlayer");
		//TODO: find Forge names
		public static final Class<?> PacketPlayOutLogin = safeGetClass("{nms}.PacketPlayOutLogin");
		public static final Class<?> PathfinderGoal = safeGetClass("{nms}.PathfinderGoal", "{f:goal}.Goal", "{f:ent}.ai.EntityAIBase");
		public static final Class<?> PathfinderGoalSelector = safeGetClass("{nms}.PathfinderGoalSelector", "{f:goal}.GoalSelector", "{f:ent}.ai.EntityAITasks");
		public static final Class<?> PathfinderGoalMeleeAttack = safeGetClass("{nms}.PathfinderGoalMeleeAttack", "{f:goal}.MeleeAttackGoal", "{f:ent}.ai.EntityAIAttackMelee");
		public static final Class<?> PathfinderGoalNearestAttackableTarget = safeGetClass("{nms}.PathfinderGoalNearestAttackableTarget", "{f:goal}.NearestAttackableTargetGoal", "{f:ent}.ai.EntityAINearestAttackableTarget");
		public static final Class<?> PlayerConnection = safeGetClass("{nms}.PlayerConnection", "{f:net}.play.ServerPlayNetHandler", "{f:net}.NetHandlerPlayServer");
		public static final Class<?> ServerConnection = safeGetClass("{nms}.ServerConnection", "{f:net}.NetworkSystem");
		public static final Class<?> World = safeGetClass("{nms}.World", "{f:world}.World");
		public static final Class<?> WorldType = safeGetClass("{nms}.WorldType");
		public static final Class<?> CraftEquipmentSlot = safeGetClass("{obc}.CraftEquipmentSlot");
		public static final Class<?> CraftItemStack = safeGetClass("{obc}.inventory.CraftItemStack");
		public static final Class<?> CraftMagicNumbers = safeGetClass("{obc}.util.CraftMagicNumbers");
		public static final Class<?> CraftVector = safeGetClass("{obc}.CraftVector");
		public static final Class<?> BlockPosition = safeGetClass("{nms}.BlockPosition");
		// 1.16
		public static final Class<?> AttributeModifiable = safeGetClass("{nms}.AttributeModifiable", "{f:ent}.ai.attributes.ModifiableAttributeInstance");

		public static final Class<?> CraftPersistentDataContainer = safeGetClass("{obc}.persistence.CraftPersistentDataContainer");
		public static final Class<?> CraftMetaItem = safeGetClass("{obc}.inventory.CraftMetaItem");
		public static final Class<?> CraftPersistentDataTypeRegistry = safeGetClass("{obc}.persistence.CraftPersistentDataTypeRegistry");
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
	
	public static Class<?> safeGetClass(String... clazz) {
		return Reflect.getClassSafe(
				Map.of(
						"{obc}", "org.bukkit.craftbukkit." + NMS_VERSION,
						"{nms}", "net.minecraft.server." + NMS_VERSION,
						"{f:ent}", "net.minecraft.entity",
						"{f:goal}", "net.minecraft.entity.ai.goal",
						"{f:nbt}", "net.minecraft.nbt",
						"{f:net}", "net.minecraft.network",
						"{f:nms}", "net.minecraft.server",
						"{f:util}", "net.minecraft.util",
						"{f:world}", "net.minecraft.world"
				),
				clazz
		);
	}
	
	public static Object getHandle(Object obj) {
		return Reflect.getMethod(obj, "getHandle").invoke();
	}
	
	public static Object getPlayerConnection(Player player) {
		return Reflect
				.getMethod(player, "getHandle")
				.invokeResulted()
				.getField("connection,playerConnection,field_71135_a");
	}
	
	public static boolean sendPacket(Player player, Object packet) {
		return sendPackets(player, List.of(packet));
	}

	public static boolean sendPackets(Player player, List<Object> packets) {
		var connection = getPlayerConnection(player);
		if (connection != null) {
			packets.forEach(packet -> {
				if (!NMS.Packet.isInstance(packet)) {
					return;
				}

				Reflect.getMethod(connection, "sendPacket,func_147359_a", NMS.Packet).invoke(packet);
			});
			return true;
		}
		return false;
	}

	public static Object getMethodProfiler(World world) {
		return getMethodProfiler(getHandle(world));
	}

	public static Object getMethodProfiler(Object handler) {
		Object methodProfiler = Reflect.getMethod(handler, "getMethodProfiler,func_217381_Z").invoke();
		if (methodProfiler == null) {
			methodProfiler = Reflect.getField(handler, "methodProfiler,field_72984_F");
		}
		return methodProfiler;
	}
	
	public static Object obtainNewPathfinderSelector(Object handler) {
		try {
			Object world = Reflect.getMethod(handler, "getWorld,func_130014_f_").invoke();
			try {
				// 1.16
				return NMS.PathfinderGoalSelector.getConstructor(Supplier.class).newInstance((Supplier<?>) () -> getMethodProfiler(world));
			} catch (Throwable ignored) {
				// Pre 1.16
				return NMS.PathfinderGoalSelector.getConstructors()[0].newInstance(getMethodProfiler(world));
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public static Object getVectorToNMS(Vector3Df vector3f) {
		try {
			return Reflect.constructor(NMS.Vector3f, float.class, float.class, float.class).construct(vector3f.getX(), vector3f.getY(), vector3f.getZ());
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	public static Vector3Df getVectorFromNMS(Object vector3f) {
		Preconditions.checkNotNull(vector3f, "Vector is null!");
		var invoker = new InvocationResult(vector3f);
		try {
			return new Vector3Df(
					(float) invoker.fastInvoke("getX"),
					(float) invoker.fastInvoke("getY"),
					(float) invoker.fastInvoke("getZ")
			);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public static Object asMinecraftComponent(Component component) {
		try {
			return MinecraftComponentSerializer.get().serialize(component);
		} catch (Exception ignored) { // current Adventure is facing some weird bug on non-adventure native server software, let's do temporary workaround
			return Reflect.getMethod(ClassStorage.NMS.ChatSerializer, "a,field_150700_a", String.class)
					.invokeStatic(GsonComponentSerializer.gson().serialize(component));
		}
	}

	public static Object stackAsNMS(ItemStack item) {
		Preconditions.checkNotNull(item, "Item is null!");
		return Reflect.getMethod(ClassStorage.NMS.CraftItemStack, "asNMSCopy", ItemStack.class).invokeStatic(item);
	}

	public static Object getDataWatcher(Object handler) {
		Preconditions.checkNotNull(handler, "Handler is null!");
		return Reflect.getMethod(handler, "getDataWatcher,func_184212_Q").invoke();
	}

	public static Object getGameProfile(HumanEntity entity) {
		Preconditions.checkNotNull(entity, "Entity is null!");
		final var handle = getHandle(entity);
		return Reflect.getMethod(handle, "getProfile").invoke();
	}
}
