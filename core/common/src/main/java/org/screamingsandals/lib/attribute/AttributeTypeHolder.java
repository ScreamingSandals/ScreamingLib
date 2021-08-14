package org.screamingsandals.lib.attribute;

import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Optional;

@Data
public class AttributeTypeHolder implements Wrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return AttributeTypeMapping.convertAttributeTypeHolder(this, type);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ATTRIBUTE_TYPE)
    public static AttributeTypeHolder of(Object attributeType) {
        return ofOptional(attributeType).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ATTRIBUTE_TYPE)
    public static Optional<AttributeTypeHolder> ofOptional(Object attributeType) {
        if (attributeType instanceof AttributeTypeHolder) {
            return Optional.of((AttributeTypeHolder) attributeType);
        }
        return AttributeTypeMapping.resolve(attributeType);
    }
}
