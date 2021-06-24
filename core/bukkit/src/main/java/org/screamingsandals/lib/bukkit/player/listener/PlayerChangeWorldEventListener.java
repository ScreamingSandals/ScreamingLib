package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerWorldChangeEvent;

public class PlayerChangeWorldEventListener extends AbstractBukkitEventHandlerFactory<PlayerChangedWorldEvent, SPlayerWorldChangeEvent> {

    public PlayerChangeWorldEventListener(Plugin plugin) {
        super(PlayerChangedWorldEvent.class, SPlayerWorldChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerWorldChangeEvent wrapEvent(PlayerChangedWorldEvent event, EventPriority priority) {
        return new SPlayerWorldChangeEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                new BukkitWorldHolder(event.getFrom())
        );
    }
}
