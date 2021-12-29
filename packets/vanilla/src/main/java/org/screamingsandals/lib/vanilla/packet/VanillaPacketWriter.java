package org.screamingsandals.lib.vanilla.packet;

import io.netty.buffer.ByteBuf;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.packet.PacketWriter;
import org.screamingsandals.lib.utils.reflect.Reflect;

public abstract class VanillaPacketWriter extends PacketWriter {
    public VanillaPacketWriter(ByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected int getItemId(ItemTypeHolder material) {
        return Reflect.fastInvokeResulted(ItemAccessor.getMethodGetId1(), materialHolderToItem(material)).as(Integer.class);
    }

    @Override
    protected int getBlockStateId(BlockTypeHolder blockDataHolder) {
        return Reflect.fastInvokeResulted(BlockAccessor.getMethodGetId1(), blockDataToBlockState(blockDataHolder)).as(Integer.class);
        // TODO: check if this works for legacy too
    }

    protected abstract Object materialHolderToItem(ItemTypeHolder material);

    protected abstract Object blockDataToBlockState(BlockTypeHolder blockData);
}
