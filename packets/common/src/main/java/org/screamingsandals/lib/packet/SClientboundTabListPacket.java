package org.screamingsandals.lib.packet;

import net.kyori.adventure.text.Component;

public interface SClientboundTabListPacket extends SPacket {

    SClientboundTabListPacket setHeader(Component header);

    SClientboundTabListPacket setFooter(Component footer);
}
