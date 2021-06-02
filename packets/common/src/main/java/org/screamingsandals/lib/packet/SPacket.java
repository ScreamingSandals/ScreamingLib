package org.screamingsandals.lib.packet;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;

public interface SPacket {
    void sendPacket(PlayerWrapper player);

    void sendPacket(List<PlayerWrapper> players);

    Object getRawPacket();
}
