package org.screamingsandals.lib.attribute;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;

@Data
public class AttributeTypeHolder implements Wrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return AttributeTypeMapping.convertAttributeTypeHolder(this, type);
    }
}
