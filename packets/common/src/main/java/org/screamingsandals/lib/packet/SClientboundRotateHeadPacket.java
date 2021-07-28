package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundRotateHeadPacket extends AbstractPacket {
    private int entityId;
    private byte rotation;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(this.entityId);
        writer.writeByte(this.rotation);
    }

    public SClientboundRotateHeadPacket headYaw(float headYaw) {
        this.rotation = (byte) (headYaw * 256 / 360);
        return this;
    }
}
