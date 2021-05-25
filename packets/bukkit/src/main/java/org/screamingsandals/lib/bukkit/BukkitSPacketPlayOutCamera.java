package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutCamera;

public class BukkitSPacketPlayOutCamera extends BukkitSPacket implements SPacketPlayOutCamera {
    public BukkitSPacketPlayOutCamera() {
        super(ClassStorage.NMS.PacketPlayOutCamera);
    }

    @Override
    public void setCameraId(int cameraId) {
        packet.setField("a", cameraId);
    }
}
