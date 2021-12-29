package org.screamingsandals.lib.minestom.entity.damage;

import net.minestom.server.entity.damage.DamageType;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class MinestomDamageCauseHolder extends BasicWrapper<DamageType> implements DamageCauseHolder {
    protected MinestomDamageCauseHolder(DamageType wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.getIdentifier();
    }

    @Override
    public boolean is(Object damageCause) {
        if (damageCause instanceof DamageType || damageCause instanceof DamageCauseHolder) {
            return equals(damageCause);
        }
        return equals(DamageCauseHolder.ofOptional(damageCause).orElse(null));
    }

    @Override
    public boolean is(Object... damageCauses) {
        return Arrays.stream(damageCauses).anyMatch(this::is);
    }
}
