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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.screamingsandals.lib.utils.reflect.ClassMethod;
import org.screamingsandals.lib.utils.reflect.Reflect;

// this method of transforming adventure components is from AdventurePlatform
@UtilityClass
public class ComponentUtils {
    public final Class<?> NATIVE_COMPONENT_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "text", "Component"));
    public final Class<?> NATIVE_GSON_COMPONENT_SERIALIZER_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "text", "serializer", "gson", "GsonComponentSerializer"));
    public final ClassMethod NATIVE_GSON_COMPONENT_SERIALIZER_GETTER =
            Reflect.getMethod(NATIVE_GSON_COMPONENT_SERIALIZER_CLASS, "gson");

    public Object componentToPlatform(Component component) {
        if (component == null) {
            return null;
        }
        if (NATIVE_COMPONENT_CLASS.isInstance(component)) {
            return component;
        }
        return componentToPlatform(component, NATIVE_GSON_COMPONENT_SERIALIZER_GETTER.invokeStatic());
    }

    public Object componentToPlatform(Component component, Object nativeGsonSerializer) {
        if (component == null) {
            return null;
        }
        var result = GsonComponentSerializer.gson().serialize(component);
        return Reflect.getMethod(nativeGsonSerializer, "deserialize", String.class).invoke(result);
    }

    public Component componentFromPlatform(Object platformComponent) {
        if (platformComponent == null) {
            return null;
        }
        if (platformComponent instanceof Component) {
            return (Component) platformComponent;
        }
        return componentFromPlatform(platformComponent, NATIVE_GSON_COMPONENT_SERIALIZER_GETTER.invokeStatic(), NATIVE_COMPONENT_CLASS);
    }

    public Component componentFromPlatform(Object platformComponent, Object nativeGsonSerializer, Class<?> nativeComponentClass) {
        if (platformComponent == null) {
            return null;
        }
        var result = Reflect
                .getMethod(nativeGsonSerializer, "serialize", nativeComponentClass)
                .invokeResulted(platformComponent)
                .as(String.class);
        return GsonComponentSerializer.gson().deserialize(result);
    }
}
