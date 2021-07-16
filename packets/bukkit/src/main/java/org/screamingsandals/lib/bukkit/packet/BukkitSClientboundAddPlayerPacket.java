package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.bukkit.entity.BukkitDataWatcher;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundAddPlayerPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundAddPlayerPacket;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.UUID;

public class BukkitSClientboundAddPlayerPacket extends BukkitSPacket implements SClientboundAddPlayerPacket {

    public BukkitSClientboundAddPlayerPacket() {
        super(ClientboundAddPlayerPacketAccessor.getType());
    }

    @Override
    public SClientboundAddPlayerPacket setEntityId(int entityId) {
        packet.setField(ClientboundAddPlayerPacketAccessor.getFieldEntityId(), entityId);
        return this;
    }

    @Override
    public SClientboundAddPlayerPacket setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }

        packet.setField(ClientboundAddPlayerPacketAccessor.getFieldPlayerId(), uuid);
        return this;
    }

    @Override
    public SClientboundAddPlayerPacket setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }

        packet.setField(ClientboundAddPlayerPacketAccessor.getFieldX(), location.getX());
        packet.setField(ClientboundAddPlayerPacketAccessor.getFieldY(), location.getY());
        packet.setField(ClientboundAddPlayerPacketAccessor.getFieldZ(), location.getZ());
        return this;
    }

    @Override
    public SClientboundAddPlayerPacket setYaw(float yaw) {
        packet.setField(ClientboundAddPlayerPacketAccessor.getFieldYRot(), (byte) (yaw * 256.0F / 360.0F));
        return this;
    }

    @Override
    public SClientboundAddPlayerPacket setPitch(float pitch) {
        packet.setField(ClientboundAddPlayerPacketAccessor.getFieldXRot(), (byte) (pitch * 256.0F / 360.0F));
        return this;
    }

    @Deprecated
    @Override
    public SClientboundAddPlayerPacket setDataWatcher(DataWatcher dataWatcher) {
        if (dataWatcher == null) {
            throw new UnsupportedOperationException("DataWatcher cannot be null!");
        }

        if (!(dataWatcher instanceof BukkitDataWatcher)) {
            throw new UnsupportedOperationException("DataWatcher is not an instance of BukkitDataWatcher!");
        }

        final var bukkitDataWatcher = (BukkitDataWatcher) dataWatcher;
        final var nmsDataWatcher = bukkitDataWatcher.toNMS();
        packet.setField(ClientboundAddPlayerPacketAccessor.getFieldField_148960_i(), nmsDataWatcher);
        return this;
    }

    @Override
    public SClientboundAddPlayerPacket setItems(List<DataWatcher.Item<?>> items) {
        //TODO:
        packet.setField(ClientboundAddPlayerPacketAccessor.getFieldField_148958_j(), null);
        return this;
    }
}
