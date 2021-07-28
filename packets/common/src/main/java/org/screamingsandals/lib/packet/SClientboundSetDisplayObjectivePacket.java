package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundSetDisplayObjectivePacket extends AbstractPacket {
    private DisplaySlot slot;
    private String objectiveKey;

    @Override
    public void write(PacketWriter writer) {
        writer.writeByte((byte) slot.ordinal());
        writer.writeSizedString(objectiveKey);
    }

    public enum DisplaySlot {
        PLAYER_LIST,
        SIDEBAR,
        BELOW_NAME
    }
}
