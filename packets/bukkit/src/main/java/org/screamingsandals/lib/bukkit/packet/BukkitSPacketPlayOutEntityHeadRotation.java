package org.screamingsandals.lib.bukkit.packet;

import org.bukkit.Bukkit;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
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
    public SPacketPlayOutEntityHeadRotation setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    @Override
    public SPacketPlayOutEntityHeadRotation setRotation(byte rotation) {
        this.rotation = rotation;
        return this;
    }

    private void generatePacket() {
        // create fake entity
        //TODO: find alternative
        final var fakeEntity = Reflect.constructor(ClassStorage.NMS.EntityArmorStand, ClassStorage.NMS.World, double.class, double.class, double.class)
                .construct(ClassStorage.getHandle(Bukkit.getServer().getWorlds().get(0)), 0.0D, 0.0D, 0.0D);
        Reflect.setField(fakeEntity, "as,f_19848_,id", entityId);

        packet = Reflect.constructor(ClassStorage.NMS.PacketPlayOutEntityHeadRotation, ClassStorage.NMS.Entity, byte.class)
                .constructResulted(fakeEntity, rotation);
    }
}
