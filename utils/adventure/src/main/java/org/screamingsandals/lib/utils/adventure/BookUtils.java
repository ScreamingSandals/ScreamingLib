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

package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Collection;

@UtilityClass
public class BookUtils {
    public final Class<?> NATIVE_BOOK_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "inventory", "Book"));

    public Object bookToPlatform(Book book) {
        if (NATIVE_BOOK_CLASS.isInstance(book)) {
            return book;
        }
        return bookToPlatform(book, NATIVE_BOOK_CLASS, ComponentUtils.NATIVE_COMPONENT_CLASS, ComponentUtils.NATIVE_GSON_COMPONENT_SERIALIZER_GETTER.invokeStatic());
    }

    public Object bookToPlatform(Book book, Class<?> bookClass, Class<?> componentClass, Object componentSerializer) {
        return Reflect
                .getMethod(bookClass, "book", componentClass, componentClass, Collection.class)
                .invokeStatic(
                        ComponentUtils.componentToPlatform(book.title(), componentClass),
                        ComponentUtils.componentToPlatform(book.author(), componentClass),
                        book.pages()
                                .stream()
                                .map(component -> ComponentUtils.componentToPlatform(component, componentSerializer))
                                .toArray()
                );
    }

    public Book bookFromPlatform(Object platformObject) {
        if (platformObject instanceof Book) {
            return (Book) platformObject;
        }
        return bookFromPlatform(platformObject, ComponentUtils.NATIVE_GSON_COMPONENT_SERIALIZER_GETTER.invokeStatic(), ComponentUtils.NATIVE_COMPONENT_CLASS);
    }

    @SuppressWarnings("unchecked")
    public Book bookFromPlatform(Object platformObject, Object componentSerializer, Class<?> nativeComponentClass) {
        if (platformObject instanceof Book) {
            return (Book) platformObject;
        }
        return Book.book(
                ComponentUtils.componentFromPlatform(Reflect.fastInvoke(platformObject, "title"), componentSerializer, nativeComponentClass),
                ComponentUtils.componentFromPlatform(Reflect.fastInvoke(platformObject, "author"), componentSerializer, nativeComponentClass),
                (Component[]) // IDEA is retarded or idk, so here is casting
                    Reflect
                        .fastInvokeResulted(platformObject, "pages")
                        .as(Collection.class)
                        .stream()
                        .map(o -> ComponentUtils.componentFromPlatform(o, componentSerializer, nativeComponentClass))
                        .toArray(Component[]::new)
        );
    }

}
