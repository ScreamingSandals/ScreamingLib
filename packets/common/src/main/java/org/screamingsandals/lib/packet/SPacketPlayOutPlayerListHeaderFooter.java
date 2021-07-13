package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SPacketPlayOutPlayerListHeaderFooter extends SPacket {

    SPacketPlayOutPlayerListHeaderFooter setHeader(Component header);

    SPacketPlayOutPlayerListHeaderFooter setFooter(Component footer);
}
