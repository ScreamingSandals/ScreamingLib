package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerLevelChangeEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class PlayerLevelChangeEventListener extends AbstractBukkitEventHandlerFactory<PlayerLevelChangeEvent, SPlayerLevelChangeEvent> {

    public PlayerLevelChangeEventListener(Plugin plugin) {
        super(PlayerLevelChangeEvent.class, SPlayerLevelChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerLevelChangeEvent wrapEvent(PlayerLevelChangeEvent event, EventPriority priority) {
        return new SPlayerLevelChangeEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ImmutableObjectLink.of(event::getOldLevel),
                ImmutableObjectLink.of(event::getNewLevel)
        );
    }
}
