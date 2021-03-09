package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Collection;

@UtilityClass
public class BookUtils {
    public final Class<?> NATIVE_BOOK_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.inventory.Book");

    public Object bookToPlatform(Book book) {
        if (NATIVE_BOOK_CLASS.isInstance(book)) {
            return book;
        }
        return bookToPlatform(book, NATIVE_BOOK_CLASS, ComponentUtils.NATIVE_COMPONENT_CLASS);
    }

    public Object bookToPlatform(Book book, Class<?> bookClass, Class<?> componentClass) {
        return Reflect
                .getMethod(bookClass, "book", componentClass, componentClass, Collection.class)
                .invokeStatic(
                        ComponentUtils.componentToPlatform(book.title(), componentClass),
                        ComponentUtils.componentToPlatform(book.author(), componentClass),
                        book.pages()
                                .stream()
                                .map(component -> ComponentUtils.componentToPlatform(component, componentClass))
                                .toArray()
                );
    }

    @SuppressWarnings("unchecked")
    public Book bookFromPlatform(Object platformObject) {
        if (platformObject instanceof Book) {
            return (Book) platformObject;
        }
        return Book.book(
                ComponentUtils.componentFromPlatform(Reflect.fastInvoke(platformObject, "title")),
                ComponentUtils.componentFromPlatform(Reflect.fastInvoke(platformObject, "author")),
                (Component[]) // IDEA is retarded or idk, so here is casting
                    Reflect
                        .fastInvokeResulted(platformObject, "pages")
                        .as(Collection.class)
                        .stream()
                        .map(ComponentUtils::componentFromPlatform)
                        .toArray(Component[]::new)
        );
    }

}
