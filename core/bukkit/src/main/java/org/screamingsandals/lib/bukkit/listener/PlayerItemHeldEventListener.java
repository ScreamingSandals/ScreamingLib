package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerItemHeldEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class PlayerItemHeldEventListener extends AbstractBukkitEventHandlerFactory<PlayerItemHeldEvent, SPlayerItemHeldEvent> {

    public PlayerItemHeldEventListener(Plugin plugin) {
        super(PlayerItemHeldEvent.class, SPlayerItemHeldEvent.class, plugin);
    }

    @Override
    protected SPlayerItemHeldEvent wrapEvent(PlayerItemHeldEvent event, EventPriority priority) {
        return new SPlayerItemHeldEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ImmutableObjectLink.of(event::getPreviousSlot),
                ImmutableObjectLink.of(event::getNewSlot)
        );
    }
}
