package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerJoinEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

public class PlayerJoinEventListener extends AbstractBukkitEventHandlerFactory<PlayerJoinEvent, SPlayerJoinEvent> {

    public PlayerJoinEventListener(Plugin plugin) {
        super(PlayerJoinEvent.class, SPlayerJoinEvent.class, plugin);
    }

    @Override
    protected SPlayerJoinEvent wrapEvent(PlayerJoinEvent event, EventPriority priority) {
        return new SPlayerJoinEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ComponentObjectLink.of(event, "joinMessage", event::getJoinMessage, event::setJoinMessage)
        );
    }
}
