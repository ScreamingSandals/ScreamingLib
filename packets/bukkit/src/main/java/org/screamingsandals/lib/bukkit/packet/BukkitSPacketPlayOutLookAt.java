package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutLookAt;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;

public class BukkitSPacketPlayOutLookAt extends BukkitSPacket implements SPacketPlayOutLookAt {
    private int entityId;
    private int yaw;
    private int pitch;
    private boolean isOnGround;

    public BukkitSPacketPlayOutLookAt() {
       super(ClassStorage.NMS.PacketPlayOutEntityLook);
    }

    @Override
    public void sendPacket(List<PlayerWrapper> players) {
        generatePacket();
        super.sendPacket(players);
    }

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
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    @Override
    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setOnGround(boolean isOnGround) {
        this.isOnGround = isOnGround;
    }

    protected void generatePacket() {
        packet = Reflect.constructor(ClassStorage.NMS.PacketPlayOutEntityLook, int.class, byte.class, byte.class, boolean.class)
                        .constructResulted(entityId, yaw, pitch, isOnGround);
    }
}
