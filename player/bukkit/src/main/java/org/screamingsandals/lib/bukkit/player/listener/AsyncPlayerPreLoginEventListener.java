package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.utils.AdventureHelper;

public class AsyncPlayerPreLoginEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLowest(AsyncPlayerPreLoginEvent chatEvent) {
        fireEvent(chatEvent, org.screamingsandals.lib.event.EventPriority.LOWEST);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLow(AsyncPlayerPreLoginEvent chatEvent) {
        fireEvent(chatEvent, org.screamingsandals.lib.event.EventPriority.LOW);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNormal(AsyncPlayerPreLoginEvent chatEvent) {
        fireEvent(chatEvent, org.screamingsandals.lib.event.EventPriority.NORMAL);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHigh(AsyncPlayerPreLoginEvent chatEvent) {
        fireEvent(chatEvent, org.screamingsandals.lib.event.EventPriority.HIGH);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHighest(AsyncPlayerPreLoginEvent chatEvent) {
        fireEvent(chatEvent, org.screamingsandals.lib.event.EventPriority.HIGHEST);
    }

    private void fireEvent(AsyncPlayerPreLoginEvent event, org.screamingsandals.lib.event.EventPriority eventPriority) {
        final var toFire = new org.screamingsandals.lib.player.event.AsyncPlayerPreLoginEvent(event.getUniqueId(), event.getName(), event.getAddress());
        try {
            final var result = EventManager.getDefaultEventManager().fireEventAsync(toFire, eventPriority)
                    .get();

            switch (result.getResult()) {
                case ALLOWED:
                    event.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
                    break;
                case KICK_FULL:
                    event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_FULL);
                    break;
                case KICK_BANNED:
                    event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                    break;
                case KICK_WHITELIST:
                    event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                    break;
                default:
                    event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                    break;

            }
            event.setKickMessage(AdventureHelper.toLegacy(result.getMessage()));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
