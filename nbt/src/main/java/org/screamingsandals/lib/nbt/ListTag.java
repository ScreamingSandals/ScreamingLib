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

    @NotNull
    public List<@NotNull Tag> value() {
        return List.copyOf(tags); // keep this class immutable
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public ListTag with(@NotNull Tag tag) {
        if (!tags.isEmpty()) {
            var firstTag = tags.get(0);
            if (!firstTag.getClass().isInstance(tag)
                    && (!(firstTag instanceof ByteTag) || !(tag instanceof BooleanTag))
                    && (!(firstTag instanceof BooleanTag) || !(tag instanceof ByteTag))
            ) {
                throw new IllegalArgumentException("This is list of " + firstTag.getClass().getSimpleName() + ", got " + tag.getClass().getSimpleName());
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
