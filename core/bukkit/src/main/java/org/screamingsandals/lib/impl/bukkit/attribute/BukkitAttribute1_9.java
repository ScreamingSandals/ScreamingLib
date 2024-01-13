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

import org.bukkit.attribute.AttributeInstance;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.attribute.*;
import org.screamingsandals.lib.impl.attribute.Attributes;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.AttributeAccessor;
import org.screamingsandals.lib.impl.nms.accessors.AttributeInstanceAccessor;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BukkitAttribute1_9 extends BasicWrapper<AttributeInstance> implements Attribute {

    protected BukkitAttribute1_9(@NotNull AttributeInstance wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull AttributeType getAttributeType() {
        return AttributeType.of(wrappedObject.getAttribute());
    }

    @Override
    public double getBaseValue() {
        return wrappedObject.getBaseValue();
    }

    @Override
    public void setBaseValue(double baseValue) {
        wrappedObject.setBaseValue(baseValue);
    }

    @Override
    public double getDefaultValue() {
        if (BukkitFeature.ATTRIBUTE_DEFAULT_VALUE.isSupported()) {
            return wrappedObject.getDefaultValue();
        } else {
            // 1.9-1.11
            return (double) Reflect.fastInvokeResulted(ClassStorage.getHandleOfItemStack(wrappedObject), AttributeInstanceAccessor.getMethodGetAttribute1()).fastInvoke(AttributeAccessor.getMethodB1());
        }
    }

    @Override
    public double getValue() {
        return wrappedObject.getValue();
    }

    @Override
    public @NotNull List<@NotNull AttributeModifier> getModifiers() {
        return wrappedObject.getModifiers().stream()
                .map(Attributes::wrapAttributeModifier)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void addModifier(@NotNull AttributeModifier modifier) {
        wrappedObject.addModifier(modifier.as(org.bukkit.attribute.AttributeModifier.class));
    }

    @Override
    public void removeModifier(@NotNull AttributeModifier modifier) {
        wrappedObject.removeModifier(modifier.as(org.bukkit.attribute.AttributeModifier.class));
    }
}
