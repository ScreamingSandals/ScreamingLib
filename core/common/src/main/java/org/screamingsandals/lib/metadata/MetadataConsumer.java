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

package org.screamingsandals.lib.metadata;

import java.util.Collection;

@Deprecated
public interface MetadataConsumer {

    /**
     *
     *
     * @param key metadata that should the provider support
     * @return true if the metadata is supported; false if the metadata is not supported or this information is unknown
     */
    @Deprecated
    boolean supportsMetadata(MetadataKey<?> key);

    /**
     *
     *
     * @param key metadata that should the provider support
     * @return true if the metadata is supported; false if the metadata is not supported or this information is unknown
     */
    @Deprecated
    boolean supportsMetadata(MetadataCollectionKey<?> key);

    @Deprecated
    <T> MetadataConsumer setMetadata(MetadataKey<T> key, T value);

    @Deprecated
    <T> MetadataConsumer setMetadata(MetadataCollectionKey<T> key, Collection<T> value);

    @Deprecated
    <T> MetadataConsumer addToListMetadata(MetadataCollectionKey<T> key, T value);
}
