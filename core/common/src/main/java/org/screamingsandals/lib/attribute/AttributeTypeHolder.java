package org.screamingsandals.lib.attribute;

import lombok.Data;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
public class AttributeTypeHolder implements ComparableWrapper {
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

    public static List<AttributeTypeHolder> all() {
        return AttributeTypeMapping.getValues();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ATTRIBUTE_TYPE)
    @Override
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ATTRIBUTE_TYPE)
    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
