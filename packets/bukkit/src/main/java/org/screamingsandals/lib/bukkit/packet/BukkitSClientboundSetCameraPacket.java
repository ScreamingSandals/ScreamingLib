package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundSetCameraPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundSetCameraPacket;

public class BukkitSClientboundSetCameraPacket extends BukkitSPacket implements SClientboundSetCameraPacket {

    public BukkitSClientboundSetCameraPacket() {
        super(ClientboundSetCameraPacketAccessor.getType());
    }

    @Override
    public SClientboundSetCameraPacket setCameraId(int cameraId) {
        packet.setField(ClientboundSetCameraPacketAccessor.getFieldCameraId(), cameraId);
        return this;
    }
}
