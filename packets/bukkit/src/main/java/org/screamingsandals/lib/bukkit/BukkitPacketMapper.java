package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.common.*;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class BukkitPacketMapper extends PacketMapper {
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
        packetConverters
                .put(SPacketPlayOutEntityDestroy.class, unused -> new BukkitSPacketPlayOutEntityDestroy());
        packetConverters
                .put(SPacketPlayOutSpawnEntityLiving.class, unused -> new BukkitSPacketPlayOutSpawnEntityLiving());
        packetConverters
                .put(SPacketPlayOutSpawnEntity.class, unused -> new BukkitSPacketPlayOutSpawnEntity());
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
