package org.screamingsandals.lib.bukkit;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutChat;
import org.screamingsandals.lib.utils.AdventureHelper;

public class BukkitSPacketPlayOutChat extends BukkitSPacket implements SPacketPlayOutChat {
    public BukkitSPacketPlayOutChat() {
        super(ClassStorage.NMS.PacketPlayOutChat);
    }

    @Override
    public void setChatComponent(Component text) {
        if (text == null) {
            throw new UnsupportedOperationException("Chat text cannot be null!");
        }
        if (packet.setField("a", ClassStorage.asMinecraftComponent(text)) == null) {
            packet.setField("a", AdventureHelper.toLegacy(text));
        }
    }

    @Override
    public void setBytes(byte bytes) {
        packet.setField("b", bytes);
    }
}
