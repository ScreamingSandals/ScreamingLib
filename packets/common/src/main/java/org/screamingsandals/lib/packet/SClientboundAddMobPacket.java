package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundAddMobPacket extends AbstractPacket {
    private int entityId;
    private UUID uuid;
    private LocationHolder location;
    private Vector3D velocity;
    private int typeId;
    private final List<MetadataItem> metadata = new ArrayList<>();
    private byte headYaw;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(entityId);
        if (writer.protocol() >= 49) {
            writer.writeUuid(uuid);
        }
        if (writer.protocol() >= 301) {
            writer.writeVarInt(typeId);
        } else {
            writer.writeByte((byte) typeId);
        }
        if (writer.protocol() >= 100) {
            writer.writeVector(location);
        } else {
            writer.writeFixedPointVector(location);
        }
        writer.writeByteRotation(location);
        writer.writeByte(headYaw);
        writer.writeMotion(velocity);

        if (writer.protocol() >= 550) {
            if (!metadata.isEmpty()) {
                var packet = new SClientboundSetEntityDataPacket();
                packet.entityId(entityId);
                packet.metadata().addAll(metadata);
                writer.append(packet);
            }
        } else {
            writer.writeDataWatcherCollection(metadata);
        }
    }
}
