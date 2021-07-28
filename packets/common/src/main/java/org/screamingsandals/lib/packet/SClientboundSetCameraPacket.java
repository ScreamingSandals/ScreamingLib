package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundSetCameraPacket extends AbstractPacket {
    private int entityId;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(entityId);
    }
}
