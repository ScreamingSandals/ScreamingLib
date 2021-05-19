package org.screamingsandals.lib.common;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutPlayerListHeaderFooter {
    void setHeader(Component header);

    void setFooter(Component footer);
}
