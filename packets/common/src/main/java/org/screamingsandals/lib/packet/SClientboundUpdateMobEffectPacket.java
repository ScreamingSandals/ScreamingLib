package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundUpdateMobEffectPacket extends AbstractPacket {
    private int entityId;
    private byte effect;
    private byte amplifier;
    private int duration;
    private boolean ambient;
    private boolean showParticles;
    private boolean showIcons;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(entityId);
        writer.writeByte(effect);
        writer.writeByte(amplifier);
        writer.writeVarInt(duration);

        if (writer.protocol() >= 210) {
            byte flags = 0;
            if (ambient) {
                flags |= 0x01;
            }
            if (showParticles) {
                flags |= 0x02;
            }
            if (showIcons) { // I think client shouldn't crash if it doesn't know this flag exists
                flags |= 0x04;
            }
            writer.writeByte(flags);
        } else {
            writer.writeBoolean(!showParticles);
        }
    }
}
