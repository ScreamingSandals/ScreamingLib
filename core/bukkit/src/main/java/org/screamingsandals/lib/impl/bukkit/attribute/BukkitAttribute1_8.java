/*
 * Copyright 2023 ScreamingSandals
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
import org.screamingsandals.lib.nms.accessors.AttributeInstanceAccessor;
import org.screamingsandals.lib.nms.accessors.AttributeModifierAccessor;
import org.screamingsandals.lib.nms.accessors.IAttributeAccessor;
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
        if (AttributeInstanceAccessor.getType() == null || !AttributeInstanceAccessor.getType().isInstance(wrappedObject)) {
            throw new IllegalArgumentException("Object must be an instance of AttributeInstance!");
        }
    }

    @Override
    public @NotNull AttributeType getAttributeType() {
        return new BukkitAttributeType1_8(Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.getMethodGetAttribute1()));
    }

    @Override
    public double getBaseValue() {
        return (double) Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.getMethodB1());
    }

    @Override
    public void setBaseValue(double baseValue) {
        Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.getMethodSetValue1(), baseValue);
    }

    @Override
    public double getDefaultValue() {
        return (double) Reflect.fastInvokeResulted(wrappedObject, AttributeInstanceAccessor.getMethodGetAttribute1()).fastInvoke(IAttributeAccessor.getMethodB1());
    }

    @Override
    public double getValue() {
        return (double) Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.getMethodGetValue1());
    }

    @Override
    public @NotNull List<@NotNull AttributeModifier> getModifiers() {
        var collection = (Collection<?>) Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.getMethodC1());
        return collection.stream()
                .map(BukkitAttribute1_8::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void addModifier(@NotNull AttributeModifier modifier) {
        Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.getMethodB2(), convert(modifier));
    }

    @Override
    public void removeModifier(@NotNull AttributeModifier modifier) {
        Reflect.fastInvoke(wrappedObject, AttributeInstanceAccessor.getMethodC2(), convert(modifier));
    }

    public static @NotNull Object convert(@NotNull AttributeModifier sandals) {
        return Objects.requireNonNull(Reflect.construct(
                AttributeModifierAccessor.getConstructor0(),
                sandals.getUuid(),
                sandals.getName(),
                sandals.getAmount(),
                sandals.getOperation().ordinal()
        ));
    }

    public static @NotNull AttributeModifier convert(@NotNull Object nms) {
        return new AttributeModifier(
                (UUID) Reflect.fastInvoke(nms, AttributeModifierAccessor.getMethodD1()),
                (String) Reflect.fastInvoke(nms, AttributeModifierAccessor.getMethodC1()),
                (double) Reflect.fastInvoke(nms, AttributeModifierAccessor.getMethodB1()),
                AttributeModifier.Operation.values()[(int) Reflect.fastInvoke(nms, AttributeModifierAccessor.getMethodA1())]
        );
    }
}
