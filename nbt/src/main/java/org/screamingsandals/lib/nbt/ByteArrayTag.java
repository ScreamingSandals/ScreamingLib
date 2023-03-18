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

import java.util.*;
import java.util.stream.Stream;

@Data
@Accessors(fluent = true)
public final class ByteArrayTag implements CollectionTag, Iterable<Byte> {
    private final byte @NotNull [] value;

    @NotNull
    public String toString() {
        return "ByteArrayTag(value=" + Arrays.toString(value) + ")";
    }

    public byte get(int index) {
        return value[index];
    }

    @Override
    public @NotNull Tag getAsTag(int index) {
        return new ByteTag(value[index]);
    }

    public int size() {
        return value.length;
    }

    @NotNull
    @Override
    public Iterator<@NotNull Byte> iterator() {
        return new Iterator<>() {
            private int cursor;

            @Override
            public boolean hasNext() {
                return cursor != value.length;
            }

            @Override
            @NotNull
            public Byte next() {
                return value[cursor++];
            }
        };
    }

    @NotNull
    public Stream<@NotNull Byte> stream() {
        var byteList = new ArrayList<Byte>();
        for (var val : value) {
            byteList.add(val);
        }
        return byteList.stream();
    }
}
