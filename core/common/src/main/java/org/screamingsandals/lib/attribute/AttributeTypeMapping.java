package org.screamingsandals.lib.attribute;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.key.AttributeMappingKey;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Optional;
import java.util.function.Supplier;

@AbstractService
public abstract class AttributeTypeMapping extends AbstractTypeMapper<AttributeTypeHolder> {
    private static AttributeTypeMapping attributeTypeMapping;

    protected final BidirectionalConverter<AttributeTypeHolder> attributeTypeConverter = BidirectionalConverter.<AttributeTypeHolder>build()
            .registerP2W(AttributeTypeHolder.class, e -> e);

    public static void init(Supplier<AttributeTypeMapping> attributeTypeMappingSupplier) {
        if (attributeTypeMapping != null) {
            throw new UnsupportedOperationException("AttributeTypeMapping is already initialized.");
        }

        attributeTypeMapping = attributeTypeMappingSupplier.get();
    }

    public static Optional<AttributeTypeHolder> resolve(Object attributeType) {
        if (attributeTypeMapping == null) {
            throw new UnsupportedOperationException("AttributeTypeMapping is not initialized yet.");
        }

        if (attributeType == null) {
            return Optional.empty();
        }

        return attributeTypeMapping.attributeTypeConverter.convertOptional(attributeType).or(() -> {
            var namespacedKey = AttributeMappingKey.ofOptional(attributeType.toString());

            if (namespacedKey.isPresent() && attributeTypeMapping.mapping.containsKey(namespacedKey.get())) {
                return Optional.of(attributeTypeMapping.mapping.get(namespacedKey.get()));
            }

            return Optional.empty();
        });
    }

    public static <T> T convertAttributeTypeHolder(AttributeTypeHolder holder, Class<T> newType) {
        if (attributeTypeMapping == null) {
            throw new UnsupportedOperationException("AttributeTypeMapping is not initialized yet.");
        }
        return attributeTypeMapping.attributeTypeConverter.convert(holder, newType);
    }

    public static boolean isInitialized() {
        return attributeTypeMapping != null;
    }
}
