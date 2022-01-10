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

package org.screamingsandals.lib.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@Accessors(chain = true, fluent = true)
public class BlockDataTranslatorBuilder {
    private final List<DataEntry> entries = new ArrayList<>();

    public BlockDataTranslatorBuilder lowerBitValueSet(String name, int mask, Map<Integer, String> values, String defaultValue) {
        values.forEach((aByte, value) -> entries.add(new DataEntry(b -> (byte) ((b & ~mask) | aByte), b -> (b & mask) == aByte, name, value, s -> s == null && defaultValue.equals(value) || value.equalsIgnoreCase(s))));
        return this;
    }

    public BlockDataTranslatorBuilder maskedBoolean(String name, int mask) {
        return maskedBoolean(name, mask, "true", "false");
    }

    public BlockDataTranslatorBuilder maskedBoolean(String name, int mask, String trueString, String falseString) {
        entries.add(new DataEntry(b -> (byte) (b | mask), b -> (b & mask) == mask, name, trueString, trueString::equalsIgnoreCase));
        entries.add(new DataEntry(b -> (byte) (b & ~mask), b -> (b & mask) != mask, name, falseString, s -> !trueString.equalsIgnoreCase(s)));
        return this;
    }

    public Pair<Function<Byte, Map<String, String>>, Function<Map<String, String>, Byte>> build() {
        Function<Byte, Map<String, String>> toFlattening = b -> {
            var map = new HashMap<String, String>();
            for (var entry : entries) {
                if (entry.predicate.test(b)) {
                    map.put(entry.flatteningName, entry.flatteningValue);
                }
            }
            return map;
        };

        Function<Map<String, String>, Byte> toLegacy = map -> {
            byte data = 0;
            for (var entry : entries) {
                if (entry.flatteningPredicate.test(map.get(entry.flatteningName))) {
                    data = entry.maskFunction.apply(data);
                }
            }
            return data;
        };


        return Pair.of(toFlattening, toLegacy);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataEntry {
        public Function<Byte, Byte> maskFunction;
        public Predicate<Byte> predicate;
        public String flatteningName;
        public String flatteningValue;
        public Predicate<@Nullable String> flatteningPredicate;
    }
}
