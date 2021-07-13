package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityLook;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSPacketPlayOutEntityLook extends BukkitSPacket implements SPacketPlayOutEntityLook {
    private int entityId;
    private byte yaw;
    private byte pitch;
    private boolean isOnGround;

    @Override
    public void sendPacket(PlayerWrapper player) {
        generatePacket();
        super.sendPacket(player);
    }

    @Override
    public Object getRawPacket() {
        generatePacket();
        return packet.raw();
    }

    @Override
    public SPacketPlayOutEntityLook setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    @Override
    public SPacketPlayOutEntityLook setYaw(byte yaw) {
        this.yaw = yaw;
        return this;
    }

    @Override
    public SPacketPlayOutEntityLook setPitch(byte pitch) {
        this.pitch = pitch;
        return this;
    }

    @Override
    public SPacketPlayOutEntityLook setOnGround(boolean isOnGround) {
        this.isOnGround = isOnGround;
        return this;
    }

    protected void generatePacket() {
        packet = Reflect.constructor(ClassStorage.NMS.PacketPlayOutEntityLook, int.class, byte.class, byte.class, boolean.class)
                        .constructResulted(entityId, yaw, pitch, isOnGround);
    }
}
