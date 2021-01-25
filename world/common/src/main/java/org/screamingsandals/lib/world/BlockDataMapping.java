package org.screamingsandals.lib.world;

import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractMapping;

import java.util.Optional;
import java.util.function.Supplier;

@AbstractMapping
public abstract class BlockDataMapping {
    protected BidirectionalConverter<BlockDataHolder> converter = BidirectionalConverter.<BlockDataHolder>build()
            .registerP2W(BlockDataHolder.class, e -> e);

    private static BlockDataMapping mapping = null;
    private static boolean initialized = false;

    public static boolean isInitialized() {
        return initialized;
    }

    public static Optional<BlockDataHolder> resolve(Object obj) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockDataMapping are not initialized yet.");
        }

        return mapping.converter.convertOptional(obj);
    }

    public static <T> T convert(BlockDataHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockDataMapping are not initialized yet.");
        }
        return mapping.converter.convert(holder, newType);
    }

    @SneakyThrows
    public static void init(Supplier<BlockDataMapping> mappingSupplier) {
        if (mapping != null) {
            throw new UnsupportedOperationException("BlockDataMapping are already initialized.");
        }

        mapping = mappingSupplier.get();
        mapping.converter.finish();
        initialized = true;
    }

    public static Optional<BlockDataHolder> getBlockStateAt(LocationHolder location) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockDataMapping are not initialized yet.");
        }

        return mapping.getBlockStateAt0(location);
    }

    public static void setBlockStateAt(LocationHolder location, BlockDataHolder stateHolder) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockDataMapping are not initialized yet.");
        }

        mapping.setBlockStateAt0(location, stateHolder);
    }

    protected abstract Optional<BlockDataHolder> getBlockStateAt0(LocationHolder location);

    protected abstract void setBlockStateAt0(LocationHolder location, BlockDataHolder stateHolder);
}
