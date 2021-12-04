package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.screamingsandals.lib.event.entity.SEntityTargetLivingEntityEvent;

public class SBukkitEntityTargetLivingEntityEvent extends SBukkitEntityTargetEvent implements SEntityTargetLivingEntityEvent {
    public SBukkitEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent event) {
        super(event);
    }
}
