package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundTabListPacket extends AbstractPacket {
    private Component header;
    private Component footer;

    @Override
    public void write(PacketWriter writer) {
        writer.writeComponent(header);
        writer.writeComponent(footer);
    }
}
