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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Data
@Accessors(fluent = true)
public final class ListTag implements Tag, Iterable<Tag> {
    @NotNull
    private final List<@NotNull Tag> tags;

    public ListTag(@NotNull List<@NotNull Tag> tags) {
        Class<?> determinedType = null;
        for (var t : tags) {
            if (determinedType == null) {
               determinedType = t.getClass();
            } else if (!determinedType.isInstance(t) && (determinedType != ByteTag.class || !(t instanceof BooleanTag)) && (determinedType != BooleanTag.class || !(t instanceof ByteTag))) {
                throw new IllegalArgumentException("This is a list of " + determinedType.getSimpleName() + ", got " + t.getClass().getSimpleName());
            }
        }
        this.tags = tags;
    }

    @NotNull
    public List<@NotNull Tag> value() {
        return List.copyOf(tags); // keep this class immutable
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public ListTag with(@NotNull Tag tag) {
        if (!tags.isEmpty()) {
            var firstTag = tags.get(0);
            if (!firstTag.getClass().isInstance(tag) && (!(firstTag instanceof ByteTag) || !(tag instanceof BooleanTag)) && (!(firstTag instanceof BooleanTag) || !(tag instanceof ByteTag))) {
                throw new IllegalArgumentException("This is a list of " + firstTag.getClass().getSimpleName() + ", got " + tag.getClass().getSimpleName());
            }
        }
        var clone = new ArrayList<>(tags);
        clone.add(tag);
        return new ListTag(clone);
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public ListTag without(@NotNull Tag tag) {
        var clone = new ArrayList<>(tags);
        clone.remove(tag);
        return new ListTag(clone);
    }

    @NotNull
    @Override
    public Iterator<@NotNull Tag> iterator() {
        return new Iterator<>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor != tags.size();
            }

            @Override
            @NotNull
            public Tag next() {
                try {
                    var tag = tags.get(cursor);
                    cursor++;
                    return tag;
                } catch (IndexOutOfBoundsException exception) {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    @NotNull
    public Stream<@NotNull Tag> stream() {
        return tags.stream();
    }
}
