package org.screamingsandals.lib.entity.type;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Arrays;

@Data
public class EntityTypeHolder implements Wrapper {
    private final String platformName;

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
    public boolean is(Object entityType) {
        return equals(EntityTypeMapping.resolve(entityType).orElse(null));
    }

    /**
     * Compares the entity type and the objects
     *
     * @param entityTypes Array of objects that represents entity type
     * @return true if at least one of the entity type objects is same as this
     */
    public boolean is(Object... entityTypes) {
        return Arrays.stream(entityTypes).anyMatch(this::is);
    }
}
