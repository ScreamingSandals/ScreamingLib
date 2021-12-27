package org.screamingsandals.lib.bukkit.entity.damage;

import org.bukkit.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitDamageCauseHolder extends BasicWrapper<EntityDamageEvent.DamageCause> implements DamageCauseHolder {

    public BukkitDamageCauseHolder(EntityDamageEvent.DamageCause wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object damageCause) {
        if (damageCause instanceof EntityDamageEvent.DamageCause || damageCause instanceof DamageCauseHolder) {
            return equals(damageCause);
        }
        return equals(DamageCauseHolder.ofOptional(damageCause).orElse(null));
    }

    @Override
    public boolean is(Object... damageCauses) {
        return Arrays.stream(damageCauses).anyMatch(this::is);
    }
}
