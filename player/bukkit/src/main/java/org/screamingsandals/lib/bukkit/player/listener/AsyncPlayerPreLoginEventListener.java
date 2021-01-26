package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.bukkit.event.AbstractEventListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.AdventureHelper;

public class AsyncPlayerPreLoginEventListener extends AbstractEventListener<AsyncPlayerPreLoginEvent> {

    @Override
    protected void onFire(AsyncPlayerPreLoginEvent event, EventPriority eventPriority) {
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
