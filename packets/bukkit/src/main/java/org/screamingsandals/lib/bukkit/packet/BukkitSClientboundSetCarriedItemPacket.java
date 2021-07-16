package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundSetCarriedItemPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundSetCarriedItemPacket;

public class BukkitSClientboundSetCarriedItemPacket extends BukkitSPacket implements SClientboundSetCarriedItemPacket {

    public BukkitSClientboundSetCarriedItemPacket() {
        super(ClientboundSetCarriedItemPacketAccessor.getType());
    }

    @Override
    public SClientboundSetCarriedItemPacket setSlot(int slot) {
        packet.setField(ClientboundSetCarriedItemPacketAccessor.getFieldSlot(), slot);
        return this;
    }
}
