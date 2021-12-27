package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.player.PlayerWrapper;
import java.util.Collection;

/**
 * Represents a packet which can be sent to a player using {@link PacketMapper#sendPacket(PlayerWrapper, AbstractPacket)} or {@link AbstractPacket#sendPacket(PlayerWrapper)}.
 */
public abstract class AbstractPacket {

    /**
     * Writes the contents of this packet to an {@link PacketWriter} instance.
     *
     * Note that the writer must have written the packet id at the header before this method is called,
     * packets may not be functional otherwise.
     *
     * @param writer the writer to populate packet contents
     */
    public abstract void write(PacketWriter writer);

    /**
     * Sends the packet to the client the player is currently connected with.
     *
     * @param player the player to send the packet to
     */
    public void sendPacket(PlayerWrapper player) {
        PacketMapper.sendPacket(player, this);
    }

    /**
     * Sends the same packet to multiple players of the list provided.
     *
     * @param players the players to send the packet to
     */
    public void sendPacket(Collection<PlayerWrapper> players) {
        players.forEach(this::sendPacket);
    }

    /**
     * Gets the id of this packet.
     *
     * @return the id of this packet
     */
    public int getId() {
        return PacketMapper.getId(this.getClass());
    }
}
