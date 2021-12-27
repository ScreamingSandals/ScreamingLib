package org.screamingsandals.lib.attribute;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface AttributeTypeHolder extends ComparableWrapper, RawValueHolder {

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    String platformName();

    @CustomAutocompletion(CustomAutocompletion.Type.ATTRIBUTE_TYPE)
    static AttributeTypeHolder of(Object attributeType) {
        return ofOptional(attributeType).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ATTRIBUTE_TYPE)
    static Optional<AttributeTypeHolder> ofOptional(Object attributeType) {
        if (attributeType instanceof AttributeTypeHolder) {
            return Optional.of((AttributeTypeHolder) attributeType);
        }
        return AttributeTypeMapping.resolve(attributeType);
    }

    static List<AttributeTypeHolder> all() {
        return AttributeTypeMapping.getValues();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ATTRIBUTE_TYPE)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.ATTRIBUTE_TYPE)
    @Override
    boolean is(Object... objects);
}
