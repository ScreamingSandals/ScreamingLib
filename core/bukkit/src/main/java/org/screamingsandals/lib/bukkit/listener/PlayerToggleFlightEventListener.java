package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerToggleFlightEvent;

public class PlayerToggleFlightEventListener extends AbstractBukkitEventHandlerFactory<PlayerToggleFlightEvent, SPlayerToggleFlightEvent> {

    public PlayerToggleFlightEventListener(Plugin plugin) {
        super(PlayerToggleFlightEvent.class, SPlayerToggleFlightEvent.class, plugin);
    }

    @Override
    protected SPlayerToggleFlightEvent wrapEvent(PlayerToggleFlightEvent event, EventPriority priority) {
        return new SPlayerToggleFlightEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                event.isFlying()
        );
    }
}
