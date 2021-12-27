package org.screamingsandals.lib.bukkit.attribute;

import org.bukkit.attribute.Attribute;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitAttributeTypeHolder extends BasicWrapper<Attribute> implements AttributeTypeHolder {

    protected BukkitAttributeTypeHolder(Attribute wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof Attribute || object instanceof AttributeTypeHolder) {
            return equals(object);
        }
        return equals(AttributeTypeHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
