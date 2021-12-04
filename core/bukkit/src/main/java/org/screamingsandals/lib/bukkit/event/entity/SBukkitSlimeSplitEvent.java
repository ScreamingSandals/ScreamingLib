package org.screamingsandals.lib.bukkit.event.entity;

import lombok.Data;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SSlimeSplitEvent;

@Data
public class SBukkitSlimeSplitEvent implements SSlimeSplitEvent, BukkitCancellable {
    private final SlimeSplitEvent event;

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
    public int getCount() {
        return event.getCount();
    }

    @Override
    public void setCount(int count) {
        event.setCount(count);
    }
}
