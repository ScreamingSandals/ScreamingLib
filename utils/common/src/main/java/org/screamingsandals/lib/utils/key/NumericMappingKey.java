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

package org.screamingsandals.lib.utils.key;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Data
@RequiredArgsConstructor(staticName = "of")
public class NumericMappingKey implements MappingKey {
    private final int number;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof NumericMappingKey) {
            return number == ((NumericMappingKey) object).number;
        }
        try {
            var number2 = Integer.parseInt(object.toString());
            return number2 == number;
        } catch (NumberFormatException ignored) {}
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    @NotNull
    public String toString() {
        return String.valueOf(number);
    }
}
