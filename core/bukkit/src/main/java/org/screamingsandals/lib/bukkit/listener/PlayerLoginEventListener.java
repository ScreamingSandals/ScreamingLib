package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SAsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.event.player.SPlayerLoginEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

public class PlayerLoginEventListener extends AbstractBukkitEventHandlerFactory<PlayerLoginEvent, SPlayerLoginEvent> {

    public PlayerLoginEventListener(Plugin plugin) {
        super(PlayerLoginEvent.class, SPlayerLoginEvent.class, plugin);
    }

    @Override
    protected SPlayerLoginEvent wrapEvent(PlayerLoginEvent event, EventPriority priority) {
        return new SPlayerLoginEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ImmutableObjectLink.of(event::getAddress),
                ImmutableObjectLink.of(event::getHostname),
                ObjectLink.of(
                        () -> SAsyncPlayerPreLoginEvent.Result.valueOf(event.getResult().name().toUpperCase()),
                        result -> event.setResult(PlayerLoginEvent.Result.valueOf(result.name().toUpperCase()))
                ),
                ComponentObjectLink.of(event, "kickMessage", event::getKickMessage, event::setKickMessage)
        );
    }
}
