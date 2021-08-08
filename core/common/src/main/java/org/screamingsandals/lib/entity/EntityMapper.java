package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.material.Item;
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

    public static <T extends EntityBasic> Optional<T> wrapEntity(Object entity) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.wrapEntity0(entity);
    }

    public static <T extends EntityBasic> Optional<T> spawn(Object entityType, LocationHolder locationHolder) {
        if (entityType instanceof EntityTypeHolder) {
            return spawn((EntityTypeHolder) entityType, locationHolder);
        } else {
            var type = EntityTypeMapping.resolve(entityType);
            if (type.isPresent()) {
                return spawn(type.get(), locationHolder);
            } else {
                return Optional.empty();
            }
        }
    }

    public static <T extends EntityBasic> Optional<T> spawn(EntityTypeHolder entityType, LocationHolder locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.spawn0(entityType, locationHolder);
    }

    public static Optional<EntityItem> dropItem(Item item, LocationHolder locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.dropItem0(item, locationHolder);
    }

    public static Optional<EntityExperience> dropExperience(int experience, LocationHolder locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.dropExperience0(experience, locationHolder);
    }

    public static Optional<EntityLightning> strikeLightning(LocationHolder locationHolder) {
        if (mapper == null) {
            throw new UnsupportedOperationException("EntityMapper is not initialized yet.");
        }
        return mapper.strikeLightning0(locationHolder);
    }

    public static boolean isInitialized() {
        return mapper != null;
    }

    protected abstract <T extends EntityBasic> Optional<T> wrapEntity0(Object entity);

    public abstract <T extends EntityBasic>  Optional<T> spawn0(EntityTypeHolder entityType, LocationHolder locationHolder);

    public abstract Optional<EntityItem> dropItem0(Item item, LocationHolder locationHolder);

    public abstract Optional<EntityExperience> dropExperience0(int experience, LocationHolder locationHolder);

    public abstract Optional<EntityLightning> strikeLightning0(LocationHolder locationHolder);
}
