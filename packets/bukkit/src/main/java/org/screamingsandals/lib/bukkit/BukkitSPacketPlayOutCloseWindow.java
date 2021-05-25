package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutCloseWindow;

public class BukkitSPacketPlayOutCloseWindow extends BukkitSPacket implements SPacketPlayOutCloseWindow {
    public BukkitSPacketPlayOutCloseWindow() {
        super(ClassStorage.NMS.PacketPlayOutCloseWindow);
    }

    @Override
    public void setWindowId(int windowId) {
        packet.setField("a", windowId);
    }
}
