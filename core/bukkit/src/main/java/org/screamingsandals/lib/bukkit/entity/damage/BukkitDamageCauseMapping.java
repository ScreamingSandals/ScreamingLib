package org.screamingsandals.lib.bukkit.entity.damage;

import org.bukkit.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.entity.damage.DamageCauseMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitDamageCauseMapping extends DamageCauseMapping {
    public BukkitDamageCauseMapping() {
        damageCauseConverter
                .registerP2W(EntityDamageEvent.DamageCause.class, entityType -> new DamageCauseHolder(entityType.name()))
                .registerW2P(EntityDamageEvent.DamageCause.class, entityTypeHolder -> EntityDamageEvent.DamageCause.valueOf(entityTypeHolder.getPlatformName()));

        Arrays.stream(EntityDamageEvent.DamageCause.values()).forEach(damageCause -> mapping.put(NamespacedMappingKey.of(damageCause.name()), new DamageCauseHolder(damageCause.name())));
    }
}
