package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.TitleUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;

@Data
public final class TitleWrapper implements Wrapper {
    private final Title title;

    @NotNull
    public Title asTitle() {
        return title;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(title)) {
            return (T) title;
        }

        var generalPackageSplit = Arrays.asList(type.getPackageName().split("\\."));
        generalPackageSplit.remove(generalPackageSplit.size() - 1);
        var generalPackage = String.join(".", generalPackageSplit);

        var component = Reflect.getClassSafe(generalPackage + ".text.Component");
        var gsonSerializer = Reflect.getClassSafe(generalPackage + ".text.serializer.gson.GsonComponentSerializer");
        var times = Reflect.getClassSafe(type.getName() + "$Times");
        if (component == null || gsonSerializer == null || times == null) {
            throw new UnsupportedOperationException("Not supported! The target adventure needs to have gson serializer!");
        }

        return (T) TitleUtils.titleToPlatform(title, type, component, Reflect.fastInvoke(gsonSerializer, "gson"), times);
    }
}
