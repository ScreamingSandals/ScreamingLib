package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundTabListPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundTabListPacket;
import org.screamingsandals.lib.utils.AdventureHelper;

public class BukkitSClientboundTabListPacket extends BukkitSPacket implements SClientboundTabListPacket {

    public BukkitSClientboundTabListPacket() {
        super(ClientboundTabListPacketAccessor.getType());
    }

    @Override
    public SClientboundTabListPacket setHeader(Component header) {
        Component text = header;
        if (header == null) {
            text = Component.text("");
        }
        if (packet.setField(ClientboundTabListPacketAccessor.getFieldHeader(), ClassStorage.asMinecraftComponent(text)) == null) {
            packet.setField(ClientboundTabListPacketAccessor.getFieldHeader(), AdventureHelper.toLegacy(text));
        }
        return this;
    }

    @Override
    public SClientboundTabListPacket setFooter(Component footer) {
        Component text = footer;
        if (footer == null) {
            text = Component.text("");
        }
        if (packet.setField(ClientboundTabListPacketAccessor.getFieldFooter(), ClassStorage.asMinecraftComponent(text)) == null) {
            packet.setField(ClientboundTabListPacketAccessor.getFieldFooter(), AdventureHelper.toLegacy(text));
        }
        return this;
    }
}
