/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.attribute;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.attribute.Attribute;
import org.screamingsandals.lib.attribute.AttributeModifier;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.impl.nms.accessors.AttributeAccessor;
import org.screamingsandals.lib.impl.nms.accessors.AttributeInstanceAccessor;
import org.screamingsandals.lib.impl.nms.accessors.AttributeModifierAccessor;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class BukkitAttribute1_8 extends BasicWrapper<Object> implements Attribute {

    protected BukkitAttribute1_8(@NotNull Object wrappedObject) {
        super(wrappedObject);
        if (AttributeInstanceAccessor.TYPE.get() == null || !AttributeInstanceAccessor.TYPE.get().isInstance(wrappedObject)) {
            throw new IllegalArgumentException("Object must be an instance of AttributeInstance!");
        }
    }

    @Override
    public @NotNull AttributeType getAttributeType() {
        return new BukkitAttributeType1_8(Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.METHOD_GET_ATTRIBUTE.get()));
    }

    @Override
    public double getBaseValue() {
        return (double) Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.METHOD_GET_BASE_VALUE.get());
    }

    @Override
    public void setBaseValue(double baseValue) {
        Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.METHOD_SET_BASE_VALUE.get(), baseValue);
    }

    @Override
    public double getDefaultValue() {
        return (double) Reflect.fastInvokeResulted(wrappedObject, AttributeInstanceAccessor.METHOD_GET_ATTRIBUTE.get()).fastInvoke(AttributeAccessor.METHOD_GET_DEFAULT_VALUE.get());
    }

    @Override
    public double getValue() {
        return (double) Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.METHOD_GET_VALUE.get());
    }

    @Override
    public @NotNull List<@NotNull AttributeModifier> getModifiers() {
        var collection = (Collection<?>) Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.METHOD_GET_MODIFIERS_1.get());
        return collection.stream()
                .map(BukkitAttribute1_8::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void addModifier(@NotNull AttributeModifier modifier) {
        Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.METHOD_ADD_MODIFIER.get(), convert(modifier));
    }

    @Override
    public void removeModifier(@NotNull AttributeModifier modifier) {
        Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.METHOD_REMOVE_MODIFIER.get(), convert(modifier));
    }

    public static @NotNull Object convert(@NotNull AttributeModifier sandals) {
        return Objects.requireNonNull(Reflect.construct(
                AttributeModifierAccessor.CONSTRUCTOR_0.get(),
                sandals.getUuid(),
                sandals.getName(),
                sandals.getAmount(),
                sandals.getOperation().ordinal()
        ));
    }

    public static @NotNull AttributeModifier convert(@NotNull Object nms) {
        return new AttributeModifier(
                (UUID) Reflect.fastInvoke(nms, AttributeModifierAccessor.METHOD_GET_ID.get()),
                (String) Reflect.fastInvoke(nms, AttributeModifierAccessor.METHOD_GET_NAME.get()),
                (double) Reflect.fastInvoke(nms, AttributeModifierAccessor.METHOD_GET_AMOUNT.get()),
                AttributeModifier.Operation.values()[(int) Reflect.fastInvoke(nms, AttributeModifierAccessor.METHOD_FUNC_111169_C.get())]
        );
    }
}
