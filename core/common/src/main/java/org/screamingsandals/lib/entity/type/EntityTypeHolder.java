package org.screamingsandals.lib.entity.type;

import lombok.Data;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
public class EntityTypeHolder implements ComparableWrapper {
    private final String platformName;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T as(Class<T> type) {
        return EntityTypeMapping.convertEntityTypeHolder(this, type);
    }

    public boolean isAlive() {
        return EntityTypeMapping.isAlive(this);
    }

    /**
     * Compares the entity type and the object
     *
     * @param entityType Object that represents entity type
     * @return true if specified entity type is the same as this
     */
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    public boolean is(Object entityType) {
        return equals(ofOptional(entityType).orElse(null));
    }

    /**
     * Compares the entity type and the objects
     *
     * @param entityTypes Array of objects that represents entity type
     * @return true if at least one of the entity type objects is same as this
     */
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    public boolean is(Object... entityTypes) {
        return Arrays.stream(entityTypes).anyMatch(this::is);
    }

    public <T extends EntityBasic> Optional<T> spawn(LocationHolder location) {
        return EntityMapper.spawn(this, location);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    public static EntityTypeHolder of(Object entityType) {
        return ofOptional(entityType).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    public static Optional<EntityTypeHolder> ofOptional(Object entityType) {
        if (entityType instanceof EntityTypeHolder) {
            return Optional.of((EntityTypeHolder) entityType);
        }
        return EntityTypeMapping.resolve(entityType);
    }

    public static List<EntityTypeHolder> all() {
        return EntityTypeMapping.getValues();
    }
}
