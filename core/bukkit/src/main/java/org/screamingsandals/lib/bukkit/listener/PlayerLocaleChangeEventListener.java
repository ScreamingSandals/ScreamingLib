package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerLocaleChangeEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class PlayerLocaleChangeEventListener extends AbstractBukkitEventHandlerFactory<PlayerLocaleChangeEvent, SPlayerLocaleChangeEvent> {

    public PlayerLocaleChangeEventListener(Plugin plugin) {
        super(PlayerLocaleChangeEvent.class, SPlayerLocaleChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerLocaleChangeEvent wrapEvent(PlayerLocaleChangeEvent event, EventPriority priority) {
        return new SPlayerLocaleChangeEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ImmutableObjectLink.of(event::getLocale)
        );
    }
}
