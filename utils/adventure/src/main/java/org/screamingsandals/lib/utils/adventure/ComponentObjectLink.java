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
                                classMethod.invokeInstanceResulted(objectHavingMethod).as(Component.class),
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
