package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.EntityPickupItemEvent;
import org.screamingsandals.lib.event.player.SPlayerPickupItemEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

public class SBukkitModernPlayerPickupItemEvent extends SBukkitEntityPickupItemEvent implements SPlayerPickupItemEvent {
    public SBukkitModernPlayerPickupItemEvent(EntityPickupItemEvent event) {
        super(event);
    }

    @Override
    public PlayerWrapper getPlayer() {
        return (PlayerWrapper) getEntity();
    }
}
