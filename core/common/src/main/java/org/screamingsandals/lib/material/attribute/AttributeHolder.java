package org.screamingsandals.lib.material.attribute;

import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;

public interface AttributeHolder extends Wrapper{
    AttributeTypeHolder getAttributeType();

    double getBaseValue();

    void setBaseValue(double baseValue);

    double getDefaultValue();

    double getValue();

    List<AttributeModifierHolder> getModifiers();

    void addModifier(AttributeModifierHolder modifier);

    void removeModifier(AttributeModifierHolder modifier);
}
