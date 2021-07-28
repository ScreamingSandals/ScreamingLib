package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundRemoveEntitiesPacket extends AbstractPacket {
    private int[] entityIds;

    @Override
    public void write(PacketWriter writer) {
        if (writer.protocol() == 755) {
            // weird 1.17 version
            for (int i = 0; i < entityIds.length; i++) {
                if (i > 0) {
                    var append = new SClientboundRemoveEntitiesPacket();
                    append.entityIds(new int[] {entityIds[i]});
                    writer.append(append);
                } else {
                    writer.writeVarInt(entityIds[i]);
                }
            }
        } else {
            writer.writeVarInt(entityIds.length);
            for (var entityId : entityIds) {
                writer.writeVarInt(entityId);
            }
        }
    }
}
