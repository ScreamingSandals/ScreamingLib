package org.screamingsandals.lib.metadata;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class MetadataCollectionKey<T> {
    /**
     * NBT key
     */
    private final String key;
    private final Class<T> componentType;
}