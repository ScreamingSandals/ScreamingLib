package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityItem;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityPickupItemEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityPickupItemEvent implements SEntityPickupItemEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityPickupItemEvent event;

    // Internal cache
    private EntityBasic entity;
    private EntityItem item;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public EntityItem getItem() {
        if (item == null) {
            item = new BukkitEntityItem(event.getItem());
        }
        return item;
    }

    @Override
    public int getRemaining() {
        return event.getRemaining();
    }
}
