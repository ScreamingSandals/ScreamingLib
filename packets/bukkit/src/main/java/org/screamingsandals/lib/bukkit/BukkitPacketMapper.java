package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.common.PacketMapper;
import org.screamingsandals.lib.common.SPacketPlayOutScoreboardObjective;
import org.screamingsandals.lib.common.SPacketPlayOutScoreboardScore;
import java.util.HashMap;
import java.util.Map;

public class BukkitPacketMapper extends PacketMapper{
    public static void init() {
        PacketMapper.init(BukkitPacketMapper::new);
    }

    //TODO: find alternative solution
    protected Map<Class<?>, BukkitSPacket> packetConverters = new HashMap<>();

    public BukkitPacketMapper() {
        packetConverters
                .put(SPacketPlayOutScoreboardScore.class, new BukkitSPacketPlayOutScoreboardObjective());
        packetConverters
                .put(SPacketPlayOutScoreboardObjective.class, new BukkitSPacketPlayOutScoreboardObjective());
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
        return (T) packet;
    }
}
