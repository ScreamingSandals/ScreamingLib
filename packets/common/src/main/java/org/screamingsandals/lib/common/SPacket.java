package org.screamingsandals.lib.common;
import org.screamingsandals.lib.player.PlayerWrapper;

public abstract class SPacket {
    abstract public void sendPacket(PlayerWrapper player);
}
