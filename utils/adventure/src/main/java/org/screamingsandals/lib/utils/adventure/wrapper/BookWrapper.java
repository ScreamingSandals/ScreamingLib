package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.inventory.Book;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.BookUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;

@Data
public final class BookWrapper implements Wrapper {
    private final Book book;

    @NotNull
    public Book asBook() {
        return book;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(book)) {
            return (T) book;
        }

        var generalPackageSplit = Arrays.asList(type.getPackageName().split("\\."));
        generalPackageSplit.remove(generalPackageSplit.size() - 1);
        var generalPackage = String.join(".", generalPackageSplit);

        var component = Reflect.getClassSafe(generalPackage + ".text.Component");
        var gsonSerializer = Reflect.getClassSafe(generalPackage + ".text.serializer.gson.GsonComponentSerializer");
        if (component == null || gsonSerializer == null) {
            throw new UnsupportedOperationException("Not supported! The target adventure needs to have gson serializer!");
        }

        return (T) BookUtils.bookToPlatform(book, type, component, Reflect.fastInvoke(gsonSerializer, "gson"));
    }
}
