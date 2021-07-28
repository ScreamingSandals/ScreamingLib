package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundPlayerAbilitiesPacket extends AbstractPacket {
    private boolean invulnerable;
    private boolean flying;
    private boolean canFly;
    private boolean canInstantlyBreak;
    private float flyingSpeed;
    private float walkingSpeed;

    @Override
    public void write(PacketWriter writer) {
        byte flags = 0;
        if (invulnerable) {
            flags |= 0x01;
        }
        if (flying) {
            flags |= 0x02;
        }
        if (canFly) {
            flags |= 0x04;
        }
        if (canInstantlyBreak) {
            flags |= 0x08;
        }

        writer.writeByte(flags);
        writer.writeFloat(flyingSpeed);
        writer.writeFloat(walkingSpeed);
    }
}
