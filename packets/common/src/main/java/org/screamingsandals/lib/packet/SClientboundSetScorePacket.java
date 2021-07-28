package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundSetScorePacket extends AbstractPacket {
    private String entityName;
    private ScoreboardAction action;
    private String objectiveKey;
    private int score;

    @Override
    public void write(PacketWriter writer) {
        writer.writeSizedString(entityName);
        writer.writeByte((byte) action.ordinal());
        writer.writeSizedString(objectiveKey);
        if (action == ScoreboardAction.CHANGE) {
            writer.writeVarInt(score);
        }
    }

    public enum ScoreboardAction {
        CHANGE,
        REMOVE
    }
}
