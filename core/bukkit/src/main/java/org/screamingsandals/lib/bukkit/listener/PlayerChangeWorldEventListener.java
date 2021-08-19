package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.bukkit.world.BukkitWorldHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerWorldChangeEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class PlayerChangeWorldEventListener extends AbstractBukkitEventHandlerFactory<PlayerChangedWorldEvent, SPlayerWorldChangeEvent> {

    public PlayerChangeWorldEventListener(Plugin plugin) {
        super(PlayerChangedWorldEvent.class, SPlayerWorldChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerWorldChangeEvent wrapEvent(PlayerChangedWorldEvent event, EventPriority priority) {
        return new SPlayerWorldChangeEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> new BukkitWorldHolder(event.getFrom()))
        );
    }
}
