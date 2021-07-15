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
import org.screamingsandals.lib.bukkit.utils.nms.entity.EntityNMS;
import org.screamingsandals.lib.nms.accessors.EntityTypeAccessor;
import org.screamingsandals.lib.nms.accessors.MappedRegistryAccessor;
import org.screamingsandals.lib.nms.accessors.RegistryAccessor;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.InvocationResult;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ClassStorage {

	public static final boolean NMS_BASED_SERVER = safeGetClass("org.bukkit.craftbukkit.Main") != null;
	public static final boolean IS_SPIGOT_SERVER = safeGetClass("org.spigotmc.SpigotConfig") != null;
	public static final boolean IS_PAPER_SERVER = safeGetClass("com.destroystokyo.paper.PaperConfig") != null;
	public static final String NMS_VERSION = checkNMSVersion();

	public static final class NMS {
		public static final Class<?> Vector3f = safeGetClass("net.minecraft.core.Vector3f", "{nms}.Vector3f", "{f:net}.Vector3f", "{f:util}.math.vector.Vector3f");
		public static final Class<?> ItemStack = safeGetClass("net.minecraft.world.item.ItemStack", "{nms}.ItemStack", "{f:net}.ItemStack", "net.minecraft.item.ItemStack");
		public static final Class<?> ChatSerializer = safeGetClass("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer", "{nms}.IChatBaseComponent$ChatSerializer", "{nms}.ChatSerializer", "{f:util}.text.ITextComponent$Serializer");
		public static final Class<?> DataWatcher = safeGetClass("net.minecraft.network.syncher.DataWatcher", "{nms}.DataWatcher", "{f:net}.datasync.EntityDataManager");
		public static final Class<?> Entity = safeGetClass("net.minecraft.world.entity.Entity", "{nms}.Entity", "{f:ent}.Entity");
		public static final Class<?> EntityArmorStand = safeGetClass("net.minecraft.world.entity.decoration.EntityArmorStand", "{nms}.EntityArmorStand", "{f:ent}.item.ArmorStandEntity", "{f:ent}.item.EntityArmorStand");
		public static final Class<?> EntityCreature = safeGetClass("net.minecraft.world.entity.EntityCreature", "{nms}.EntityCreature", "{f:ent}.CreatureEntity", "{f:ent}.EntityCreature");
		public static final Class<?> EntityInsentient = safeGetClass("net.minecraft.world.entity.EntityInsentient", "{nms}.EntityInsentient", "{f:ent}.MobEntity", "{f:ent}.EntityLiving");
		public static final Class<?> EntityLiving = safeGetClass("net.minecraft.world.entity.EntityLiving", "{nms}.EntityLiving", "{f:ent}.LivingEntity", "{f:ent}.EntityLivingBase");
		public static final Class<?> EntityPlayer = safeGetClass("net.minecraft.server.level.EntityPlayer", "{nms}.EntityPlayer", "{f:ent}.player.ServerPlayerEntity", "{f:ent}.player.EntityPlayerMP");
		public static final Class<?> EnumChatFormat = safeGetClass("net.minecraft.EnumChatFormat", "{nms}.EnumChatFormat");
		public static final Class<?> EnumClientCommand = safeGetClass("net.minecraft.network.protocol.game.PacketPlayInClientCommand$EnumClientCommand", "{nms}.PacketPlayInClientCommand$EnumClientCommand", "{nms}.EnumClientCommand", "{f:net}.play.client.CClientStatusPacket$State", "{f:net}.play.client.CPacketClientStatus$State");
		public static final Class<?> EnumItemSlot = safeGetClass("net.minecraft.world.entity.EnumItemSlot", "{nms}.EnumItemSlot");
		public static final Class<?> EnumParticle = safeGetClass("{nms}.EnumParticle");
		public static final Class<?> EnumGamemode = safeGetClass("net.minecraft.world.level.EnumGamemode", "{nms}.WorldSettings$EnumGamemode");
		public static final Class<?> EnumDifficulty = safeGetClass("net.minecraft.world.EnumDifficulty", "{nms}.EnumDifficulty");
		public static final Class<?> EntityTypes = safeGetClass("net.minecraft.world.entity.EntityTypes", "{nms}.EntityTypes");
		public static final Class<?> EnumBossAction = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutBoss$Action","{nms}.PacketPlayOutBoss$Action");
		public static final Class<?> EnumBarStyle = safeGetClass("net.minecraft.world.BossBattle", "{nms}.BossBattle$BarStyle");
		public static final Class<?> EnumBarColor = safeGetClass("net.minecraft.world.BossBattle$BarColor", "{nms}.BossBattle$BarColor");
		public static final Class<?> EnumPlayerInfoAction = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo$EnumPlayerInfoAction", "{nms}.PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
		public static final Class<?> EnumScoreboardAction = safeGetClass("net.minecraft.server.ScoreboardServer$Action", "{nms}.ScoreboardServer$Action", "{nms}.PacketPlayOutScoreboardScore$EnumScoreboardAction");
		public static final Class<?> EnumScoreboardHealthDisplay = safeGetClass("net.minecraft.world.scores.criteria.IScoreboardCriteria$EnumScoreboardHealthDisplay", "{nms}.IScoreboardCriteria$EnumScoreboardHealthDisplay");
		public static final Class<?> EnumTitleAction = safeGetClass("{nms}.PacketPlayOutTitle$EnumTitleAction", "{nms}.EnumTitleAction", "{f:net}.play.server.STitlePacket$Type", "{f:net}.play.server.SPacketTitle$Type");
		public static final Class<?> GenericAttributes = safeGetClass("{nms}.GenericAttributes", "{f:ent}.SharedMonsterAttributes");
		public static final Class<?> IChatBaseComponent = safeGetClass("net.minecraft.network.chat.IChatBaseComponent", "{nms}.IChatBaseComponent", "{f:util}.text.ITextComponent");
		public static final Class<?> IAttribute = safeGetClass("{nms}.IAttribute", "{nms}.AttributeBase", "{f:ent}.ai.attributes.IAttribute", "{f:ent}.ai.attributes.Attribute"); // since 1.16, IAttribute no longer exists
		public static final Class<?> MinecraftServer = safeGetClass("net.minecraft.server.MinecraftServer", "{nms}.MinecraftServer", "{f:nms}.MinecraftServer");
		public static final Class<?> NBTTagCompound = safeGetClass("{nms}.NBTTagCompound", "{f:nbt}.CompoundNBT", "{f:nbt}.NBTTagCompound");
		public static final Class<?> NetworkManager = safeGetClass("net.minecraft.network.NetworkManager", "{nms}.NetworkManager", "{f:net}.NetworkManager");
		public static final Class<?> PlayerInfoData = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo$PlayerInfoData", "{nms}.PacketPlayOutPlayerInfo$PlayerInfoData");
		public static final Class<?> GameProfile = safeGetClass("com.mojang.authlib.GameProfile");

		//TODO: forge names
		public static final Class<?> DataWatcherRegistry = safeGetClass("net.minecraft.network.syncher.DataWatcherRegistry", "{nms}.DataWatcherRegistry");
		public static final Class<?> DataWatcherObject = safeGetClass("net.minecraft.network.syncher.DataWatcherObject", "{nms}.DataWatcherObject");
		public static final Class<?> DataWatcherSerializer = safeGetClass("net.minecraft.network.syncher.DataWatcherSerializer", "{nms}.DataWatcherSerializer");

		public static final Class<?> Packet = safeGetClass("net.minecraft.network.protocol.Packet", "{nms}.Packet", "{f:net}.IPacket", "{f:net}.Packet");
		public static final Class<?> PacketPlayOutEntityHeadRotation = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation", "{nms}.PacketPlayOutEntityHeadRotation", "{f:net}.play.server.SEntityHeadLookPacket", "{f:net}.play.server.SPacketEntityHeadLook");
		public static final Class<?> PacketLoginInStart = safeGetClass("net.minecraft.network.protocol.login.PacketLoginInStart", "{nms}.PacketLoginInStart", "{f:net}.login.client.CLoginStartPacket", "{f:net}.login.client.CPacketLoginStart");
		public static final Class<?> PacketPlayInClientCommand = safeGetClass("net.minecraft.network.protocol.game.PacketPlayInClientCommand", "{nms}.PacketPlayInClientCommand", "{f:net}.play.client.CClientStatusPacket", "{f:net}.play.client.CPacketClientStatus");
		public static final Class<?> PacketPlayInUseEntity = safeGetClass("net.minecraft.network.protocol.game.PacketPlayInUseEntity", "{nms}.PacketPlayInUseEntity", "{f:net}.play.client.CUseEntityPacket", "{f:net}.play.client.CPacketUseEntity");
		public static final Class<?> PacketPlayOutAbilities = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutAbilities", "{nms}.PacketPlayOutAbilities", "{f:net}.play.server.SPlayerAbilitiesPacket", "{f:net}.play.server.SPacketPlayerAbilities");
		public static final Class<?> PacketPlayOutAttachEntity = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutAttachEntity", "{nms}.PacketPlayOutAttachEntity", "{f:net}.play.server.SPacketEntityAttach", "{f:net}.play.server.SMountEntityPacket");
		public static final Class<?> PacketPlayOutBlockBreakAnimation = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation", "{nms}.PacketPlayOutBlockBreakAnimation", "{f:net}.play.server.SPacketBlockBreakAnim", "{f:net}.play.server.SAnimateBlockBreakPacket");
		public static final Class<?> PacketPlayOutBlockChange = safeGetClass(" net.minecraft.network.protocol.game.PacketPlayOutBlockChange", "{nms}.PacketPlayOutBlockChange", "{f:net}.play.server.SPacketBlockChange", "{f:net}.play.server.SChangeBlockPacket");
		public static final Class<?> PacketPlayOutExplosion = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutExplosion", "{nms}.PacketPlayOutExplosion", "{f:net}.play.server.SExplosionPacket", "{f:net}.play.server.SPacketExplosion");
		public static final Class<?> PacketPlayOutEntityStatus = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutEntityStatus", "{nms}.PacketPlayOutEntityStatus", "{f:net}.play.server.SEntityStatusPacket", "{f:net}.play.server.SPacketEntityStatus");
		public static final Class<?> PacketPlayOutEntityDestroy = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy", "{nms}.PacketPlayOutEntityDestroy", "{f:net}.play.server.SDestroyEntitiesPacket", "{f:net}.play.server.SPacketDestroyEntities");
		public static final Class<?> PacketPlayOutEntityMetadata = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata", "{nms}.PacketPlayOutEntityMetadata", "{f:net}.play.server.SEntityMetadataPacket", "{f:net}.play.server.SPacketEntityMetadata");
		public static final Class<?> PacketPlayOutEntityTeleport = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport", "{nms}.PacketPlayOutEntityTeleport", "{f:net}.play.server.SEntityTeleportPacket", "{f:net}.play.server.SPacketEntityTeleport");
		public static final Class<?> PacketPlayOutChat = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutChat", "{nms}.PacketPlayOutChat", "{f:net}.play.server.SPacketChat", "{f:net}.play.server.SChatPacket");
		public static final Class<?> PacketPlayOutExperience = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutExperience", "{nms}.PacketPlayOutExperience", "{f:net}.play.server.SSetExperiencePacket", "{f:net}.play.server.SPacketSetExperience");
		public static final Class<?> PacketPlayOutEntityEffect = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutEntityEffect", "{nms}.PacketPlayOutEntityEffect", "{f:net}.play.server.SPlayEntityEffectPacket", "{f:net}.play.server.SPacketEntityEffect");
		public static final Class<?> PacketPlayOutSpawnEntityLiving = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving", "{nms}.PacketPlayOutSpawnEntityLiving", "{f:net}.play.server.SSpawnMobPacket", "{f:net}.play.server.SPacketSpawnMob");
		public static final Class<?> PacketPlayOutSpawnEntity = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity", "{nms}.PacketPlayOutSpawnEntity", "{f:net}.play.server.SSpawnObjectPacket", "{f:net}.play.server.SPacketSpawnObject");
		public static final Class<?> PacketPlayOutScoreboardDisplayObjective = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutScoreboardDisplayObjective", "{nms}.PacketPlayOutScoreboardDisplayObjective", "{f:net}.play.server.SPacketDisplayObjective", "{f:net}.play.server.SDisplayObjectivePacket");
		public static final Class<?> PacketPlayOutScoreboardObjective = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective", "{nms}.PacketPlayOutScoreboardObjective", "{f:net}.play.server.SScoreboardObjectivePacket", "{f:net}.play.server.SPacketScoreboardObjective");
		public static final Class<?> PacketPlayOutScoreboardScore = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutScoreboardScore", "{nms}.PacketPlayOutScoreboardScore", "{f:net}.play.server.SUpdateScorePacket", "{f:net}.play.server.SPacketUpdateScore");
		public static final Class<?> PacketPlayOutScoreboardTeam = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam", "{nms}.PacketPlayOutScoreboardTeam", "{f:net}.play.server.STeamsPacket", "{f:net}.play.server.SPacketTeams");
		public static final Class<?> PacketPlayOutEntityEquipment = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment", "{nms}.PacketPlayOutEntityEquipment", "{f:net}.play.server.SEntityEquipmentPacket", "{f:net}.play.server.SPacketEntityEquipment");
		public static final Class<?> PacketPlayOutEntityVelocity = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutEntityVelocity", "{nms}.PacketPlayOutEntityVelocity", "{f:net}.play.server.SPacketEntityVelocity", "{f:net}.play.server.SEntityVelocityPacket");
		public static final Class<?> PacketPlayOutTitle = safeGetClass("{nms}.PacketPlayOutTitle", "{f:net}.play.server.STitlePacket", "{f:net}.play.server.SPacketTitle");
		public static final Class<?> PacketPlayOutWorldParticles = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutWorldParticles", "{nms}.PacketPlayOutWorldParticles", "{f:net}.play.server.SSpawnParticlePacket", "{f:net}.play.server.SPacketParticles");
		public static final Class<?> PacketPlayOutPlayerListHeaderFooter = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter", "{nms}.PacketPlayOutPlayerListHeaderFooter", "{f:net}.play.server.SPlayerListHeaderFooterPacket", "{f:net}.play.server.SPacketPlayerListHeaderFooter");
		public static final Class<?> PacketPlayOutGameStateChange = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutGameStateChange", "{nms}.PacketPlayOutGameStateChange", "{f:net}.play.server.SChangeGameStatePacket", "{f:net}.play.server.SPacketChangeGameState");
		public static final Class<?> PacketPlayOutHeldItemSlot = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutHeldItemSlot", "{nms}.PacketPlayOutHeldItemSlot", "{f:net}.play.server.SHeldItemChangePacket", "{f:net}.play.server.SPacketHeldItemChange");
		public static final Class<?> PacketPlayOutKickDisconnect = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutKickDisconnect", "{nms}.PacketPlayOutKickDisconnect", "{f:net}.play.server.SDisconnectPacket",  "{f:net}.play.server.SPacketDisconnect");
		public static final Class<?> PacketPlayOutRemoveEntityEffect = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutRemoveEntityEffect", "{nms}.PacketPlayOutRemoveEntityEffect", "{f:net}.play.server.SRemoveEntityEffectPacket", "{f:net}.play.server.SPacketRemoveEntityEffect");
		public static final Class<?> PacketPlayOutBoss = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutBoss", "{nms}.PacketPlayOutBoss", "{f:net}.play.server.SUpdateBossInfoPacket", "{f:net}.play.server.SPacketUpdateBossInfo");
		public static final Class<?> PacketPlayOutUnloadChunk = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutUnloadChunk", "{nms}.PacketPlayOutUnloadChunk", "{f:net}.play.server.SUnloadChunkPacket", "{f:net}.play.server.SPacketUnloadChunk");
		public static final Class<?> PacketPlayOutAnimation = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutAnimation", "{nms}.PacketPlayOutAnimation", "{f:net}.play.server.SAnimateHandPacket", "{f:net}.play.server.SPacketAnimation");
		public static final Class<?> PacketPlayOutBed = safeGetClass("{nms}.PacketPlayOutBed", "{f:net}.play.server.SPacketUseBed");
		public static final Class<?> PacketPlayOutBlockAction = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutBlockAction", "{nms}.PacketPlayOutBlockAction", "{f:net}.play.server.SBlockActionPacket", "{f:net}.play.server.SPacketBlockAction");
		public static final Class<?> PacketPlayOutCamera = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutCamera", "{nms}.PacketPlayOutCamera", "{f:net}.play.server.SCameraPacket", "{f:net}.play.server.SPacketCamera");
		public static final Class<?> PacketPlayOutCloseWindow = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutCloseWindow", "{nms}.PacketPlayOutCloseWindow", "{f:net}.play.server.SCloseWindowPacket", "{f:net}.play.server.SPacketCloseWindow");
		public static final Class<?> PacketPlayOutCollect = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutCollect", "{nms}.PacketPlayOutCollect", "{f:net}.play.server.SCollectItemPacket", "{f:net}.play.server.SPacketCollectItem");
		public static final Class<?> PacketPlayOutKeepAlive = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutKeepAlive", "{nms}.PacketPlayOutKeepAlive", "{f:net}.play.server.SKeepAlivePacket", "{f:net}.play.server.SPacketKeepAlive");
		public static final Class<?> PacketPlayOutNamedEntitySpawn = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn", "{nms}.PacketPlayOutNamedEntitySpawn", "{f:net}.play.server.SSpawnPlayerPacket", "{f:net}.play.server.SPacketSpawnPlayer");

		//TODO: find Forge names
		public static final Class<?> PacketPlayInUseEntityActionType = safeGetClass("net.minecraft.network.protocol.game.PacketPlayInUseEntity$b", "{nms}.PacketPlayInUseEntity$EnumEntityUseAction");
		public static final Class<?> PacketPlayOutPlayerInfo = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo", "{nms}.PacketPlayOutPlayerInfo");
		public static final Class<?> PacketPlayOutLogin = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutLogin", "{nms}.PacketPlayOutLogin");
		public static final Class<?> PathfinderGoal = safeGetClass("net.minecraft.world.entity.ai.goal.PathfinderGoal", "{nms}.PathfinderGoal", "{f:goal}.Goal", "{f:ent}.ai.EntityAIBase");
		public static final Class<?> PathfinderGoalSelector = safeGetClass("net.minecraft.world.entity.ai.goal.PathfinderGoalSelector", "{nms}.PathfinderGoalSelector", "{f:goal}.GoalSelector", "{f:ent}.ai.EntityAITasks");
		public static final Class<?> PathfinderGoalMeleeAttack = safeGetClass("net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack", "{nms}.PathfinderGoalMeleeAttack", "{f:goal}.MeleeAttackGoal", "{f:ent}.ai.EntityAIAttackMelee");
		public static final Class<?> PathfinderGoalNearestAttackableTarget = safeGetClass("net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget", "{nms}.PathfinderGoalNearestAttackableTarget", "{f:goal}.NearestAttackableTargetGoal", "{f:ent}.ai.EntityAINearestAttackableTarget");
		public static final Class<?> PlayerConnection = safeGetClass("net.minecraft.server.network.PlayerConnection", "{nms}.PlayerConnection", "{f:net}.play.ServerPlayNetHandler", "{f:net}.NetHandlerPlayServer");
		public static final Class<?> ServerConnection = safeGetClass("net.minecraft.server.network.ServerConnection", "{nms}.ServerConnection", "{f:net}.NetworkSystem");
		public static final Class<?> World = safeGetClass("net.minecraft.world.level.World", "{nms}.World", "{f:world}.World");
		public static final Class<?> WorldType = safeGetClass("{nms}.WorldType");
		public static final Class<?> CraftEquipmentSlot = safeGetClass("{obc}.CraftEquipmentSlot");
		public static final Class<?> CraftItemStack = safeGetClass("{obc}.inventory.CraftItemStack");
		public static final Class<?> CraftMagicNumbers = safeGetClass("{obc}.util.CraftMagicNumbers");
		public static final Class<?> CraftVector = safeGetClass("{obc}.util.CraftVector");
		public static final Class<?> BlockPosition = safeGetClass("net.minecraft.core.BlockPosition", "{nms}.BlockPosition");
		public static final Class<?> IRegistry = safeGetClass("net.minecraft.core.IRegistry", "{nms}.IRegistry", "{f:util}.registry.Registry");
		public static final Class<?> PacketPlayOutEntityLook = safeGetClass("{nms}.PacketPlayOutEntity$PacketPlayOutEntityLook", "net.minecraft.network.protocol.game.PacketPlayOutEntity$PacketPlayOutEntityLook");
		// 1.16
		public static final Class<?> AttributeModifiable = safeGetClass("net.minecraft.world.entity.ai.attributes.AttributeModifiable", "{nms}.AttributeModifiable", "{f:ent}.ai.attributes.ModifiableAttributeInstance");

		public static final Class<?> CraftPersistentDataContainer = safeGetClass("{obc}.persistence.CraftPersistentDataContainer");
		public static final Class<?> CraftMetaItem = safeGetClass("{obc}.inventory.CraftMetaItem");
		public static final Class<?> CraftPersistentDataTypeRegistry = safeGetClass("{obc}.persistence.CraftPersistentDataTypeRegistry");

		//TODO: forge mappings
		// 1.17
		public static final Class<?> ChatMessageType = safeGetClass("net.minecraft.network.chat.ChatMessageType");
		public static final Class<?> PacketPlayOutScoreboardTeamData = safeGetClass("net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam$b");
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
				.getField("connection,playerConnection,field_71135_a,b");
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
				// 1.17
				return NMS.PathfinderGoalSelector.getConstructor(Supplier.class).newInstance(Reflect.getMethod(world, "getMethodProfilerSupplier").invoke());
			} catch (Throwable ignored) {
				try {
					// 1.16
					return NMS.PathfinderGoalSelector.getConstructor(Supplier.class).newInstance((Supplier<?>) () -> getMethodProfiler(world));
				} catch (Throwable ignore) {
					// Pre 1.16
					return NMS.PathfinderGoalSelector.getConstructors()[0].newInstance(getMethodProfiler(world));
				}
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
					(float) invoker.fastInvoke("getX,func_179415_b"),
					(float) invoker.fastInvoke("getY,func_179416_c"),
					(float) invoker.fastInvoke("getZ,func_179413_d")
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
		return Reflect.getMethod(handler, "getDataWatcher,func_184212_Q,ad").invoke();
	}

	public static int getEntityTypeId(EntityNMS entityNMS) {
		Preconditions.checkNotNull(entityNMS, "Entity is null!");
		final var entity_type_field = Reflect.getField(ClassStorage.NMS.IRegistry, "ENTITY_TYPE,field_212629_r,Y,f_122826_");
		if (entityNMS.getEntityType() != null && entity_type_field != null) {
			return (int) Reflect.getMethod(entity_type_field, "a,getId,func_148757_b,m_7447_", Object.class)
					.invoke(entityNMS.getEntityType());
		} else {
			var result = Reflect.getMethod(NMS.EntityTypes, "a,func_75619_a", NMS.Entity).invokeStatic(entityNMS.getHandler());
			if (result instanceof Number) {
				return ((Number) result).intValue();
			} else {
				return (int) Reflect.getFieldResulted(NMS.EntityTypes, "b").getMethod("a,func_148757_b", Object.class).invoke(entityNMS.getHandler().getClass());
			}
		}
	}

	public static int getEntityTypeId(String key, Class<?> clazz) {
		var registry = Reflect.getFieldResulted(RegistryAccessor.getFieldENTITY_TYPE());

		if (registry.isPresent()) {
			// 1.14+
			var optional = Reflect.fastInvoke(EntityTypeAccessor.getMethodByString1(), key);
			if (optional instanceof Optional) {
				return registry.fastInvokeResulted(RegistryAccessor.getMethodGetId1(), ((Optional<?>) optional).orElse(null)).asOptional(Integer.class).orElse(0);
			}

			// 1.13.X
			var nullable = Reflect.fastInvoke(EntityTypeAccessor.getMethodFunc_200713_a1(), key);
			return registry.fastInvokeResulted(RegistryAccessor.getMethodGetId1(), nullable).asOptional(Integer.class).orElse(0);
		} else {
			// 1.11 - 1.12.2
			if (EntityTypeAccessor.getFieldField_191308_b() != null) {
				return Reflect.getFieldResulted(EntityTypeAccessor.getFieldField_191308_b()).fastInvokeResulted(MappedRegistryAccessor.getMethodFunc_148757_b1(), clazz).asOptional(Integer.class).orElse(0);
			}

			// 1.9.4 - 1.10.2
			return (int) Reflect.getFieldResulted(EntityTypeAccessor.getFieldField_75624_e()).as(Map.class).get(clazz);
		}
	}
}
