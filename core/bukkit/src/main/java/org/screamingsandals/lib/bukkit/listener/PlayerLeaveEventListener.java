package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

public class PlayerLeaveEventListener extends AbstractBukkitEventHandlerFactory<PlayerQuitEvent, SPlayerLeaveEvent> {

    public PlayerLeaveEventListener(Plugin plugin) {
        super(PlayerQuitEvent.class, SPlayerLeaveEvent.class, plugin);
    }

    @Override
    protected SPlayerLeaveEvent wrapEvent(PlayerQuitEvent event, EventPriority priority) {
        return new SPlayerLeaveEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ComponentObjectLink.of(event, "quitMessage", event::getQuitMessage, event::setQuitMessage)
        );
    }
}
