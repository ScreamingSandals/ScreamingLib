package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

/**
 *  Represents the PacketMapper instance which is useful for sending {@link AbstractPacket} to Player connections.
 */
@AbstractService
@ServiceDependencies(dependsOn = {
        ProtocolInjector.class
})
public abstract class PacketMapper {
    private static PacketMapper packetMapper = null;

    public PacketMapper() {
        if (packetMapper != null) {
            throw new UnsupportedOperationException("PacketMapper is already initialized.");
        }
        packetMapper = this;
    }

    /**
     * Returns if this PacketMapper instance has been initialized.
     *
     * @return true if the PacketMapper instance has been initialized, false otherwise
     */
    public static boolean isInitialized() {
        return packetMapper != null;
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

    public static int getProtocolVersion(PlayerWrapper player) {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        if (player == null || !player.isOnline()) {
            throw new UnsupportedOperationException("Invalid player provided!");
        }
        return packetMapper.getProtocolVersion0(player);
    }

    public abstract int getId0(Class<? extends AbstractPacket> clazz);

    public abstract int getProtocolVersion0(PlayerWrapper player);
}
