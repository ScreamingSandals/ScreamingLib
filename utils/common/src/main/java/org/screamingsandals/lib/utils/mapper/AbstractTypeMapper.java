/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.utils.mapper;

import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.utils.key.MappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.*;

public abstract class AbstractTypeMapper<T extends Wrapper> {
    protected final Map<MappingKey, T> mapping = new HashMap<>();
    protected final List<T> values = new LinkedList<>();

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
