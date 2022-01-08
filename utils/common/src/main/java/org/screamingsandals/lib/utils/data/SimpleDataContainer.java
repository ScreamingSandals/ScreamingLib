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

package org.screamingsandals.lib.utils.data;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class SimpleDataContainer implements DataContainer {
    private final Map<String, Object> dataMap = new ConcurrentHashMap<>();

    static SimpleDataContainer get() {
        return new SimpleDataContainer();
    }

    @Override
    public Map<String, Object> getAll() {
        return Map.copyOf(dataMap);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        if (dataMap.containsKey(key)) {
            return (T) dataMap.get(key);
        }

        throw new NullPointerException("Data for key " + key + " was not found!");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getOptional(String key) {
        return Optional.ofNullable((T) dataMap.get(key));
    }

    @Override
    public boolean contains(String key) {
        return dataMap.containsKey(key);
    }

    @Override
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    @Override
    public void set(String key, Object data) {
        dataMap.put(key, data);
    }

    @Override
    public void add(String key, Object data) {
        dataMap.putIfAbsent(key, data);
    }
}
