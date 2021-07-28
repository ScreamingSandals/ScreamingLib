package org.screamingsandals.lib.packet;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.function.Supplier;

@AbstractService
public abstract class PacketMapper {
    private static PacketMapper packetMapper = null;

    public static boolean isInitialized() {
        return packetMapper != null;
    }

    public static void init(@NotNull Supplier<PacketMapper> packetMapperSupplier) {
        if (packetMapper != null) {
            throw new UnsupportedOperationException("PacketMapper is already initialized.");
        }
        packetMapper = packetMapperSupplier.get();
    }

    public static void sendPacket(PlayerWrapper player, AbstractPacket packet) {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        packetMapper.sendPacket0(player, packet);
    }

    public abstract void sendPacket0(PlayerWrapper player, AbstractPacket packet);
}
