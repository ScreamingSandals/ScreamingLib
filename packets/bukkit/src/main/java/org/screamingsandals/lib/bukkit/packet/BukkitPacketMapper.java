package org.screamingsandals.lib.bukkit.packet;

import org.bukkit.entity.Player;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.HashMap;
import java.util.Map;

@Service
public class BukkitPacketMapper extends PacketMapper {
    public static void init() {
        PacketMapper.init(BukkitPacketMapper::new);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C, T extends C> T createPacket0(Class<C> packetClass) {
        if (packetClass == null) {
            throw new UnsupportedOperationException("Invalid packet class provided!");
        }

        final var simpleName = packetClass.getSimpleName();
        final var packet = Reflect.construct(getClass().getPackageName() + ".Bukkit" + simpleName);
        if (packet == null) {
            throw new UnsupportedOperationException("No packet found for packet of class: " + packetClass.getSimpleName());
        }

        return (T) packet;
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
