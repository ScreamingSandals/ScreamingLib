package org.screamingsandals.lib.utils.key;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NamespacedMappingKey implements MappingKey {
    public static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(?:(?<namespace>[A-Za-z0-9_.\\-]+):)?(?<key>[A-Za-z0-9_.\\-/ ]+)$");
    public static final Pattern VALID_NAMESPACE = Pattern.compile("^[a-z0-9_.\\-]+$");
    public static final Pattern VALID_KEY = Pattern.compile("^[a-z0-9_.\\-/]+$");

    private final String namespace;
    private final String key;

    public static Optional<NamespacedMappingKey> ofOptional(String combinedString) {
        var matcher = RESOLUTION_PATTERN.matcher(combinedString);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        var namespace = matcher.group("namespace") != null ? matcher.group("namespace").toLowerCase() : "minecraft";
        var key = matcher.group("key").replaceAll(" ", "_").toLowerCase();

        return ofOptional(namespace, key);
    }

    public static NamespacedMappingKey of(String combinedString) {
        return ofOptional(combinedString).orElseThrow(() -> new IllegalArgumentException(combinedString + " doesn't match validation patterns!"));
    }

    public static Optional<NamespacedMappingKey> ofOptional(String namespace, String key) {
        if (!VALID_NAMESPACE.matcher(namespace).matches() || !VALID_KEY.matcher(key).matches()) {
            return Optional.empty();
        }

        return Optional.of(new NamespacedMappingKey(namespace, key));
    }

    public static NamespacedMappingKey of(String namespace, String key) {
        return ofOptional(namespace, key).orElseThrow(() -> new IllegalArgumentException(namespace + ":" + key + " doesn't match validation patterns!"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof NamespacedMappingKey) {
            return this.namespace.equals(((NamespacedMappingKey) object).namespace) && this.key.equals(((NamespacedMappingKey) object).key);
        }

        var namespacedKey = NamespacedMappingKey.of(object.toString());

        return this.namespace.equals(namespacedKey.namespace) && this.key.equals(namespacedKey.key);
    }

    @Override
    @NotNull
    public String toString() {
        return namespace + ":" + key;
    }
}
