package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerAnimationEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class PlayerAnimationEventListener extends AbstractBukkitEventHandlerFactory<PlayerAnimationEvent, SPlayerAnimationEvent> {

    public PlayerAnimationEventListener(Plugin plugin) {
        super(PlayerAnimationEvent.class, SPlayerAnimationEvent.class, plugin);
    }

    @Override
    protected SPlayerAnimationEvent wrapEvent(PlayerAnimationEvent event, EventPriority priority) {
        return new SPlayerAnimationEvent(
                ImmutableObjectLink.of(() -> new BukkitEntityPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> SPlayerAnimationEvent.PlayerAnimationType.convert(event.getAnimationType().name()))
        );
    }
}
