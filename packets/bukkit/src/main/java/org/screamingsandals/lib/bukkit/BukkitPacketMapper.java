package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BukkitPacketMapper extends PacketMapper{
    public static void init() {
        PacketMapper.init(BukkitPacketMapper::new);
    }

    //TODO: find alternative solution
    protected Map<Class<?>, Function<Void, BukkitSPacket>> packetConverters = new HashMap<>();

    public BukkitPacketMapper() {
        packetConverters
                .put(SPacketPlayOutScoreboardScore.class, unused -> new BukkitSPacketPlayOutScoreboardObjective());
        packetConverters
                .put(SPacketPlayOutScoreboardObjective.class, unused ->  new BukkitSPacketPlayOutScoreboardObjective());
        packetConverters
                .put(SPacketPlayOutScoreboardDisplayObjective.class, unused -> new BukkitSPacketPlayOutScoreboardDisplayObjective());
        packetConverters
                .put(SPacketPlayOutEntityTeleport.class, unused -> new BukkitSPacketPlayOutEntityTeleport());
        packetConverters
                .put(SPacketPlayOutEntityEquipment.class, unused -> new BukkitSPacketPlayOutEntityEquipment());
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
        return (T) packet.apply(null);
    }
}
