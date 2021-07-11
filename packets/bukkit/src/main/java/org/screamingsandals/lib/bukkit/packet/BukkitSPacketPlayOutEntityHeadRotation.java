package org.screamingsandals.lib.bukkit.packet;

import org.bukkit.Location;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.entity.ArmorStandNMS;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityHeadRotation;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSPacketPlayOutEntityHeadRotation extends BukkitSPacket implements SPacketPlayOutEntityHeadRotation {
    private int entityId;
    private byte rotation;

    @Override
    public Object getRawPacket() {
        generatePacket();
        return super.getRawPacket();
    }

    @Override
    public void sendPacket(PlayerWrapper player) {
        generatePacket();
        super.sendPacket(player);
    }

    @Override
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void setRotation(byte rotation) {
        this.rotation = rotation;
    }

    private void generatePacket() {
        // create fake entity
        final var fakeEntity = new ArmorStandNMS(new Location(null, 0, 0, 0, 0, 0));
        fakeEntity.setId(entityId);

        packet = Reflect.constructor(ClassStorage.NMS.PacketPlayOutEntityHeadRotation, ClassStorage.NMS.Entity, byte.class)
                .constructResulted(fakeEntity.getHandler(), rotation);
    }
}
