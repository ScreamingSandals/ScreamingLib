package org.screamingsandals.lib.attribute;

import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;

public interface AttributeHolder extends Wrapper, RawValueHolder {
    AttributeTypeHolder getAttributeType();

    double getBaseValue();

    void setBaseValue(double baseValue);

    double getDefaultValue();

    double getValue();

    List<AttributeModifierHolder> getModifiers();

    void addModifier(AttributeModifierHolder modifier);

    void removeModifier(AttributeModifierHolder modifier);
}
