package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.Collection;

public abstract class AbstractPacket {

    public abstract void write(PacketWriter writer);

    public void sendPacket(PlayerWrapper player) {
        PacketMapper.sendPacket(player, this);
    }

    public void sendPacket(Collection<PlayerWrapper> players) {
        players.forEach(this::sendPacket);
    }
}
