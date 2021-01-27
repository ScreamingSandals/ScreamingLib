package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerLeaveEvent;

public class PlayerLeaveEventListener extends AbstractBukkitEventHandlerFactory<PlayerQuitEvent, SPlayerLeaveEvent> {

    public PlayerLeaveEventListener(Plugin plugin) {
        super(PlayerQuitEvent.class, SPlayerLeaveEvent.class, plugin);
    }

    @Override
    protected SPlayerLeaveEvent wrapEvent(PlayerQuitEvent event, EventPriority priority) {
        return new SPlayerLeaveEvent(PlayerMapper.wrapPlayer(event.getPlayer()));
    }
}
