package org.screamingsandals.lib.utils;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CollectionLinkedToCollection<L, O> implements Collection<L> {
    private final Collection<O> original;
    private final Function<L, O> linkToOriginal;
    private final Function<O, L> originalToLink;

    @Override
    public int size() {
        return original.size();
    }

    @Override
    public boolean isEmpty() {
        return original.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        return original.contains(linkToOriginal.apply((L) o));
    }

    @NotNull
    @Override
    public Iterator<L> iterator() {
        var realIterator = original.iterator();

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return realIterator.hasNext();
            }

            @Override
            public L next() {
                return originalToLink.apply(realIterator.next());
            }

            @Override
            public void remove() {
                realIterator.remove();
            }
        };
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return original.stream().map(originalToLink).toArray();
    }

    @SuppressWarnings({"unchecked", "SuspiciousToArrayCall"})
    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return original.stream().map(originalToLink).toArray(value -> (T[]) Array.newInstance(a.getClass().getComponentType(), value));
    }

    @Override
    public boolean add(L e) {
        var a = linkToOriginal.apply(e);
        if (a != null) {
            return original.add(a);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        var a = linkToOriginal.apply((L) o);
        if (a != null) {
            return original.remove(a);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return original.containsAll(c.stream().map(o -> linkToOriginal.apply((L) o)).collect(Collectors.toList()));
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends L> c) {
        return original.addAll(c.stream().map(linkToOriginal).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return original.removeAll(c.stream().map(o -> linkToOriginal.apply((L) o)).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return original.retainAll(c.stream().map(o -> linkToOriginal.apply((L) o)).collect(Collectors.toList()));
    }

    @Override
    public void clear() {
        original.clear();
    }
}
