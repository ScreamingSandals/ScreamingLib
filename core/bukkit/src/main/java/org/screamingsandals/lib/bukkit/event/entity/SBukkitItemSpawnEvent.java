package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.ItemSpawnEvent;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.entity.SItemSpawnEvent;

public class SBukkitItemSpawnEvent extends SBukkitEntitySpawnEvent implements SItemSpawnEvent {
    public SBukkitItemSpawnEvent(ItemSpawnEvent event) {
        super(event);
    }

    @Override
    public EntityItem getEntity() {
        return (EntityItem) super.getEntity();
    }
}
