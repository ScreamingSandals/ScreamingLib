package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerUnleashEntityEvent;

public class PlayerUnleashEntityEventListener extends AbstractBukkitEventHandlerFactory<PlayerUnleashEntityEvent, SPlayerUnleashEntityEvent> {

    public PlayerUnleashEntityEventListener(Plugin plugin) {
        super(PlayerUnleashEntityEvent.class, SPlayerUnleashEntityEvent.class, plugin);
    }

    @Override
    protected SPlayerUnleashEntityEvent wrapEvent(PlayerUnleashEntityEvent event, EventPriority priority) {
        return new SPlayerUnleashEntityEvent(PlayerMapper.wrapPlayer(event.getPlayer()));
    }
}
