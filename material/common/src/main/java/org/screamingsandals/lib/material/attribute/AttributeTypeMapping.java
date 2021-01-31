package org.screamingsandals.lib.material.attribute;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.key.AttributeMappingKey;
import org.screamingsandals.lib.utils.key.MappingKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class AttributeTypeMapping {
    private static AttributeTypeMapping attributeTypeMapping;

    protected final BidirectionalConverter<AttributeTypeHolder> attributeTypeConverter = BidirectionalConverter.build();
    protected final Map<MappingKey, AttributeTypeHolder> mapping = new HashMap<>();

    public static void init(Supplier<AttributeTypeMapping> supplier) {
        if (attributeTypeMapping != null) {
            throw new UnsupportedOperationException("AttributeTypeMapping is already initialized.");
        }

        attributeTypeMapping = supplier.get();
        attributeTypeMapping.attributeTypeConverter.finish();
    }

    public static Optional<AttributeTypeHolder> resolve(Object attributeType) {
        if (attributeTypeMapping == null) {
            throw new UnsupportedOperationException("AttributeTypeMapping is not initialized yet.");
        }

        if (attributeType == null) {
            return Optional.empty();
        }

        var converted = attributeTypeMapping.attributeTypeConverter.convertOptional(attributeType);
        if (converted.isPresent()) {
            return converted;
        }

        var namespacedKey = AttributeMappingKey.ofOptional(attributeType.toString());

        if (namespacedKey.isPresent()) {
            if (attributeTypeMapping.mapping.containsKey(namespacedKey.get())) {
                return Optional.of(attributeTypeMapping.mapping.get(namespacedKey.get()));
            }
        }

        return Optional.empty();
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
