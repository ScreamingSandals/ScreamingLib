package org.screamingsandals.lib.entity.type;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;

@Data
public class EntityTypeHolder implements Wrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return EntityTypeMapping.convertEntityTypeHolder(this, type);
    }
}
