package org.screamingsandals.lib.common;

import org.jetbrains.annotations.NotNull;
import java.util.function.Supplier;

public abstract class PacketMapper {
    private static PacketMapper packetMapper = null;

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
}
