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

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.ObjectLink;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ComponentObjectLink extends ObjectLink<Component> {
    private final Object objectHavingMethod;
    private final String getterName;
    private final String setterName;
    private final Supplier<String> getter;
    private final Consumer<String> setter;

    protected ComponentObjectLink(Object objectHavingMethod, String getterName, Supplier<String> getter, String setterName, Consumer<String> setter) {
        super(null, null);
        this.objectHavingMethod = objectHavingMethod;
        this.getterName = getterName;
        this.setterName = setterName;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public Component get() {
        return AdventureUtils
                .get(objectHavingMethod, getterName)
                .ifPresentOrElseGet(classMethod ->
                                classMethod.invokeInstanceResulted(objectHavingMethod).asOptional(Component.class).orElse(null),
                        () -> AdventureHelper.toComponentNullableResult(getter.get()));
    }

    @Override
    public void set(Component component) {
        AdventureUtils
                .get(objectHavingMethod, setterName, Component.class)
                .ifPresentOrElse(classMethod -> classMethod.invokeInstance(objectHavingMethod, component),
                        () -> setter.accept(AdventureHelper.toLegacyNullableResult(component)));
    }

    public static ComponentObjectLink of(Object objectHavingMethod, String getterName, Supplier<String> fallbackGetter, String setterName, Consumer<String> fallbackSetter) {
        return new ComponentObjectLink(objectHavingMethod, getterName, fallbackGetter, setterName, fallbackSetter);
    }

    public static ComponentObjectLink of(Object objectHavingMethod, String name, Supplier<String> fallbackGetter, Consumer<String> fallbackSetter) {
        return of(objectHavingMethod, name, fallbackGetter, name, fallbackSetter);
    }

    public static Component processGetter(Object objectHavingMethod, String getterName, Supplier<String> fallbackGetter) {
        return of(objectHavingMethod, getterName, fallbackGetter, null).get();
    }

    public static void processSetter(Object objectHavingMethod, String setterName, Consumer<String> fallbackSetter, Component component) {
        of(objectHavingMethod, setterName, null, fallbackSetter).set(component);
    }
}
