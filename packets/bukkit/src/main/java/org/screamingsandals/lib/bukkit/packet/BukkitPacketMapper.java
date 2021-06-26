package org.screamingsandals.lib.bukkit.packet;

import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@Service
public class BukkitPacketMapper extends PacketMapper {
    public static void init() {
        PacketMapper.init(BukkitPacketMapper::new);
    }

    //TODO: find alternative solution
    protected Map<Class<?>, Class<? extends BukkitSPacket>> packetConverters = new HashMap<>();

    public BukkitPacketMapper() {
        packetConverters
                .put(SPacketPlayOutScoreboardScore.class, BukkitSPacketPlayOutScoreboardScore.class);
        packetConverters
                .put(SPacketPlayOutScoreboardObjective.class, BukkitSPacketPlayOutScoreboardObjective.class);
        packetConverters
                .put(SPacketPlayOutScoreboardDisplayObjective.class, BukkitSPacketPlayOutScoreboardDisplayObjective.class);
        packetConverters
                .put(SPacketPlayOutEntityTeleport.class, BukkitSPacketPlayOutEntityTeleport.class);
        packetConverters
                .put(SPacketPlayOutEntityEquipment.class, BukkitSPacketPlayOutEntityEquipment.class);
        packetConverters
                .put(SPacketPlayOutEntityDestroy.class, BukkitSPacketPlayOutEntityDestroy.class);
        packetConverters
                .put(SPacketPlayOutSpawnEntityLiving.class, BukkitSPacketPlayOutSpawnEntityLiving.class);
        packetConverters
                .put(SPacketPlayOutSpawnEntity.class, BukkitSPacketPlayOutSpawnEntity.class);
        packetConverters
                .put(SPacketPlayOutEntityStatus.class, BukkitSPacketPlayOutEntityStatus.class);
        packetConverters
                .put(SPacketPlayOutExplosion.class, BukkitSPacketPlayOutExplosion.class);
        packetConverters
                .put(SPacketPlayOutEntityHeadRotation.class, BukkitSPacketPlayOutEntityHeadRotation.class);
        packetConverters
                .put(SPacketPlayOutEntityEffect.class, BukkitSPacketPlayOutEntityEffect.class);
        packetConverters
                .put(SPacketPlayOutAttachEntity.class, BukkitSPacketPlayOutAttachEntity.class);
        packetConverters
                .put(SPacketPlayOutBlockBreakAnimation.class, BukkitSPacketPlayOutBlockBreakAnimation.class);
        packetConverters
                .put(SPacketPlayOutBlockChange.class, BukkitSPacketPlayOutBlockChange.class);
        packetConverters
                .put(SPacketPlayOutChat.class, BukkitSPacketPlayOutChat.class);
        packetConverters
                .put(SPacketPlayOutEntityMetadata.class, BukkitSPacketPlayOutEntityMetadata.class);
        packetConverters
                .put(SPacketPlayOutPlayerListHeaderFooter.class, BukkitSPacketPlayOutPlayerListHeaderFooter.class);
        packetConverters
                .put(SPacketPlayOutEntityVelocity.class, BukkitSPacketPlayOutEntityVelocity.class);
        packetConverters
                .put(SPacketPlayOutExperience.class, BukkitSPacketPlayOutExperience.class);
        packetConverters
                .put(SPacketPlayOutGameStateChange.class, BukkitSPacketPlayOutGameStateChange.class);
        packetConverters
                .put(SPacketPlayOutKickDisconnect.class, BukkitSPacketPlayOutKickDisconnect.class);
        packetConverters
                .put(SPacketPlayOutRemoveEntityEffect.class, BukkitSPacketPlayOutRemoveEntityEffect.class);
        packetConverters
                .put(SPacketPlayOutScoreboardTeam.class, BukkitSPacketPlayOutScoreboardTeam.class);
        packetConverters
                .put(SPacketPlayOutHeldItemSlot.class, BukkitSPacketPlayOutHeldItemSlot.class);
        packetConverters
                .put(SPacketPlayOutBoss.class, BukkitSPacketPlayOutBoss.class);
        packetConverters
                .put(SPacketPlayOutUnloadChunk.class, BukkitSPacketPlayOutUnloadChunk.class);
        packetConverters
                .put(SPacketPlayOutAbilities.class, BukkitSPacketPlayOutAbilities.class);
        packetConverters
                .put(SPacketPlayOutAnimation.class, BukkitSPacketPlayOutAnimation.class);
        packetConverters
                .put(SPacketPlayOutBlockAction.class, BukkitSPacketPlayOutBlockAction.class);
        packetConverters
                .put(SPacketPlayOutCamera.class, BukkitSPacketPlayOutCamera.class);
        packetConverters
                .put(SPacketPlayOutCloseWindow.class, BukkitSPacketPlayOutCloseWindow.class);
        packetConverters
                .put(SPacketPlayOutCollect.class, BukkitSPacketPlayOutCollect.class);
        packetConverters
                .put(SPacketPlayOutKeepAlive.class, BukkitSPacketPlayOutKeepAlive.class);
        packetConverters
                .put(SPacketPlayOutLogin.class, BukkitSPacketPlayOutLogin.class);
        packetConverters
                .put(SPacketPlayOutNamedEntitySpawn.class, BukkitSPacketPlayOutNamedEntitySpawn.class);
        packetConverters
                .put(SPacketPlayOutPlayerInfo.class, BukkitSPacketPlayOutPlayerInfo.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C, T extends C> T createPacket0(Class<C> packetClass) {
        if (packetClass == null) {
            throw new UnsupportedOperationException("Invalid packet class provided!");
        }
        final var packet = packetConverters.get(packetClass);
        if (packet == null) {
            throw new UnsupportedOperationException("No packet found for packet of class: " + packetClass.getSimpleName());
        }

        return (T) Reflect.construct(packet);
    }

    @Override
    public void sendPacket0(PlayerWrapper player, Object packet) {
        if (packet == null) {
            throw new UnsupportedOperationException("Packet cannot be null!");
        }
        if (player == null) {
            throw new UnsupportedOperationException("Player cannot be null!");
        }
        if (packet instanceof SPacket) {
            final var sPacket = (SPacket) packet;
            sPacket.sendPacket(player);
            return;
        }
        ClassStorage.sendPacket(player.as(Player.class), packet);
    }

}
