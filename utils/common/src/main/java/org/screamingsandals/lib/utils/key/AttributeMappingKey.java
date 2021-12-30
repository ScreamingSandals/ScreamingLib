package org.screamingsandals.lib.utils.key;

import org.screamingsandals.lib.utils.StringUtils;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Special case of Namespaced ID
 */
public class AttributeMappingKey extends NamespacedMappingKey {
    public static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(?:(?<namespace>[A-Za-z0-9_.\\-]*):)?((?<attributeGroup>[A-Za-z]+)\\.)?(?<key>[A-Za-z0-9_.\\-/ ]*)$");

    protected AttributeMappingKey(String namespace, String key) {
        super(namespace, key);
    }

    public static Optional<AttributeMappingKey> ofAttributeOptional(String combinedString) {
        var matcher = RESOLUTION_PATTERN.matcher(combinedString);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        var namespace = matcher.group("namespace") != null ? matcher.group("namespace").toLowerCase() : "minecraft";
        var attributeGroup = matcher.group("attributeGroup");
        var key = matcher.group("key").replaceAll(" ", "_");
        if (attributeGroup == null) {
            // Bukkit -> Vanilla Attribute
            if (key.toLowerCase().startsWith("generic_") || key.toLowerCase().startsWith("horse_") || key.toLowerCase().startsWith("zombie_")) {
                var split1 = key.split("_", 2);
                attributeGroup = split1[0];
                key = split1[1];
            } else {
                attributeGroup = "generic";
            }
        }

        if (!key.contains("_")) {
            // Camel to snake
            key = StringUtils.Case.LOWER_CAMEL.toSnake(key);
        }
        key = key.toLowerCase();

        return ofAttributeOptional(namespace, attributeGroup.toLowerCase() + "." + key);
    }

    public static AttributeMappingKey of(String combinedString) {
        return ofAttributeOptional(combinedString).orElseThrow(() -> new IllegalArgumentException(combinedString + " doesn't match validation patterns!"));
    }

    @SuppressWarnings("unchecked")
    public static Optional<NamespacedMappingKey> ofOptional(String combinedString) {
        return (Optional) ofAttributeOptional(combinedString); // Java is weird in some cases :)
    }

    public static Optional<AttributeMappingKey> ofAttributeOptional(String namespace, String key) {
        if (!VALID_NAMESPACE.matcher(namespace).matches() || !VALID_KEY.matcher(key).matches()) {
            return Optional.empty();
        }

        return Optional.of(new AttributeMappingKey(namespace, key));
    }

    @SuppressWarnings("unchecked")
    public static Optional<NamespacedMappingKey> ofOptional(String namespace, String key) {
        return (Optional) ofAttributeOptional(namespace, key); // Java is weird in some cases :)
    }

    public static AttributeMappingKey of(String namespace, String key) {
        return ofAttributeOptional(namespace, key).orElseThrow(() -> new IllegalArgumentException(namespace + ":" + key + " doesn't match validation patterns!"));
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof NamespacedMappingKey) {
            return super.equals(object);
        }

        var namespacedKey = AttributeMappingKey.of(object.toString());

        return this.getNamespace().equals(namespacedKey.getNamespace()) && this.getKey().equals(namespacedKey.getKey());
    }
}
