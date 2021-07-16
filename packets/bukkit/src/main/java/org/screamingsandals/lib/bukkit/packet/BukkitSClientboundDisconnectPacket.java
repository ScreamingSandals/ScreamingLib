package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundDisconnectPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundDisconnectPacket;
import org.screamingsandals.lib.utils.AdventureHelper;

public class BukkitSClientboundDisconnectPacket extends BukkitSPacket implements SClientboundDisconnectPacket {

    public BukkitSClientboundDisconnectPacket() {
        super(ClientboundDisconnectPacketAccessor.getType());
    }

    @Override
    public SClientboundDisconnectPacket setDisconnectReason(Component reason) {
        if (reason == null) {
            throw new UnsupportedOperationException("Invalid kick reason provided!");
        }
        if (packet.setField(ClientboundDisconnectPacketAccessor.getFieldReason(), ClassStorage.asMinecraftComponent(reason)) == null) {
            packet.setField(ClientboundDisconnectPacketAccessor.getFieldReason(), AdventureHelper.toLegacy(reason));
        }
        return this;
    }
}
