package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SAsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

public class AsyncPlayerPreLoginEventListener extends AbstractBukkitEventHandlerFactory<AsyncPlayerPreLoginEvent, SAsyncPlayerPreLoginEvent> {

    public AsyncPlayerPreLoginEventListener(Plugin plugin) {
        super(AsyncPlayerPreLoginEvent.class, SAsyncPlayerPreLoginEvent.class, plugin, true);
    }

    @Override
    protected SAsyncPlayerPreLoginEvent wrapEvent(AsyncPlayerPreLoginEvent event, EventPriority priority) {
        return new SAsyncPlayerPreLoginEvent(
                ImmutableObjectLink.of(event::getUniqueId),
                ImmutableObjectLink.of(event::getAddress),
                ObjectLink.of(event::getName, n -> {}),
                ObjectLink.of(() -> SAsyncPlayerPreLoginEvent.Result.valueOf(event.getLoginResult().name()), r -> event.setLoginResult(AsyncPlayerPreLoginEvent.Result.valueOf(r.name()))),
                ComponentObjectLink.of(event, "kickMessage", event::getKickMessage, event::setKickMessage)
        );
    }
}
