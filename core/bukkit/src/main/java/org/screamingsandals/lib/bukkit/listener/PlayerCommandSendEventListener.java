package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerCommandSendEvent;

public class PlayerCommandSendEventListener extends AbstractBukkitEventHandlerFactory<PlayerCommandSendEvent, SPlayerCommandSendEvent> {

    public PlayerCommandSendEventListener(Plugin plugin) {
        super(PlayerCommandSendEvent.class, SPlayerCommandSendEvent.class, plugin);
    }

    @Override
    protected SPlayerCommandSendEvent wrapEvent(PlayerCommandSendEvent event, EventPriority priority) {
        return new SPlayerCommandSendEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                event.getCommands()
        );
    }
}
