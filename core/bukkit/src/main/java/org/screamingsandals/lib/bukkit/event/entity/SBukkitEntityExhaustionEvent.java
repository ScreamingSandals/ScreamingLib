package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityExhaustionEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityExhaustionEvent implements SEntityExhaustionEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityExhaustionEvent event;

    // Internal cache
    private EntityBasic entity;
    private ExhaustionReason exhaustionReason;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public ExhaustionReason getExhaustionReason() {
        if (exhaustionReason == null) {
            exhaustionReason = ExhaustionReason.valueOf(event.getExhaustionReason().name());
        }
        return exhaustionReason;
    }

    @Override
    public float getExhaustion() {
        return event.getExhaustion();
    }

    @Override
    public void setExhaustion(float exhaustion) {
        event.setExhaustion(exhaustion);
    }
}
