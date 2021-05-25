package org.screamingsandals.lib.common;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutKickDisconnect {
    void setDisconnectReason(Component reason);
}
