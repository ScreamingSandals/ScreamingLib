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
        return wrappedObject.getAttribute().getDefaultValue();
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
