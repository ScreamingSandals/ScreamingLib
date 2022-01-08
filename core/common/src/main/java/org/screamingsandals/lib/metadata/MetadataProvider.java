/*
 * Copyright 2022 ScreamingSandals
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
