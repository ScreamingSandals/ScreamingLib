/*
 * Copyright 2024 ScreamingSandals
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

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListLinkedToList<L, O> extends CollectionLinkedToCollection<L, O> implements List<L> {
    public ListLinkedToList(List<O> original, Function<L, O> linkToOriginal, Function<O, L> originalToLink) {
        super(original, linkToOriginal, originalToLink);
    }

    private List<O> original() {
        return (List<O>) original;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends L> c) {
        return original().addAll(index, c.stream().map(linkToOriginal).collect(Collectors.toList()));
    }

    @Override
    public L get(int index) {
        return originalToLink.apply(original().get(index));
    }

    @Override
    public L set(int index, L element) {
        return originalToLink.apply(original().set(index, linkToOriginal.apply(element)));
    }

    @Override
    public void add(int index, L element) {
        original().add(index, linkToOriginal.apply(element));
    }

    @Override
    public L remove(int index) {
        return originalToLink.apply(original().remove(index));
    }

    @Override
    public int indexOf(Object o) {
        return original().indexOf(linkToOriginal.apply((L) o));
    }

    @Override
    public int lastIndexOf(Object o) {
        return original().lastIndexOf(linkToOriginal.apply((L) o));
    }

    @Override
    public @NotNull ListIterator<L> listIterator() {
        var realIterator = original().listIterator();

        return new ListIterator<>() {
            @Override
            public boolean hasNext() {
                return realIterator.hasNext();
            }

            @Override
            public L next() {
                return originalToLink.apply(realIterator.next());
            }

            @Override
            public boolean hasPrevious() {
                return realIterator.hasPrevious();
            }

            @Override
            public L previous() {
                return originalToLink.apply(realIterator.previous());
            }

            @Override
            public int nextIndex() {
                return realIterator.nextIndex();
            }

            @Override
            public int previousIndex() {
                return realIterator.previousIndex();
            }

            @Override
            public void remove() {
                realIterator.remove();
            }

            @Override
            public void set(L l) {
                realIterator.set(linkToOriginal.apply(l));
            }

            @Override
            public void add(L l) {
                realIterator.add(linkToOriginal.apply(l));
            }
        };
    }

    @Override
    public @NotNull ListIterator<L> listIterator(int index) {
        var realIterator = original().listIterator(index);

        return new ListIterator<>() {
            @Override
            public boolean hasNext() {
                return realIterator.hasNext();
            }

            @Override
            public L next() {
                return originalToLink.apply(realIterator.next());
            }

            @Override
            public boolean hasPrevious() {
                return realIterator.hasPrevious();
            }

            @Override
            public L previous() {
                return originalToLink.apply(realIterator.previous());
            }

            @Override
            public int nextIndex() {
                return realIterator.nextIndex();
            }

            @Override
            public int previousIndex() {
                return realIterator.previousIndex();
            }

            @Override
            public void remove() {
                realIterator.remove();
            }

            @Override
            public void set(L l) {
                realIterator.set(linkToOriginal.apply(l));
            }

            @Override
            public void add(L l) {
                realIterator.add(linkToOriginal.apply(l));
            }
        };
    }

    @Override
    public @NotNull List<L> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
}
