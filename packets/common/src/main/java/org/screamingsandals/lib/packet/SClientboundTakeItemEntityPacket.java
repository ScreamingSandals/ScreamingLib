package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundTakeItemEntityPacket extends AbstractPacket {
    private int entityId;
    private int collectedEntityId;
    private int amount;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(collectedEntityId);
        writer.writeVarInt(entityId);
        if (writer.protocol() >= 301) {
            writer.writeVarInt(amount);
        }
    }
}
