package org.screamingsandals.lib.common;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutChat {
    void setChatComponent(Component text);

    void setBytes(byte bytes);
}
