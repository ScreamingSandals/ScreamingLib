package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerToggleSprintEvent;

public class PlayerToggleSprintEventListener extends AbstractBukkitEventHandlerFactory<PlayerToggleSprintEvent, SPlayerToggleSprintEvent> {

    public PlayerToggleSprintEventListener(Plugin plugin) {
        super(PlayerToggleSprintEvent.class, SPlayerToggleSprintEvent.class, plugin);
    }

    @Override
    protected SPlayerToggleSprintEvent wrapEvent(PlayerToggleSprintEvent event, EventPriority priority) {
        return new SPlayerToggleSprintEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                event.isSprinting()
        );
    }
}
