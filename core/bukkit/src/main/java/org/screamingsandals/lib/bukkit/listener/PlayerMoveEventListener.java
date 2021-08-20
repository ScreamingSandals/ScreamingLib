package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerMoveEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.LocationMapper;

public class PlayerMoveEventListener extends AbstractBukkitEventHandlerFactory<PlayerMoveEvent, SPlayerMoveEvent> {

    public PlayerMoveEventListener(Plugin plugin) {
        super(PlayerMoveEvent.class, SPlayerMoveEvent.class, plugin);
    }

    @Override
    protected SPlayerMoveEvent wrapEvent(PlayerMoveEvent event, EventPriority priority) {
        // the move listener is separated from teleport event listener due to performance issues
        return new SPlayerMoveEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ObjectLink.of(
                        () -> LocationMapper.wrapLocation(event.getFrom()),
                        locationHolder -> event.setFrom(locationHolder.as(Location.class))
                ),
                ObjectLink.of(
                        () -> LocationMapper.wrapLocation(event.getTo()),
                        locationHolder -> event.setTo(locationHolder.as(Location.class))
                )
        );
    }
}
