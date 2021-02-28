package org.screamingsandals.lib.bukkit.world.state;

import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.BlockDataMapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.state.BlockStateHolder;

public class GenericBlockStateHolder extends BasicWrapper<BlockState> implements BlockStateHolder {
    protected GenericBlockStateHolder(BlockState wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public MaterialHolder getType() {
        return MaterialMapping.resolve(wrappedObject.getType()).orElseThrow();
    }

    @Override
    public BlockDataHolder getBlockData() {
        return BlockDataMapper.resolve(wrappedObject.getBlockData()).orElseThrow();
    }

    @Override
    public void setBlockData(BlockDataHolder blockData) {
        wrappedObject.setBlockData(blockData.as(BlockData.class));
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
