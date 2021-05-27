package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.packet.SPacketPlayOutBlockBreakAnimation;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;

public class BukkitSPacketPlayOutBlockBreakAnimation extends BukkitSPacket implements SPacketPlayOutBlockBreakAnimation {
    public BukkitSPacketPlayOutBlockBreakAnimation() {
        super(ClassStorage.NMS.PacketPlayOutBlockBreakAnimation);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }

    @Override
    public void setBlockLocation(LocationHolder blockLocation) {
        if (blockLocation == null) {
            throw new UnsupportedOperationException("Cannot play block break animation on null location!");
        }
        var bukkitBlockPos = Reflect.constructor(ClassStorage.NMS.BlockPosition, int.class, int.class, int.class)
                .construct(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
        packet.setField("b", bukkitBlockPos);
    }

    @Override
    public void setDestroyStage(int destroyStage) {
        if (destroyStage < 0 || destroyStage > 9) {
            throw new UnsupportedOperationException("Invalid destroy stage: " + destroyStage);
        }
        packet.setField("c", destroyStage);
    }
}
