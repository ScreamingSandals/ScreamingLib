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

package org.screamingsandals.lib.nbt;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

@Data
@Accessors(fluent = true)
public final class IntArrayTag implements CollectionTag, Iterable<Integer> {
    private final int @NotNull [] value;

    @NotNull
    public String toString() {
        return "IntArrayTag(value=" + Arrays.toString(value) + ")";
    }

    public int get(int index) {
        return value[index];
    }

    @Override
    public @NotNull Tag getAsTag(int index) {
        return new IntTag(value[index]);
    }

    public int size() {
        return value.length;
    }

    public PrimitiveIterator.@NotNull OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private int cursor;

            @Override
            public boolean hasNext() {
                return cursor != value.length;
            }

            @Override
            public int nextInt() {
                return value[cursor++];
            }
        };
    }

    public Spliterator.@NotNull OfInt spliterator() {
        return Arrays.spliterator(value);
    }

    public void forEachInt(@NotNull IntConsumer consumer) {
        for (var i : value) {
            consumer.accept(i);
        }
    }

    @NotNull
    public IntStream stream() {
        return IntStream.of(value);
    }
}
