package org.screamingsandals.lib.packet;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

import java.util.function.Supplier;

/**
 *  Represents the PacketMapper instance which is useful for sending {@link AbstractPacket} to Player connections.
 */
@AbstractService
@ServiceDependencies(dependsOn = {
        ProtocolInjector.class
})
public abstract class PacketMapper {
    private static PacketMapper packetMapper = null;

    /**
     * Returns if this PacketMapper instance has been initialized.
     *
     * @return true if the PacketMapper instance has been initialized, false otherwise
     */
    public static boolean isInitialized() {
        return packetMapper != null;
    }

    /**
     * Initializes this PacketMapper instance with a PacketMapper that is provided by a {@link Supplier}
     *
     * @param packetMapperSupplier the provider for this PacketMapper instance
     */
    public static void init(@NotNull Supplier<PacketMapper> packetMapperSupplier) {
        if (packetMapper != null) {
            throw new UnsupportedOperationException("PacketMapper is already initialized.");
        }
        packetMapper = packetMapperSupplier.get();
    }

    /**
     * Sends the packet to the client the player is currently connected with.
     *
     * @param player the player to send the packet to
     * @param packet the packet instance to send the player
     */
    public static void sendPacket(PlayerWrapper player, AbstractPacket packet) {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        packetMapper.sendPacket0(player, packet);
    }

    /**
     * Platform specific method that sends the packet to the client the player is currently connected with.
     *
     * @param player the player to send the packet to
     * @param packet the packet instance to send the player
     */
    public abstract void sendPacket0(PlayerWrapper player, AbstractPacket packet);

    public static int getId(Class<? extends AbstractPacket> clazz) {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        return packetMapper.getId0(clazz);
    }

    public abstract int getId0(Class<? extends AbstractPacket> clazz);
}
