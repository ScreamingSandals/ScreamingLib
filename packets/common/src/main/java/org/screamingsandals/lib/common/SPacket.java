package org.screamingsandals.lib.common;
import org.screamingsandals.lib.player.PlayerWrapper;

public interface SPacket {
    void sendPacket(PlayerWrapper player);

    Object getRawPacket();
}
