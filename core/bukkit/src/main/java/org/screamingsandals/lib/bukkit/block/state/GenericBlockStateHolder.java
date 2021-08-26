package org.screamingsandals.lib.bukkit.block.state;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.*;
import org.screamingsandals.lib.block.state.BlockStateHolder;

public class GenericBlockStateHolder extends BasicWrapper<BlockState> implements BlockStateHolder {
    protected GenericBlockStateHolder(BlockState wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public BlockTypeHolder getType() {
        if (!Version.isVersion(1,13)) {
            return BlockTypeHolder.of(wrappedObject.getData());
        } else {
            return BlockTypeHolder.of(wrappedObject.getBlockData());
        }
    }

    @Override
    public void setType(BlockTypeHolder type) {
        if (!Version.isVersion(1,13)) {
            wrappedObject.setType(type.as(Material.class));
            wrappedObject.setRawData(type.legacyData());
        } else {
            wrappedObject.setBlockData(type.as(BlockData.class));
        }
    }

    @Override
    public LocationHolder getLocation() {
        return LocationMapper.wrapLocation(wrappedObject.getLocation());
    }

    @Override
    public boolean updateBlock(boolean force, boolean applyPhysics) {
        return wrappedObject.update(force, applyPhysics);
    }
}
