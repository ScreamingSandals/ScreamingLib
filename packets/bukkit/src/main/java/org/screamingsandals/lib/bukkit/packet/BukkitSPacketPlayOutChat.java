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
    public SPacketPlayOutChat setChatComponent(Component text) {
        if (text == null) {
            throw new UnsupportedOperationException("Chat text cannot be null!");
        }
        if (packet.setField("a,field_148919_a,f_131821_", ClassStorage.asMinecraftComponent(text)) == null) {
            packet.setField("a,field_148919_a,f_131821_", AdventureHelper.toLegacy(text));
        }
        return this;
    }

    @Deprecated
    @Override
    public SPacketPlayOutChat setBytes(byte bytes) {
        packet.setField("b,field_179842_b", bytes);
        return this;
    }

    @Override
    public SPacketPlayOutChat setUUID(UUID uuid) {
        if (uuid == null) {
            throw new UnsupportedOperationException("UUID cannot be null!");
        }
        packet.setField("c,field_240809_c_,f_131823_", uuid);
        return this;
    }

    @Override
    public SPacketPlayOutChat setChatType(ChatType type) {
        if (type == null) {
            throw new UnsupportedOperationException("ChatType cannot be null!");
        }
        packet.setField("b,field_179842_b,f_131822_", Reflect.findEnumConstant(ClassStorage.NMS.ChatMessageType, type.name().toUpperCase()));
        return this;
    }
}
