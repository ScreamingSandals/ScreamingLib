package org.screamingsandals.lib.bukkit;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutPlayerListHeaderFooter;
import org.screamingsandals.lib.utils.AdventureHelper;

public class BukkitSPacketPlayOutPlayerListHeaderFooter extends BukkitSPacket implements SPacketPlayOutPlayerListHeaderFooter {
    public BukkitSPacketPlayOutPlayerListHeaderFooter() {
        super(ClassStorage.NMS.PacketPlayOutPlayerListHeaderFooter);
    }

    @Override
    public void setHeader(Component header) {
        Component text = header;
        if (header == null) {
            text = Component.text("");
        }
        if (packet.setField("a", ClassStorage.asMinecraftComponent(text)) == null) {
            packet.setField("a", AdventureHelper.toLegacy(text));
        }
    }

    @Override
    public void setFooter(Component footer) {
        Component text = footer;
        if (footer == null) {
            text = Component.text("");
        }
        if (packet.setField("b", ClassStorage.asMinecraftComponent(text)) == null) {
            packet.setField("b", AdventureHelper.toLegacy(text));
        }
    }
}
