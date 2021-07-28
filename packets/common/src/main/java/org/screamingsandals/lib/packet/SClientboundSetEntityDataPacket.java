package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundSetEntityDataPacket extends AbstractPacket {
    private int entityId;
    private final List<MetadataItem> metadata = new ArrayList<>();

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(entityId);

        writer.writeDataWatcherCollection(metadata);
    }
}
