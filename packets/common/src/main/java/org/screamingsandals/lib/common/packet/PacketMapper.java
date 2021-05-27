package org.screamingsandals.lib.common.packet;

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

    public static <C, T extends C> T createPacket(Class<C> packetClass) {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        return packetMapper.createPacket0(packetClass);
    }

    public abstract <C, T extends C> T createPacket0(Class<C> packetClass);

    public static void sendPacket(PlayerWrapper player, Object packet) {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        packetMapper.sendPacket0(player, packet);
    }

    public abstract void sendPacket0(PlayerWrapper player, Object packet);
}
