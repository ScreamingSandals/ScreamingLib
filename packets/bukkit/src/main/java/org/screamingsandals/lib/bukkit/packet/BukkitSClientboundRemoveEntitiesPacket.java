package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ClientboundRemoveEntitiesPacketAccessor;
import org.screamingsandals.lib.packet.SClientboundRemoveEntitiesPacket;

import java.util.Arrays;

public class BukkitSClientboundRemoveEntitiesPacket extends BukkitSPacket implements SClientboundRemoveEntitiesPacket {

    public BukkitSClientboundRemoveEntitiesPacket() {
        super(ClientboundRemoveEntitiesPacketAccessor.getType());
    }

    @Override
    public SClientboundRemoveEntitiesPacket setEntitiesToDestroy(int[] entityIdArray) {
        if (entityIdArray == null || entityIdArray.length == 0) {
            throw new UnsupportedOperationException("Invalid array provided!");
        }

        if (packet.setField(ClientboundRemoveEntitiesPacketAccessor.getFieldEntityIds(), entityIdArray) == null) {
            packet.setField(ClientboundRemoveEntitiesPacketAccessor.getFieldEntityIds(), entityIdArray[0]);
            Arrays.stream(entityIdArray)
                    .skip(1)
                    .forEach(id -> {
                        BukkitSClientboundRemoveEntitiesPacket packet = BukkitPacketMapper.createPacket(SClientboundRemoveEntitiesPacket.class);
                        packet.setEntityToDestroy(id);
                        addAdditionalPacket(packet.getRawPacket());
                    });
        }
        return this;
    }

    @Override
    public SClientboundRemoveEntitiesPacket setEntityToDestroy(int entityId) {
        if (packet.setField(ClientboundRemoveEntitiesPacketAccessor.getFieldEntityIds(), entityId) == null) {
            int[] arr = {entityId};
            packet.setField(ClientboundRemoveEntitiesPacketAccessor.getFieldEntityIds(), arr);
        }
        return this;
    }
}
