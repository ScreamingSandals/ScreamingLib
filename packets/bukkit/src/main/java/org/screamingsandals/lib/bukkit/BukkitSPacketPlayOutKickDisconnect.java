package org.screamingsandals.lib.bukkit;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutKickDisconnect;
import org.screamingsandals.lib.utils.AdventureHelper;

public class BukkitSPacketPlayOutKickDisconnect extends BukkitSPacket implements SPacketPlayOutKickDisconnect {
    public BukkitSPacketPlayOutKickDisconnect() {
        super(ClassStorage.NMS.PacketPlayOutKickDisconnect);
    }

    @Override
    public void setDisconnectReason(Component reason) {
        if (reason == null) {
            throw new UnsupportedOperationException("Invalid kick reason provided!");
        }
        if (packet.setField("a", ClassStorage.asMinecraftComponent(reason)) == null) {
            packet.setField("a", AdventureHelper.toLegacy(reason));
        }
    }
}
