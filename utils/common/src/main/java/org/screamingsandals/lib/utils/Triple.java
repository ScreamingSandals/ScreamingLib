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

package org.screamingsandals.lib.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Triple<F, S, T>  {
    private final F first;
    private final S second;
    private final T third;

    public static <F, S, T> Triple<F, S, T> of(F first, S second, T third) {
        return new Triple<>(first, second, third);
    }

    public static <F, S, T> Triple<F, S, T> empty() {
        return new Triple<>(null, null, null);
    }

    public boolean areAllPresent() {
        return first != null && second != null && third != null;
    }

    public boolean isPresent() {
        return first != null || second != null || third != null;
    }

    public boolean isEmpty() {
        return first == null && second == null && third == null;
    }

    public F first() {
        return first;
    }

    public S second() {
        return second;
    }

    public T third() {
        return third;
    }

}
