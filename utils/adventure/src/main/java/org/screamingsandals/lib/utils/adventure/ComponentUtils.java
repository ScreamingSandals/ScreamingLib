package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.screamingsandals.lib.utils.reflect.ClassMethod;
import org.screamingsandals.lib.utils.reflect.Reflect;

// this method of transforming adventure components is from AdventurePlatform
@UtilityClass
public class ComponentUtils {
    public final Class<?> NATIVE_COMPONENT_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.text.Component");
    public final Class<?> NATIVE_GSON_COMPONENT_SERIALIZER_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.text.serializer.gson.GsonComponentSerializer");
    public final ClassMethod NATIVE_GSON_COMPONENT_SERIALIZER_GETTER =
            Reflect.getMethod(NATIVE_GSON_COMPONENT_SERIALIZER_CLASS, "gson");

    public Object componentToPlatform(Component component) {
        if (NATIVE_COMPONENT_CLASS.isInstance(component)) {
            return component;
        }
        return componentToPlatform(component, NATIVE_GSON_COMPONENT_SERIALIZER_GETTER.invokeStatic());
    }

    public Object componentToPlatform(Component component, Object nativeGsonSerializer) {
        var result = GsonComponentSerializer.gson().serialize(component);
        return Reflect.getMethod(nativeGsonSerializer, "deserialize", String.class).invoke(result);
    }

    public Component componentFromPlatform(Object platformComponent) {
        if (platformComponent instanceof Component) {
            return (Component) platformComponent;
        }
        return componentFromPlatform(platformComponent, NATIVE_GSON_COMPONENT_SERIALIZER_GETTER.invokeStatic());
    }

    public Component componentFromPlatform(Object platformComponent, Object nativeGsonSerializer) {
        var result = Reflect
                .getMethod(nativeGsonSerializer, "serialize", String.class)
                .invokeResulted(platformComponent)
                .as(String.class);
        return GsonComponentSerializer.gson().deserialize(result);
    }
}
