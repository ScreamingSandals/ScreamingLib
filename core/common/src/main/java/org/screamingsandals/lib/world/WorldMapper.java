package org.screamingsandals.lib.world;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;
import java.util.UUID;

/**
 * Class responsible for converting platform worlds to wrappers.
 */
@AbstractService
public abstract class WorldMapper {
    protected BidirectionalConverter<WorldHolder> converter = BidirectionalConverter.<WorldHolder>build()
            .registerP2W(WorldHolder.class, e -> e);

    private static WorldMapper mapping;

    /**
     * Constructs the location mapper.
     */
    @ApiStatus.Internal
    public WorldMapper() {
        if (mapping != null) {
            throw new UnsupportedOperationException("WorldMapper is already initialized.");
        }
        mapping = this;
    }

    /**
     * Resolves the supplied platform world to its {@link WorldHolder} wrapper, can be empty.
     *
     * @param obj the platform world
     * @return the world wrapper
     */
    public static Optional<WorldHolder> resolve(Object obj) {
        if (mapping == null) {
            throw new UnsupportedOperationException("WorldMapper is not initialized yet.");
        }
        return mapping.converter.convertOptional(obj);
    }

    /**
     * Maps the supplied platform world to its {@link WorldHolder} wrapper.
     *
     * @param input the platform world
     * @param <T> the platform world type
     * @return the world wrapper
     * @throws java.util.NoSuchElementException when the world could not be mapped
     */
    public static <T> WorldHolder wrapWorld(T input) {
        return resolve(input).orElseThrow();
    }

    /**
     * Converts the world holder to a new type (like a platform world type).
     *
     * @param holder the world holder to convert
     * @param newType the new type class
     * @param <T> the new type
     * @return the world holder converted to the supplied type
     * @throws UnsupportedOperationException when the wrapper could not be converted to its new type
     */
    public static <T> T convert(WorldHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("WorldMapper is not initialized yet.");
        }
        return mapping.converter.convert(holder, newType);
    }


    /**
     * Gets the world holder by a supplied {@link UUID}.
     *
     * @param uuid the world uuid
     * @return the world, can be empty
     */
    public static Optional<WorldHolder> getWorld(UUID uuid) {
        if (mapping == null) {
            throw new UnsupportedOperationException("WorldMapper is not initialized yet.");
        }
        return mapping.getWorld0(uuid);
    }

    /**
     * Gets the world holder by a supplied name.
     *
     * @param name the world name
     * @return the world, can be empty
     */
    public static Optional<WorldHolder> getWorld(String name) {
        if (mapping == null) {
            throw new UnsupportedOperationException("WorldMapper is not initialized yet.");
        }
        return mapping.getWorld0(name);
    }

    // abstract methods for implementations

    protected abstract Optional<WorldHolder> getWorld0(UUID uuid);

    protected abstract Optional<WorldHolder> getWorld0(String name);
}