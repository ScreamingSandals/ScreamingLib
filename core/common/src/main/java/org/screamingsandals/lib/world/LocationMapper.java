package org.screamingsandals.lib.world;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;
import java.util.UUID;

@AbstractService
public abstract class LocationMapper {

    protected BidirectionalConverter<LocationHolder> converter = BidirectionalConverter.<LocationHolder>build()
            .registerP2W(LocationHolder.class, e -> e)
            .registerW2P(BlockHolder.class, locationHolder -> BlockMapper.resolve(locationHolder).orElse(null));

    private static LocationMapper mapping;

    protected LocationMapper() {
        if (mapping != null) {
            throw new UnsupportedOperationException("LocationMapper is already initialized.");
        }

        mapping = this;
    }

    public static Optional<LocationHolder> resolve(Object obj) {
        if (mapping == null) {
            throw new UnsupportedOperationException("LocationMapper is not initialized yet.");
        }

        return mapping.converter.convertOptional(obj);
    }

    public static <T> LocationHolder wrapLocation(T input) {
        return resolve(input).orElseThrow();
    }

    public static <T> T convert(LocationHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("LocationMapper is not initialized yet.");
        }
        return mapping.converter.convert(holder, newType);
    }

    public static Optional<WorldHolder> getWorld(UUID uuid) {
        if (mapping == null) {
            throw new UnsupportedOperationException("LocationMapper is not initialized yet.");
        }
        return mapping.getWorld0(uuid);
    }

    public static Optional<WorldHolder> getWorld(String name) {
        if (mapping == null) {
            throw new UnsupportedOperationException("LocationMapper is not initialized yet.");
        }
        return mapping.getWorld0(name);
    }

    protected abstract Optional<WorldHolder> getWorld0(UUID uuid);

    protected abstract Optional<WorldHolder> getWorld0(String name);
}
