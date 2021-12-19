package org.screamingsandals.lib.metadata;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public interface MetadataProvider {

    boolean supportsMetadata(MetadataKey<?> key);

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
