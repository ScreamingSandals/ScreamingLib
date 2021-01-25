package org.screamingsandals.lib.world;

import lombok.SneakyThrows;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractMapping;

import java.util.Optional;
import java.util.function.Supplier;

@AbstractMapping
public abstract class BlockMapping {
    protected BidirectionalConverter<BlockHolder> converter = BidirectionalConverter.<BlockHolder>build()
            .registerP2W(BlockHolder.class, e -> e);

    private static BlockMapping mapping = null;
    private static boolean initialized = false;

    public static boolean isInitialized() {
        return initialized;
    }

    public static Optional<BlockHolder> resolve(Object obj) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapping is not initialized yet.");
        }

        return mapping.converter.convertOptional(obj);
    }

    public static <T> T convert(BlockHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapping is not initialized yet.");
        }
        return mapping.converter.convert(holder, newType);
    }

    @SneakyThrows
    public static void init(Supplier<BlockMapping> mappingSupplier) {
        if (mapping != null) {
            throw new UnsupportedOperationException("BlockMapping is already initialized.");
        }

        mapping = mappingSupplier.get();
        mapping.converter.finish();
        initialized = true;
    }

    public static BlockHolder getBlockAt(LocationHolder location) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapping is already initialized.");
        }

        return mapping.getBlockAt0(location);
    }

    public static void setBlockAt(LocationHolder location, MaterialHolder material) {
        if (mapping == null) {
            throw new UnsupportedOperationException("BlockMapping is already initialized.");
        }

        mapping.setBlockAt0(location, material);
    }

    protected abstract BlockHolder getBlockAt0(LocationHolder location);

    protected abstract void setBlockAt0(LocationHolder location, MaterialHolder material);
}
