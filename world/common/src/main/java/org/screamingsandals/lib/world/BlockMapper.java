package org.screamingsandals.lib.world;

import lombok.SneakyThrows;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;
import java.util.function.Supplier;

@AbstractService
public abstract class BlockMapper {
    protected BidirectionalConverter<BlockHolder> converter = BidirectionalConverter.<BlockHolder>build()
            .registerP2W(BlockHolder.class, e -> e);

    private static BlockMapper mapping = null;
    private static boolean initialized = false;

    public static boolean isInitialized() {
        return initialized;
    }

    public static Optional<BlockHolder> resolve(Object obj) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is not initialized yet.");
        }
        
        return mapping.converter.convertOptional(obj);
    }

    public static <T> BlockHolder wrapBlock(T block) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is not initialized yet.");
        }
        return mapping.converter.convert(block);
    }

    public static <T> T convert(BlockHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is not initialized yet.");
        }
        return mapping.converter.convert(holder, newType);
    }

    @SneakyThrows
    public static void init(Supplier<BlockMapper> mappingSupplier) {
        if (mapping != null) {
            throw new UnsupportedOperationException("BlockMapper is already initialized.");
        }

        mapping = mappingSupplier.get();
        initialized = true;
    }

    public static BlockHolder getBlockAt(LocationHolder location) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is already initialized.");
        }

        return mapping.getBlockAt0(location);
    }

    public static void setBlockAt(LocationHolder location, MaterialHolder material) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapper is already initialized.");
        }

        mapping.setBlockAt0(location, material);
    }

    protected abstract BlockHolder getBlockAt0(LocationHolder location);

    protected abstract void setBlockAt0(LocationHolder location, MaterialHolder material);
}
