package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SArrowBodyCountChangeEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitArrowBodyCountChangeEvent implements SArrowBodyCountChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final ArrowBodyCountChangeEvent event;

    // Internal cache
    private EntityBasic entity;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public boolean isReset() {
        return event.isReset();
    }

    @Override
    public int getOldAmount() {
        return event.getOldAmount();
    }

    @Override
    public int getNewAmount() {
        return event.getNewAmount();
    }

    @Override
    public void setNewAmount(int newAmount) {
        event.setNewAmount(newAmount);
    }
}
