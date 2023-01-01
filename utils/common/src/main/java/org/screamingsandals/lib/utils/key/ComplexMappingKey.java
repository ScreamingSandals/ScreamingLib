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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor(staticName = "of")
public class ComplexMappingKey implements MappingKey {
    private final List<MappingKey> mappingKeys;

    public static ComplexMappingKey of(MappingKey...keys) {
        return of(Arrays.asList(keys));
    }

    @Override
    @NotNull
    public String toString() {
        return mappingKeys.stream().map(Object::toString).collect(Collectors.joining(":"));
    }
}
