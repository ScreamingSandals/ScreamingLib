/*
 * Copyright 2025 ScreamingSandals
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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Data
@Accessors(fluent = true)
public final class ListTag implements CollectionTag, Iterable<Tag> {
    public static final @NotNull ListTag EMPTY = new ListTag(List.of());

    private final @NotNull List<@NotNull Tag> tags;

    public ListTag(@NotNull List<? extends @NotNull Tag> tags) {
        this.tags = new ArrayList<@NotNull Tag>(tags.size());
        Class<?> determinedType = null;
        for (var t : tags) {
            if (determinedType == null) {
               determinedType = t.getClass();
            } else if (!determinedType.isInstance(t)) {
                if (tags.get(0) instanceof NumericTag && t instanceof NumericTag && ((NumericTag) tags.get(0)).canHoldDataOfTag((NumericTag) t)) {
                    t = ((NumericTag) tags.get(0)).convert((NumericTag) t);
                } else {
                    // Heterogeneous list, just copy or wrap&copy the original contents
                    this.tags.clear();
                    for (var tag : tags) {
                        if (tag instanceof CompoundTag) {
                            this.tags.add(tag);
                            continue;
                        }

                        this.tags.add(CompoundTag.wrapper(tag));
                    }
                    break;
                }
            }
            this.tags.add(t);
        }
    }

    public @NotNull List<@NotNull Tag> value() {
        return List.copyOf(tags); // keep this class immutable
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull ListTag with(@NotNull Tag tag) {
        var clone = new ArrayList<>(tags);
        clone.add(tag);
        return new ListTag(clone);
    }

    @Contract(value = "_,_ -> new", pure = true)
    public @NotNull ListTag withAt(int index, @NotNull Tag tag) {
        var clone = new ArrayList<>(tags);
        clone.set(index, tag);
        return new ListTag(clone);
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull ListTag without(@NotNull Tag tag) {
        var clone = new ArrayList<>(tags);
        clone.remove(tag);
        return new ListTag(clone);
    }

    @Contract(value = "_ -> new", pure = true)
    public @NotNull ListTag without(int index) {
        var clone = new ArrayList<>(tags);
        clone.remove(index);
        return new ListTag(clone);
    }

    public @NotNull Tag get(int index) {
        return tags.get(index);
    }

    @Override
    public @NotNull Tag getAsTag(int index) {
        return get(index);
    }

    public int size() {
        return tags.size();
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    @Override
    public @NotNull Iterator<@NotNull Tag> iterator() {
        return new Iterator<>() {
            private int cursor;

            @Override
            public boolean hasNext() {
                return cursor != tags.size();
            }

            @Override
            public @NotNull Tag next() {
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

    public @NotNull Stream<@NotNull Tag> stream() {
        return tags.stream();
    }
}
