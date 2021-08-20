package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerKickEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

public class PlayerKickEventListener extends AbstractBukkitEventHandlerFactory<PlayerKickEvent, SPlayerKickEvent> {

    public PlayerKickEventListener(Plugin plugin) {
        super(PlayerKickEvent.class, SPlayerKickEvent.class, plugin);
    }

    @Override
    protected SPlayerKickEvent wrapEvent(PlayerKickEvent event, EventPriority priority) {
        return new SPlayerKickEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ComponentObjectLink.of(event, "leaveMessage", event::getLeaveMessage, event::setLeaveMessage),
                ComponentObjectLink.of(event, "reason", event::getReason, event::setReason)
        );
    }
}
