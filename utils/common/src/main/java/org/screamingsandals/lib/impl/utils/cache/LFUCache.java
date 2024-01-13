/*
 * Copyright 2024 ScreamingSandals
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LFUCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private final @NotNull Map<@NotNull K, CacheItem<K, V>> map;
    private @Nullable CacheItem<K, V> first;
    private @Nullable CacheItem<K, V> last;

    public LFUCache(int size) {
        this.capacity = size;
        this.map = new HashMap<>(size);
    }

    @Override
    public void put(@NotNull K key, @NotNull V value) {
        var node = new CacheItem<>(key, value);

        if (!map.containsKey(key)) {
            if (size() >= capacity) {
                deleteNode(first);
            }

            if (first != null) {
                node.setNext(first);
                first.setPrevious(node);
            }
            first = node;

            if (last == null) {
                last = node;
            }
        }

        map.put(key, node);
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        if (!map.containsKey(key)) {
            return null;
        }
        var node = map.get(key);
        node.incrementHitCount();
        reorder(node);
        return node.getValue();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        this.map.clear();
    }

    private void deleteNode(@Nullable CacheItem<K, V> node) {
        if (node == null) {
            return;
        }
        if (last == node) {
            last = node.getPrevious();
        }
        if (first == node) {
            first = node.getNext();
        }
        map.remove(node.getKey());
    }

    private void reorder(@NotNull CacheItem<K, V> node) {
        if (last == node) {
            return;
        }
        var nextNode = node.getNext();
        while (nextNode != null) {
            if (nextNode.getHitCount() > node.getHitCount()) {
                break;
            }
            if (first == node) {
                first = nextNode;
            }
            if (node.getPrevious() != null) {
                node.getPrevious().setNext(nextNode);
            }
            nextNode.setPrevious(node.getPrevious());
            node.setPrevious(nextNode);
            node.setNext(nextNode.getNext());
            if (nextNode.getNext() != null) {
                nextNode.getNext().setPrevious(node);
            }
            nextNode.setNext(node);
            nextNode = node.getNext();
        }
        if (node.getNext() == null) {
            last = node;
        }
    }
}