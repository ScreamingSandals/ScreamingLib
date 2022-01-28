package org.screamingsandals.lib.minestom.attribute;

import net.minestom.server.attribute.Attribute;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class MinestomAttributeTypeHolder extends BasicWrapper<Attribute> implements AttributeTypeHolder {
    protected MinestomAttributeTypeHolder(Attribute wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.key();
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
