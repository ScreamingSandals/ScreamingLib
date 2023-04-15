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

package org.screamingsandals.lib.impl.utils.cache;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class CacheItem<K, V> {
    private final @NotNull K key;
    private final @NotNull V value;
    private @Nullable CacheItem<K, V> previous;
    private @Nullable CacheItem<K, V> next;
    private int hitCount;

    public CacheItem(@NotNull K key, @NotNull V value) {
        this.value = value;
        this.key = key;
    }
    
    public void incrementHitCount() {
        this.hitCount++;
    }
}