package org.screamingsandals.lib.utils.mapper;

import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.key.MappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractTypeMapper<T extends Wrapper> {
    protected final Map<MappingKey, T> mapping = new HashMap<>();

    protected Optional<T> resolveFromMapping(Object key) {
        var namespaced = !(key instanceof MappingKey) ?
                NamespacedMappingKey.ofOptional(key.toString().trim()) : Optional.of((MappingKey) key);

        if (namespaced.isPresent() && mapping.containsKey(namespaced.get())) {
            return Optional.ofNullable(mapping.get(namespaced.get()));
        }
        return Optional.empty();

    }

    protected void mapAlias(String mappingKey, String alias) {
        if (mappingKey == null || alias == null) {
            throw new IllegalArgumentException("Both mapping keys mustn't be null!");
        }

        var mappingKeyNamespaced = NamespacedMappingKey.of(mappingKey);
        var aliasNamespaced = NamespacedMappingKey.of(alias);

        if (mapping.containsKey(mappingKeyNamespaced) && !mapping.containsKey(aliasNamespaced)) {
            mapping.put(aliasNamespaced, mapping.get(mappingKeyNamespaced));
        } else if (mapping.containsKey(aliasNamespaced) && !mapping.containsKey(mappingKeyNamespaced)) {
            mapping.put(mappingKeyNamespaced, mapping.get(aliasNamespaced));
        }
    }
}
