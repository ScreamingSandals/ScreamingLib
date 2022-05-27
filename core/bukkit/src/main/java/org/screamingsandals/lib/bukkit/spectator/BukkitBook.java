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

package org.screamingsandals.lib.bukkit.spectator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.Preconditions;

import java.util.Arrays;
import java.util.List;

@Data
@Accessors(fluent = true)
public class BukkitBook implements Book {
    private final Component title;
    private final Component author;
    private final List<Component> pages;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(this)) {
            return (T) this;
        }
        throw new UnsupportedOperationException("Bukkit doesn't have any class for books");
    }

    @Override
    public Object raw() {
        return null;
    }

    @Override
    @NotNull
    public Book withTitle(@NotNull Component title) {
        return new BukkitBook(title, author, pages);
    }

    @Override
    @NotNull
    public Book withAuthor(@NotNull Component author) {
        return new BukkitBook(title, author, pages);
    }

    @Override
    @NotNull
    public Book withPages(@NotNull List<Component> pages) {
        return new BukkitBook(title, author, pages);
    }

    @Override
    @NotNull
    public Book withPages(@NotNull Component... pages) {
        return new BukkitBook(title, author, Arrays.asList(pages));
    }

    @Override
    @NotNull
    public Book.Builder toBuilder() {
        return new BukkitBookBuilder(title, author, pages);
    }

    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BukkitBookBuilder implements Book.Builder {
        private Component title;
        private Component author;
        private List<Component> pages;

        @Override
        @NotNull
        @Tolerate
        public Builder pages(@NotNull Component... pages) {
            this.pages = Arrays.asList(pages);
            return this;
        }

        @Override
        @NotNull
        public Book build() {
            Preconditions.checkNotNull(title);
            Preconditions.checkNotNull(author);
            Preconditions.checkNotNull(pages);
            return new BukkitBook(title, author, pages);
        }
    }
}