package org.screamingsandals.lib.bukkit.packet;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundSetDisplayObjectivePacketAccessor;
import org.screamingsandals.lib.packet.SClientboundSetDisplayObjectivePacket;
import org.screamingsandals.lib.utils.AdventureHelper;

public class BukkitSClientboundSetDisplayObjectivePacket extends BukkitSPacket implements SClientboundSetDisplayObjectivePacket {

    public BukkitSClientboundSetDisplayObjectivePacket() {
        super(ClientboundSetDisplayObjectivePacketAccessor.getType());
    }

    @Override
    public SClientboundSetDisplayObjectivePacket setObjectiveKey(Component objectiveKey) {
        if (objectiveKey == null) {
            throw new UnsupportedOperationException("Objective key cannot be null!");
        }

        packet.setField(ClientboundSetDisplayObjectivePacketAccessor.getFieldObjectiveName(), AdventureHelper.toLegacy(objectiveKey));
        return this;
    }

    @Override
    public SClientboundSetDisplayObjectivePacket setDisplaySlot(DisplaySlot slot) {
        packet.setField(ClientboundSetDisplayObjectivePacketAccessor.getFieldSlot(), slot.ordinal());
        return this;
    }
}
