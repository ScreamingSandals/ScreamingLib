package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerMoveEvent;
import org.screamingsandals.lib.world.LocationMapper;

public class PlayerMoveEventListener extends AbstractBukkitEventHandlerFactory<PlayerMoveEvent, SPlayerMoveEvent> {

    public PlayerMoveEventListener(Plugin plugin) {
        super(PlayerMoveEvent.class, SPlayerMoveEvent.class, plugin);
    }

    @Override
    protected SPlayerMoveEvent wrapEvent(PlayerMoveEvent event, EventPriority priority) {
        return new SPlayerMoveEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                LocationMapper.wrapLocation(event.getFrom()),
                LocationMapper.wrapLocation(event.getTo())
        );
    }
}
