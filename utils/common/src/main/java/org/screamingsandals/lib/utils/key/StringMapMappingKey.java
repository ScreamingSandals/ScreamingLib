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

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor(staticName = "of")
public class StringMapMappingKey implements MappingKey {
    private final Map<String, String> str;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof StringMapMappingKey) {
            return Objects.equals(str, ((StringMapMappingKey) object).str);
        }
        if (object instanceof Map) {
            return Objects.equals(str, object);
        }
        return toString().equalsIgnoreCase(String.valueOf(object));
    }

    @Override
    public int hashCode() {
        return Objects.hash(str);
    }

    @Override
    @NotNull
    public String toString() {
        return '[' + str.entrySet().stream().map(e -> e.getKey().toLowerCase() + "=" + e.getValue().toLowerCase()).collect(Collectors.joining(",")) + ']';
    }
}
