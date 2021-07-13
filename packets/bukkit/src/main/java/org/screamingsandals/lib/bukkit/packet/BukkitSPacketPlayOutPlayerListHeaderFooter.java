package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutPlayerListHeaderFooter;
import org.screamingsandals.lib.utils.AdventureHelper;

public class BukkitSPacketPlayOutPlayerListHeaderFooter extends BukkitSPacket implements SPacketPlayOutPlayerListHeaderFooter {

    public BukkitSPacketPlayOutPlayerListHeaderFooter() {
        super(ClassStorage.NMS.PacketPlayOutPlayerListHeaderFooter);
    }

    @Override
    public SPacketPlayOutPlayerListHeaderFooter setHeader(Component header) {
        Component text = header;
        if (header == null) {
            text = Component.text("");
        }
        if (packet.setField("a,field_179703_a,f_133480_", ClassStorage.asMinecraftComponent(text)) == null) {
            packet.setField("a,field_179703_a,f_133480_", AdventureHelper.toLegacy(text));
        }
        return this;
    }

    @Override
    public SPacketPlayOutPlayerListHeaderFooter setFooter(Component footer) {
        Component text = footer;
        if (footer == null) {
            text = Component.text("");
        }
        if (packet.setField("b,field_179702_b,f_133481_", ClassStorage.asMinecraftComponent(text)) == null) {
            packet.setField("b,field_179702_b,f_133481_", AdventureHelper.toLegacy(text));
        }
        return this;
    }
}
