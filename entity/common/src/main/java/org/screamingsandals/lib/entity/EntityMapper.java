package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;
import java.util.function.Supplier;

@AbstractService
public abstract class EntityMapper {

    private static EntityMapper mapper;

    public static void init(Supplier<EntityMapper> supplier) {
        if (mapper != null) {
            throw new UnsupportedOperationException("EntityMapper is already initialized");
        }

        mapper = supplier.get();
    }

    public static Optional<EntityBasic> wrapEntity(Object entity) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.wrapEntity0(entity);
    }

    public static Optional<EntityBasic> spawn(EntityTypeHolder entityType, LocationHolder locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.spawn0(entityType, locationHolder);
    }

    public static boolean isInitialized() {
        return mapper != null;
    }

    protected abstract <T extends EntityBasic> Optional<T> wrapEntity0(Object entity);

    public abstract Optional<EntityBasic> spawn0(EntityTypeHolder entityType, LocationHolder locationHolder);
}
