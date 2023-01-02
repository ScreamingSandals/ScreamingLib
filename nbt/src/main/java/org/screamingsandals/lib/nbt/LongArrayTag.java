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

package org.screamingsandals.lib.nbt;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;

@Data
@Accessors(fluent = true)
public final class LongArrayTag implements CollectionTag, Iterable<Long> {
    private final long @NotNull [] value;

    public @NotNull String toString() {
        return "LongArrayTag(value=" + Arrays.toString(value) + ")";
    }

    public long get(int index) {
        return value[index];
    }

    @Override
    public @NotNull Tag getAsTag(int index) {
        return new LongTag(value[index]);
    }

    public int size() {
        return value.length;
    }

    public PrimitiveIterator.@NotNull OfLong iterator() {
        return new PrimitiveIterator.OfLong() {
            private int cursor;

            @Override
            public boolean hasNext() {
                return cursor != value.length;
            }

            @Override
            public long nextLong() {
                return value[cursor++];
            }
        };
    }

    public Spliterator.@NotNull OfLong spliterator() {
        return Arrays.spliterator(value);
    }

    public void forEachLong(@NotNull LongConsumer consumer) {
        for (var l : value) {
            consumer.accept(l);
        }
    }

    public @NotNull LongStream stream() {
        return LongStream.of(value);
    }
}
