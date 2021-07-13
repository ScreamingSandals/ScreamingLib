package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityDestroy;

import java.util.Arrays;

public class BukkitSPacketPlayOutEntityDestroy extends BukkitSPacket implements SPacketPlayOutEntityDestroy {

    public BukkitSPacketPlayOutEntityDestroy() {
        super(ClassStorage.NMS.PacketPlayOutEntityDestroy);
    }

    @Override
    public SPacketPlayOutEntityDestroy setEntitiesToDestroy(int[] entityIdArray) {
        if (entityIdArray == null || entityIdArray.length == 0) {
            throw new UnsupportedOperationException("Invalid array provided!");
        }

        if (packet.setField("a,field_149100_a,f_182717_", entityIdArray) == null) {
            packet.setField("a", entityIdArray[0]);
            Arrays.stream(entityIdArray)
                    .skip(1)
                    .forEach(id -> {
                        BukkitSPacketPlayOutEntityDestroy packet = BukkitPacketMapper.createPacket(SPacketPlayOutEntityDestroy.class);
                        packet.setEntityToDestroy(id);
                        addAdditionalPacket(packet.getRawPacket());
                    });
        }
        return this;
    }

    @Override
    public SPacketPlayOutEntityDestroy setEntityToDestroy(int entityId) {
        if (packet.setField("a", entityId) == null) {
            int[] arr = {entityId};
            packet.setField("a,field_149100_a,f_182717_", arr);
        }
        return this;
    }


}
