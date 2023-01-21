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

package org.screamingsandals.lib.bukkit.attribute;

import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.attribute.*;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BukkitAttributeHolder extends BasicWrapper<AttributeInstance> implements AttributeHolder {

    protected BukkitAttributeHolder(@NotNull AttributeInstance wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull AttributeTypeHolder getAttributeType() {
        return new BukkitAttributeTypeHolder(wrappedObject.getAttribute());
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
        return wrappedObject.getDefaultValue();
    }

    @Override
    public double getValue() {
        return wrappedObject.getValue();
    }

    @Override
    public @NotNull List<@NotNull AttributeModifierHolder> getModifiers() {
        return wrappedObject.getModifiers().stream()
                .map(AttributeMapping::wrapAttributeModifier)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void addModifier(@NotNull AttributeModifierHolder modifier) {
        wrappedObject.addModifier(modifier.as(AttributeModifier.class));
    }

    @Override
    public void removeModifier(@NotNull AttributeModifierHolder modifier) {
        wrappedObject.removeModifier(modifier.as(AttributeModifier.class));
    }
}
