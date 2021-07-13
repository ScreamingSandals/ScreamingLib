package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutCamera;

public class BukkitSPacketPlayOutCamera extends BukkitSPacket implements SPacketPlayOutCamera {

    public BukkitSPacketPlayOutCamera() {
        super(ClassStorage.NMS.PacketPlayOutCamera);
    }

    @Override
    public SPacketPlayOutCamera setCameraId(int cameraId) {
        packet.setField("a,field_179781_a,f_133055_", cameraId);
        return this;
    }
}
