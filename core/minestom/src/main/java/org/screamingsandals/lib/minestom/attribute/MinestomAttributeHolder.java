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

package org.screamingsandals.lib.minestom.attribute;

import net.minestom.server.attribute.AttributeInstance;
import net.minestom.server.attribute.AttributeModifier;
import org.screamingsandals.lib.attribute.*;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MinestomAttributeHolder extends BasicWrapper<AttributeInstance> implements AttributeHolder  {
    protected MinestomAttributeHolder(AttributeInstance wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public AttributeTypeHolder getAttributeType() {
        return AttributeTypeMapping.resolve(wrappedObject.getAttribute()).orElseThrow();
    }

    @Override
    public double getBaseValue() {
        return wrappedObject.getBaseValue();
    }

    @Override
    public void setBaseValue(double baseValue) {
        wrappedObject.setBaseValue((float) baseValue);
    }

    @Override
    public double getDefaultValue() {
        return wrappedObject.getAttribute().defaultValue();
    }

    @Override
    public double getValue() {
        return wrappedObject.getValue();
    }

    @Override
    public List<AttributeModifierHolder> getModifiers() {
        return wrappedObject.getModifiers().stream()
                .map(AttributeMapping::wrapAttributeModifier)
                .map(Optional::orElseThrow)
                .collect(Collectors.toList());
    }

    @Override
    public void addModifier(AttributeModifierHolder modifier) {
        wrappedObject.addModifier(modifier.as(AttributeModifier.class));
    }

    @Override
    public void removeModifier(AttributeModifierHolder modifier) {
        wrappedObject.removeModifier(modifier.as(AttributeModifier.class));
    }
}
