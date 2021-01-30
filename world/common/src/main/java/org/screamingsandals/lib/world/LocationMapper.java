package org.screamingsandals.lib.world;

import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;
import java.util.function.Supplier;

@AbstractService
public abstract class LocationMapper {

    protected BidirectionalConverter<LocationHolder> converter = BidirectionalConverter.<LocationHolder>build()
            .registerP2W(LocationHolder.class, e -> e);

    private static LocationMapper mapping = null;
    private static boolean initialized = false;

    public static Optional<LocationHolder> resolve(Object obj) {
        if (mapping == null) {
            throw new UnsupportedOperationException("Material mapping is not initialized yet.");
        }

        return mapping.converter.convertOptional(obj);
    }

    public static <T> LocationHolder wrapLocation(T input) {
        return resolve(input).orElseThrow();
    }

    public static <T> T convert(LocationHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("Material mapping is not initialized yet.");
        }
        return mapping.converter.convert(holder, newType);
    }

    @SneakyThrows
    public static void init(Supplier<LocationMapper> mappingSupplier) {
        if (mapping != null) {
            throw new UnsupportedOperationException("Material mapping is already initialized.");
        }

        mapping = mappingSupplier.get();
        mapping.converter.finish();
        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
