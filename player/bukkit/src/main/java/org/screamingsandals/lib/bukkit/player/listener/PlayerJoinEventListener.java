package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;

public class PlayerJoinEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLowest(PlayerJoinEvent event) {
        fireEvent(event, org.screamingsandals.lib.event.EventPriority.LOWEST);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLow(PlayerJoinEvent event) {
        fireEvent(event, org.screamingsandals.lib.event.EventPriority.LOW);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNormal(PlayerJoinEvent event) {
        fireEvent(event, org.screamingsandals.lib.event.EventPriority.NORMAL);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHigh(PlayerJoinEvent event) {
        fireEvent(event, org.screamingsandals.lib.event.EventPriority.HIGH);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHighest(PlayerJoinEvent event) {
        fireEvent(event, org.screamingsandals.lib.event.EventPriority.HIGHEST);
    }

    private void fireEvent(PlayerJoinEvent event, org.screamingsandals.lib.event.EventPriority eventPriority) {
        final var toFire = new org.screamingsandals.lib.player.event.PlayerJoinEvent(PlayerMapper.wrapPlayer(event.getPlayer()));
        EventManager.getDefaultEventManager().fireEvent(toFire, eventPriority);
    }
}
