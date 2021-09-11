package org.screamingsandals.lib.packet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundSetEquipmentPacket extends AbstractPacket {
    private int entityId;
    private final Map<EquipmentSlotHolder, Item> slots = new HashMap<>();

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(entityId);
        var iterator = slots.entrySet().iterator();
        boolean hasNext = iterator.hasNext();
        boolean first = false;
        while (hasNext) {
            var next = iterator.next();
            hasNext = iterator.hasNext();
            if (writer.protocol() >= 732) {
                if (hasNext) {
                    writer.writeByte((byte) (writer.getEquipmentSlotId(next.getKey()) | 0x80));
                } else {
                    writer.writeByte((byte) writer.getEquipmentSlotId(next.getKey()));
                }
                writer.writeItem(next.getValue());
            } else {
                if (!first) {
                    if (writer.protocol() >= 49) {
                        writer.writeVarInt(writer.getEquipmentSlotId(next.getKey()));
                    } else {
                        writer.writeShort(writer.getEquipmentSlotId(next.getKey()));
                    }
                    writer.writeItem(next.getValue());
                    first = true;
                } else {
                    var append = new SClientboundSetEquipmentPacket();
                    append.entityId(entityId);
                    append.slots().put(next.getKey(), next.getValue());
                    writer.append(append);
                }
            }
        }
    }
}
