package org.screamingsandals.lib.bukkit.world.tile;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.state.BlockStateHolder;
import org.screamingsandals.lib.world.state.BlockStateMapper;

import java.util.Optional;

@Service
public class BukkitBlockStateMapper extends BlockStateMapper {
    public static void init() {
        BlockStateMapper.init(BukkitBlockStateMapper::new);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BlockStateHolder> Optional<T> wrapBlockState0(Object blockState) {
        // ORDER IS IMPORTANT

        if (blockState instanceof Sign) {
            return Optional.of((T) new SignBlockStateHolder((Sign) blockState));
        }

        if (blockState instanceof TileState) {
            return Optional.of((T) new TileBlockStateHolder((TileState) blockState));
        }

        if (blockState instanceof BlockState) {
            return Optional.of((T) new GenericBlockStateHolder((BlockState) blockState));
        }

        return Optional.empty();
    }

    @Override
    protected <T extends BlockStateHolder> Optional<T> getBlockStateFromBlock0(BlockHolder blockHolder) {
        return wrapBlockState0(blockHolder.as(Block.class).getState());
    }
}
