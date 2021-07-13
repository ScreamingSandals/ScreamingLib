package org.screamingsandals.lib.bukkit.packet;

import org.bukkit.block.Block;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutBlockAction;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;

public class BukkitSPacketPlayOutBlockAction extends BukkitSPacket implements SPacketPlayOutBlockAction {

    public BukkitSPacketPlayOutBlockAction() {
        super(ClassStorage.NMS.PacketPlayOutBlockAction);
    }

    @Override
    public SPacketPlayOutBlockAction setBlockLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        var constructed = Reflect.constructor(ClassStorage.NMS.BlockPosition, int.class, int.class, int.class)
                .construct(location.getX(), location.getY(), location.getZ());
        packet.setField("a,field_179826_a,f_131709_", constructed);
        return this;
    }

    @Override
    public SPacketPlayOutBlockAction setActionId(int actionId) {
        packet.setField("b,field_148872_d,f_131710_", actionId);
        return this;
    }

    @Override
    public SPacketPlayOutBlockAction setActionParameter(int actionParameter) {
        packet.setField("c,field_148873_e,f_131711_", actionParameter);
        return this;
    }

    @Override
    public SPacketPlayOutBlockAction setBlockType(BlockHolder block) {
        if (block == null) {
            throw new UnsupportedOperationException("Block cannot be null!");
        }
        final var bukkitBlock = block.as(Block.class);
        var nmsBlock = Reflect.getMethod(bukkitBlock, "getNMSBlock").invoke();
        if (nmsBlock == null) {
            throw new UnsupportedOperationException("NMSBlock is null!");
        }
        packet.setField("d,field_148871_f,f_131712_", nmsBlock);
        return this;
    }
}
