package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutChat;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.UUID;

public class BukkitSPacketPlayOutChat extends BukkitSPacket implements SPacketPlayOutChat {
    public BukkitSPacketPlayOutChat() {
        super(ClassStorage.NMS.PacketPlayOutChat);
    }

    @Override
    public void setChatComponent(Component text) {
        if (text == null) {
            throw new UnsupportedOperationException("Chat text cannot be null!");
        }
        if (packet.setField("a,field_148919_a", ClassStorage.asMinecraftComponent(text)) == null) {
            packet.setField("a,field_148919_a", AdventureHelper.toLegacy(text));
        }
    }

    @Deprecated
    @Override
    public void setBytes(byte bytes) {
        packet.setField("b,field_179842_b", bytes);
    }

    @Override
    public void setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }
        packet.setField("c,field_240809_c_", uuid);
    }

    @Override
    public void setChatType(ChatType type) {
        if (type == null) {
            throw new UnsupportedOperationException("ChatType cannot be null!");
        }
        packet.setField("b,field_179842_b", Reflect.findEnumConstant(ClassStorage.NMS.ChatMessageType, type.name().toUpperCase()));
    }
}
