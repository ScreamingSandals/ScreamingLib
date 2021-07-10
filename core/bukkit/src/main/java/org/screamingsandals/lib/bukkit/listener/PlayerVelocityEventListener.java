package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerVelocityChangeEvent;

public class PlayerVelocityEventListener extends AbstractBukkitEventHandlerFactory<PlayerVelocityEvent, SPlayerVelocityChangeEvent> {

    public PlayerVelocityEventListener(Plugin plugin) {
        super(PlayerVelocityEvent.class, SPlayerVelocityChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerVelocityChangeEvent wrapEvent(PlayerVelocityEvent event, EventPriority priority) {
        return new SPlayerVelocityChangeEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                event.getVelocity()
        );
    }
}
