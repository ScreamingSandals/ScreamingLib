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

package org.screamingsandals.lib.utils.registry;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.cache.Cache;
import org.screamingsandals.lib.utils.cache.LFUCache;

@RequiredArgsConstructor
public abstract class Registry<T extends RegistryItem> {
    protected final @NotNull Class<T> type;
    private final @NotNull Cache<@NotNull Object, @NotNull T> cache = new LFUCache<>(200);
    private @Nullable RegistryItemStream<T> cachedBasicStream;

    @Contract("null -> null")
    public final @Nullable T resolveMapping(@Nullable Object object) {
        if (object == null) {
            return null;
        }

        if (this.type.isInstance(object)) {
            //noinspection unchecked
            return (T) object;
        }

        var cachedRow = this.cache.get(object);
        if (cachedRow != null) {
            return cachedRow;
        }

        var result = this.resolveMapping0(object);
        if (result != null) {
            this.cache.put(object, result);
            return result;
        }

        return null;
    }

    @ApiStatus.Internal
    protected abstract @Nullable T resolveMapping0(@NotNull Object object);

    public @NotNull RegistryItemStream<@NotNull T> getRegistryItemStream() {
        if (cachedBasicStream == null) {
            cachedBasicStream = getRegistryItemStream0();
        }
        return cachedBasicStream;
    }

    /**
     * Returned stream should be reusable.
     */
    @ApiStatus.Internal
    protected abstract @NotNull RegistryItemStream<@NotNull T> getRegistryItemStream0();
}
