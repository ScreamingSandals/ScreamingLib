package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityRegainHealthEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityRegainHealthEvent implements SEntityRegainHealthEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityRegainHealthEvent event;

    // Internal cache
    private EntityBasic entity;
    private RegainReason reason;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public RegainReason getRegainReason() {
        if (reason == null) {
            reason = RegainReason.valueOf(event.getRegainReason().name());
        }
        return reason;
    }

    @Override
    public double getAmount() {
        return event.getAmount();
    }

    @Override
    public void setAmount(double amount) {
        event.setAmount(amount);
    }
}
