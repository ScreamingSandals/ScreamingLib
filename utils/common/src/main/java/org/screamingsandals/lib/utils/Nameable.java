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

package org.screamingsandals.lib.utils;

import java.io.Serializable;

/**
 * Something that can have a name.
 */
public interface Nameable extends Serializable {

    /**
     * Creates new {@link Nameable}.
     *
     * @param name the name to use
     * @return new Nameable instance.
     */
    static Nameable of(String name) {
        return () -> name;
    }

    /**
     * @return the name
     */
    String name();
}