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

package org.screamingsandals.lib.impl.utils.collections;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CollectionLinkedToCollection<L, O> implements Collection<L> {
    protected final @NotNull Collection<O> original;
    protected final @NotNull Function<L, O> linkToOriginal;
    protected final @NotNull Function<O, L> originalToLink;

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

    @Override
    public @NotNull Iterator<L> iterator() {
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

    @Override
    public Object @NotNull [] toArray() {
        return original.stream().map(originalToLink).toArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] a) {
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
