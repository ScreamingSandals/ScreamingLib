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

package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.inventory.Book;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdventureBook extends BasicWrapper<Book> implements org.screamingsandals.lib.spectator.Book {
    public AdventureBook(Book wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public Component title() {
        return AdventureBackend.wrapComponent(wrappedObject.title());
    }

    @Override
    @NotNull
    public Component author() {
        return AdventureBackend.wrapComponent(wrappedObject.author());
    }

    @Override
    @Unmodifiable
    @NotNull
    public List<Component> pages() {
        return wrappedObject.pages().stream().map(AdventureBackend::wrapComponent).collect(Collectors.toUnmodifiableList());
    }

    public static class AdventureBookBuilder implements org.screamingsandals.lib.spectator.Book.Builder {
        private Component title = Component.empty();
        private Component author = Component.empty();
        private List<Component> pages = List.of();

        @Override
        @NotNull
        public Builder title(@NotNull Component title) {
            this.title = title;
            return this;
        }

        @Override
        @NotNull
        public Builder author(@NotNull Component author) {
            this.author = author;
            return this;
        }

        @Override
        @NotNull
        public Builder pages(@NotNull List<Component> pages) {
            this.pages = pages;
            return this;
        }

        @Override
        @NotNull
        public Builder pages(@NotNull Component... pages) {
            this.pages = Arrays.asList(pages);
            return this;
        }

        @Override
        @NotNull
        public org.screamingsandals.lib.spectator.Book build() {
            return new AdventureBook(Book.book(
                    this.title.as(net.kyori.adventure.text.Component.class),
                    this.author.as(net.kyori.adventure.text.Component.class),
                    this.pages.stream().map(component -> component.as(net.kyori.adventure.text.Component.class)).collect(Collectors.toUnmodifiableList())
            ));
        }
    }
}
