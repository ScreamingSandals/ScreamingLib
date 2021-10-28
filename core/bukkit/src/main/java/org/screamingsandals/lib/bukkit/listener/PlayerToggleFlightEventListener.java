package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerToggleFlightEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class PlayerToggleFlightEventListener extends AbstractBukkitEventHandlerFactory<PlayerToggleFlightEvent, SPlayerToggleFlightEvent> {

    public PlayerToggleFlightEventListener(Plugin plugin) {
        super(PlayerToggleFlightEvent.class, SPlayerToggleFlightEvent.class, plugin);
    }

    @Override
    protected SPlayerToggleFlightEvent wrapEvent(PlayerToggleFlightEvent event, EventPriority priority) {
        return new SPlayerToggleFlightEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ImmutableObjectLink.of(event::isFlying)
        );
    }
}
