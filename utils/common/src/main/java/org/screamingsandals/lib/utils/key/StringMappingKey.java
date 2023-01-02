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

package org.screamingsandals.lib.utils.key;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Data
@RequiredArgsConstructor(staticName = "of")
public final class StringMappingKey implements MappingKey {
    private final String str;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof StringMappingKey) {
            return Objects.equals(str, ((StringMappingKey) object).str);
        }
        return str.equals(String.valueOf(object));
    }

    @Override
    public int hashCode() {
        return Objects.hash(str);
    }

    @Override
    public @NotNull String toString() {
        return str;
    }
}
