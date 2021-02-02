package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.utils.annotations.AbstractService;

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

    protected abstract <T extends EntityBasic> Optional<T> wrapEntity0(Object entity);

    public static boolean isInitialized() {
        return mapper != null;
    }
}
