package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundAddEntityPacket extends AbstractPacket {
    private int entityId;
    private UUID uuid;
    private LocationHolder location;
    private Vector3D velocity;
    private int typeId;
    private int data;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(entityId);
        if (writer.protocol() >= 49) {
            writer.writeUuid(uuid);
        }
        if (writer.protocol() >= 458) {
            writer.writeVarInt(typeId);
        } else {
            writer.writeByte((byte) typeId); // TODO: Object ID was used, not EntityType id
        }
        if (writer.protocol() >= 100) {
            writer.writeVector(location);
        } else {
            writer.writeFixedPointVector(location);
        }
        writer.writeByteRotation(location);
        writer.writeInt(data);
        if (data != 0 || writer.protocol() >= 49) {
            writer.writeMotion(velocity);
        }
    }
}
