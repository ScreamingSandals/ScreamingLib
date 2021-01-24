package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;

public class PlayerLeaveEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLowest(PlayerQuitEvent event) {
        fireEvent(event, org.screamingsandals.lib.event.EventPriority.LOWEST);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLow(PlayerQuitEvent event) {
        fireEvent(event, org.screamingsandals.lib.event.EventPriority.LOW);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNormal(PlayerQuitEvent event) {
        fireEvent(event, org.screamingsandals.lib.event.EventPriority.NORMAL);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHigh(PlayerQuitEvent event) {
        fireEvent(event, org.screamingsandals.lib.event.EventPriority.HIGH);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHighest(PlayerQuitEvent event) {
        fireEvent(event, org.screamingsandals.lib.event.EventPriority.HIGHEST);
    }

    private void fireEvent(PlayerQuitEvent event, org.screamingsandals.lib.event.EventPriority eventPriority) {
        final var toFire = new org.screamingsandals.lib.player.event.PlayerLeaveEvent(PlayerMapper.wrapPlayer(event.getPlayer()));
        EventManager.getDefaultEventManager().fireEvent(toFire, eventPriority);
    }
}
