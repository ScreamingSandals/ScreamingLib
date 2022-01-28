package org.screamingsandals.lib.minestom.item.meta;

import net.minestom.server.potion.PotionType;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class MinestomPotionHolder extends BasicWrapper<PotionType> implements PotionHolder {
    protected MinestomPotionHolder(PotionType wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof PotionType || object instanceof PotionHolder) {
            return equals(object);
        }
        return equals(PotionHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
