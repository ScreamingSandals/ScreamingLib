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

package org.screamingsandals.lib.utils.data;

import org.screamingsandals.lib.impl.utils.data.SimpleDataContainer;

import java.util.Map;
import java.util.Optional;

public interface DataContainer {

    /**
     * @return New data container.
     */
    static DataContainer get() {
        return SimpleDataContainer.get();
    }

    /**
     * Immutable copy of the data in this hologram.
     *
     * @return copy of data that this hologram has.
     */
    Map<String, Object> getAll();

    <T> T get(String key);

    <T> Optional<T> getOptional(String key);

    boolean contains(String key);

    boolean isEmpty();

    void set(String key, Object data);

    void add(String key, Object data);
}
