package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundSetEntityLinkPacket extends AbstractPacket {
    private int attachedEntityId;
    private int holdingEntityId;

    @Override
    public void write(PacketWriter writer) {
        writer.writeInt(attachedEntityId);
        writer.writeInt(holdingEntityId);
        if (writer.protocol() < 77) {
            writer.writeBoolean(true);
        }
    }
}
