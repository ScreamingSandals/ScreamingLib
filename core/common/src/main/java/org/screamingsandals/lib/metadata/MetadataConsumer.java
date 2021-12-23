package org.screamingsandals.lib.metadata;

import java.util.Collection;

public interface MetadataConsumer {

    /**
     *
     *
     * @param key metadata that should the provider support
     * @return true if the metadata is supported; false if the metadata is not supported or this information is unknown
     */
    boolean supportsMetadata(MetadataKey<?> key);

    /**
     *
     *
     * @param key metadata that should the provider support
     * @return true if the metadata is supported; false if the metadata is not supported or this information is unknown
     */
    boolean supportsMetadata(MetadataCollectionKey<?> key);

    <T> MetadataConsumer setMetadata(MetadataKey<T> key, T value);

    <T> MetadataConsumer setMetadata(MetadataCollectionKey<T> key, Collection<T> value);

    <T> MetadataConsumer addToListMetadata(MetadataCollectionKey<T> key, T value);
}
