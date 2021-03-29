package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.ComponentUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

@Data
public final class ComponentWrapper implements ComponentLike, Wrapper {
    private final Component component;

    @Override
    @NonNull
    public Component asComponent() {
        return component;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) AdventureHelper.toLegacy(component);
        } else if (type.isInstance(component)) {
            return (T) component;
        }

        var gsonSerializer = Reflect.getClassSafe(type.getPackageName() + ".serializer.gson.GsonComponentSerializer");
        if (gsonSerializer == null) {
            throw new UnsupportedOperationException("Not supported! The target adventure needs to have gson serializer!");
        }

        return (T) ComponentUtils.componentToPlatform(component, Reflect.fastInvoke(gsonSerializer, "gson"));
    }
}
