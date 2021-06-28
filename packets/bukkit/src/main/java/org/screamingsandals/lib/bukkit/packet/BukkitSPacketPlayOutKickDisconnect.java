package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutKickDisconnect;
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
        if (packet.setField("a,field_149167_a", ClassStorage.asMinecraftComponent(reason)) == null) {
            packet.setField("a,field_149167_a", AdventureHelper.toLegacy(reason));
        }
    }
}
