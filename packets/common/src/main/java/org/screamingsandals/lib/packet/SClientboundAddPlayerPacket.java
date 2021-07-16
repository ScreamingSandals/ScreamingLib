package org.screamingsandals.lib.packet;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.UUID;

public interface SClientboundAddPlayerPacket extends SPacket {

    SClientboundAddPlayerPacket setEntityId(int entityId);

    SClientboundAddPlayerPacket setUUID(UUID uuid);

    SClientboundAddPlayerPacket setLocation(LocationHolder location);

    SClientboundAddPlayerPacket setYaw(float yaw);

    SClientboundAddPlayerPacket setPitch(float pitch);

    SClientboundAddPlayerPacket setDataWatcher(DataWatcher dataWatcher);

    SClientboundAddPlayerPacket setItems(List<DataWatcher.Item<?>> items);
}
