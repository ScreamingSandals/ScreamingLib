package org.screamingsandals.lib.world;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.WrappedLocation;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Optional;
import java.util.UUID;

/**
 * Class responsible for converting platform locations to wrappers.
 */
@AbstractService
public abstract class LocationMapper {
    protected BidirectionalConverter<LocationHolder> converter = BidirectionalConverter.<LocationHolder>build()
            .registerP2W(LocationHolder.class, e -> e)
            .registerW2P(BlockHolder.class, locationHolder -> BlockMapper.resolve(locationHolder).orElse(null));

    private static LocationMapper mapping;

    /**
     * Constructs the location mapper.
     */
    @ApiStatus.Internal
    public LocationMapper() {
        if (mapping != null) {
            throw new UnsupportedOperationException("LocationMapper is already initialized.");
        }
        mapping = this;

        if (Reflect.has("com.google.protobuf.MessageOrBuilder")) {
            converter
                .registerP2W(WrappedLocation.class, location -> {
                    final var world = WorldMapper.getWorld(UUID.fromString(location.getWorldUuid()));
                    if (world.isEmpty()) {
                        return null;
                    }
                    return new LocationHolder(
                            location.getX(), location.getY(), location.getZ(),
                            location.getYaw(), location.getPitch(),
                            world.get());
                });
        }
    }

    /**
     * Resolves the supplied platform location to its {@link LocationHolder} wrapper, can be empty.
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
     * Maps the supplied platform location to its {@link LocationHolder} wrapper.
     *
     * @param input the platform location
     * @param <T>   the platform location type
     * @return the location wrapper
     * @throws java.util.NoSuchElementException when the location could not be mapped
     */
    public static <T> LocationHolder wrapLocation(T input) {
        return resolve(input).orElseThrow();
    }

    /**
     * Converts the location holder to a new type (like a platform location type).
     *
     * @param holder  the location holder to convert
     * @param newType the new type class
     * @param <T>     the new type
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
