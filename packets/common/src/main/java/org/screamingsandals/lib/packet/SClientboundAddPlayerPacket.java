package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundAddPlayerPacket extends AbstractPacket {
    private int entityId;
    private UUID uuid;
    private LocationHolder location;
    private List<MetadataItem> metadata;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(entityId);
        writer.writeUuid(uuid);
        if (writer.protocol() >= 100) {
            writer.writeVector(location);
        } else {
            writer.writeFixedPointVector(location);
        }
        writer.writeByteRotation(location);
        if (writer.protocol() < 49) {
            writer.writeShort((short) 0);
        }

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
