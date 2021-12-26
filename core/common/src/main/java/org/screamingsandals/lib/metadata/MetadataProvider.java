package org.screamingsandals.lib.metadata;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public interface MetadataProvider {

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

    @Nullable
    <T> T getMetadata(MetadataKey<T> key);

    <T> Optional<T> getMetadataOptional(MetadataKey<T> key);

    default <T> T getMetadataOrElse(MetadataKey<T> key, T orElse) {
        return getMetadataOptional(key).orElse(orElse);
    }

    @Nullable
    <T> Collection<T> getMetadata(MetadataCollectionKey<T> key);
}
