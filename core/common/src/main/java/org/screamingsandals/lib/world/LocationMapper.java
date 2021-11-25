package org.screamingsandals.lib.world;

import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;
import java.util.UUID;

/**
 * <p>Class responsible for converting platform locations to wrappers.</p>
 */
@AbstractService
public abstract class LocationMapper {
    protected BidirectionalConverter<LocationHolder> converter = BidirectionalConverter.<LocationHolder>build()
            .registerP2W(LocationHolder.class, e -> e)
            .registerW2P(BlockHolder.class, locationHolder -> BlockMapper.resolve(locationHolder).orElse(null));

    private static LocationMapper mapping;

    /**
     * <p>Constructs the location mapper.</p>
     */
    protected LocationMapper() {
        if (mapping != null) {
            throw new UnsupportedOperationException("LocationMapper is already initialized.");
        }
        mapping = this;
    }

    /**
     * <p>Resolves the supplied platform location to its {@link LocationHolder} wrapper, can be empty.</p>
     *
     * @param obj the platform location
     * @return the location wrapper
     */
    public static Optional<LocationHolder> resolve(Object obj) {
        if (mapping == null) {
            throw new UnsupportedOperationException("LocationMapper is not initialized yet.");
        }
        return mapping.converter.convertOptional(obj);
    }

    /**
     * <p>Maps the supplied platform location to its {@link LocationHolder} wrapper.</p>
     *
     * @param input the platform location
     * @param <T> the platform location type
     * @return the location wrapper
     * @throws java.util.NoSuchElementException when the location could not be mapped
     */
    public static <T> LocationHolder wrapLocation(T input) {
        return resolve(input).orElseThrow();
    }

    /**
     * <p>Converts the location holder to a new type (like a platform location type).</p>
     *
     * @param holder the location holder to convert
     * @param newType the new type class
     * @param <T> the new type
     * @return the location holder converted to the supplied type
     * @throws UnsupportedOperationException when the wrapper could not be converted to its new type
     */
    public static <T> T convert(LocationHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("LocationMapper is not initialized yet.");
        }
        return mapping.converter.convert(holder, newType);
    }
}
