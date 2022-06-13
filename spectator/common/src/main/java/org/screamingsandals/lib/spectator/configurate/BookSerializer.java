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

package org.screamingsandals.lib.spectator.configurate;

import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.Preconditions;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class BookSerializer implements TypeSerializer<Book> {
    public static final BookSerializer INSTANCE = new BookSerializer();

    private static final String TITLE_KEY = "title";
    private static final String AUTHOR_KEY = "author";
    private static final String PAGES_KEY = "pages";
    private static final TypeToken<List<Component>> COMPONENT_LIST_TYPE_TOKEN = new TypeToken<List<Component>>() {};

    @Override
    public Book deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            var title = node.node(TITLE_KEY).get(Component.class);
            var author = node.node(AUTHOR_KEY).get(Component.class);
            var pages = node.node(PAGES_KEY).get(COMPONENT_LIST_TYPE_TOKEN);
            Preconditions.checkNotNull(title);
            Preconditions.checkNotNull(author);
            Preconditions.checkNotNull(pages);
            return Book.builder()
                    .title(title)
                    .author(author)
                    .pages(pages)
                    .build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(Type type, @Nullable Book obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(TITLE_KEY).set(Component.class, obj.title());
        node.node(AUTHOR_KEY).set(Component.class, obj.author());
        node.node(PAGES_KEY).set(COMPONENT_LIST_TYPE_TOKEN, obj.pages());
    }
}
