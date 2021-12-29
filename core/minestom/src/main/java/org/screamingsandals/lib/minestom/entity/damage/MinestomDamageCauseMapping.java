package org.screamingsandals.lib.minestom.entity.damage;

import net.minestom.server.entity.damage.DamageType;
import org.screamingsandals.lib.entity.damage.DamageCauseMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.stream.Stream;

@Service
public class MinestomDamageCauseMapping extends DamageCauseMapping {
    public MinestomDamageCauseMapping() {
        damageCauseConverter
                .registerP2W(DamageType.class, MinestomDamageCauseHolder::new)
                .registerW2P(DamageType.class, damageCauseHolder -> new DamageType(damageCauseHolder.platformName()));

        Stream.of(
                DamageType.GRAVITY,
                DamageType.ON_FIRE,
                DamageType.VOID
        ).forEach(damageCause -> {
            var holder = new MinestomDamageCauseHolder(damageCause);
            mapping.put(NamespacedMappingKey.of(damageCause.getIdentifier()), holder);
            values.add(holder);
        });
        Stream.of(
                new MinestomSpecialDamageCauseHolder("entity_source"), // EntityDamage class
                new MinestomSpecialDamageCauseHolder("projectile_source") // EntityProjectileDamage class
        ).forEach(holder -> {
            mapping.put(NamespacedMappingKey.of(holder.platformName()), holder);
            values.add(holder);
        });
    }
}
