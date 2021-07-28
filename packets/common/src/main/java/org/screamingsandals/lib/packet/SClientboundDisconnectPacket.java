package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundDisconnectPacket extends AbstractPacket {
    private Component reason;

    @Override
    public void write(PacketWriter writer) {
        writer.writeComponent(reason);
    }
}
