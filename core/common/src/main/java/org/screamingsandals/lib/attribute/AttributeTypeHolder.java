package org.screamingsandals.lib.attribute;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Optional;

@Data
public class AttributeTypeHolder implements Wrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return AttributeTypeMapping.convertAttributeTypeHolder(this, type);
    }

    public static AttributeTypeHolder of(Object attributeType) {
        return ofOptional(attributeType).orElseThrow();
    }

    public static Optional<AttributeTypeHolder> ofOptional(Object attributeType) {
        if (attributeType instanceof AttributeTypeHolder) {
            return Optional.of((AttributeTypeHolder) attributeType);
        }
        return AttributeTypeMapping.resolve(attributeType);
    }
}
