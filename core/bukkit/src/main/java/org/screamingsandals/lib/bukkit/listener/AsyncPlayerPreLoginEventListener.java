package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SAsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.utils.AdventureHelper;

public class AsyncPlayerPreLoginEventListener extends AbstractBukkitEventHandlerFactory<AsyncPlayerPreLoginEvent, SAsyncPlayerPreLoginEvent> {

    public AsyncPlayerPreLoginEventListener(Plugin plugin) {
        super(AsyncPlayerPreLoginEvent.class, SAsyncPlayerPreLoginEvent.class, plugin, true);
    }

    @Override
    protected SAsyncPlayerPreLoginEvent wrapEvent(AsyncPlayerPreLoginEvent event, EventPriority priority) {
        return new SAsyncPlayerPreLoginEvent(event.getUniqueId(), event.getName(), event.getAddress());
    }

    @Override
    protected void postProcess(SAsyncPlayerPreLoginEvent wrappedEvent, AsyncPlayerPreLoginEvent event) {
        if (wrappedEvent.getResult() == null) {
            return;
        }

        switch (wrappedEvent.getResult()) {
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

        if (wrappedEvent.getMessage() != null) {
            event.setKickMessage(AdventureHelper.toLegacy(wrappedEvent.getMessage()));
        }
    }
}
