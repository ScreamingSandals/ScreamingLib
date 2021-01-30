package org.screamingsandals.lib.sponge.material.attribute;

import org.screamingsandals.lib.material.attribute.*;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.spongepowered.api.entity.attribute.Attribute;
import org.spongepowered.api.entity.attribute.AttributeModifier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpongeAttributeHolder extends BasicWrapper<Attribute> implements AttributeHolder {
    protected SpongeAttributeHolder(Attribute wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public AttributeTypeHolder getAttributeType() {
        return AttributeTypeMapping.resolve(wrappedObject.getType()).orElseThrow();
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
        return wrappedObject.getType().getDefaultValue();
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
