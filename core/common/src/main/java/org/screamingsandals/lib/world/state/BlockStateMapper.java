package org.screamingsandals.lib.world.state;

import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.BlockHolder;

import java.util.Optional;
import java.util.function.Supplier;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class BlockStateMapper {

    private static BlockStateMapper blockStateMapper = null;

    @SneakyThrows
    public static void init(Supplier<BlockStateMapper> blockStateMapperSupplier) {
        if (blockStateMapper != null) {
            throw new UnsupportedOperationException("BlockStateMapper is already initialized.");
        }

        blockStateMapper = blockStateMapperSupplier.get();
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockStateHolder> Optional<T> wrapBlockState(Object blockState) {
        if (blockStateMapper == null) {
            throw new UnsupportedOperationException("BlockStateMapper is not initialized yet.");
        }
        if (blockState instanceof BlockStateHolder) {
            return Optional.of((T) blockState);
        }
        return blockStateMapper.wrapBlockState0(blockState);
    }

    protected abstract <T extends BlockStateHolder> Optional<T> wrapBlockState0(Object blockState);

    public static <T extends BlockStateHolder> Optional<T> getBlockStateFromBlock(BlockHolder blockHolder) {
        if (blockStateMapper == null) {
            throw new UnsupportedOperationException("BlockStateMapper is not initialized yet.");
        }
        return blockStateMapper.getBlockStateFromBlock0(blockHolder);
    }

    protected abstract <T extends BlockStateHolder> Optional<T> getBlockStateFromBlock0(BlockHolder blockHolder);

    public static boolean isInitialized() {
        return blockStateMapper != null;
    }
}
