package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.event.entity.SEntityDamageEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityDamageEvent implements SEntityDamageEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityDamageEvent event;

    // Internal cache
    private EntityBasic entity;
    private DamageCauseHolder damageCause;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public DamageCauseHolder getDamageCause() {
        if (damageCause == null) {
            damageCause = DamageCauseHolder.of(event.getCause());
        }
        return damageCause;
    }

    @Override
    public double getDamage() {
        return event.getDamage();
    }

    @Override
    public void setDamage(double damage) {
        event.setDamage(damage);
    }
}
