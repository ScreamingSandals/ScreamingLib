package org.screamingsandals.lib.entity.type;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface EntityTypeHolder extends ComparableWrapper, RawValueHolder {
    String platformName();

    boolean isAlive();

    /**
     * Compares the entity type and the object
     *
     * @param entityType Object that represents entity type
     * @return true if specified entity type is the same as this
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    boolean is(Object entityType);

    /**
     * Compares the entity type and the objects
     *
     * @param entityTypes Array of objects that represents entity type
     * @return true if at least one of the entity type objects is same as this
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    boolean is(Object... entityTypes);

    <T extends EntityBasic> Optional<T> spawn(LocationHolder location);

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    static EntityTypeHolder of(Object entityType) {
        return ofOptional(entityType).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    static Optional<EntityTypeHolder> ofOptional(Object entityType) {
        if (entityType instanceof EntityTypeHolder) {
            return Optional.of((EntityTypeHolder) entityType);
        }
        return EntityTypeMapping.resolve(entityType);
    }

    static List<EntityTypeHolder> all() {
        return EntityTypeMapping.getValues();
    }
}
