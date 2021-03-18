package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerAnimationEvent;

public class PlayerAnimationEventListener extends AbstractBukkitEventHandlerFactory<PlayerAnimationEvent, SPlayerAnimationEvent> {

    public PlayerAnimationEventListener(Plugin plugin) {
        super(org.bukkit.event.player.PlayerAnimationEvent.class, SPlayerAnimationEvent.class, plugin);
    }

    @Override
    protected SPlayerAnimationEvent wrapEvent(org.bukkit.event.player.PlayerAnimationEvent event, EventPriority priority) {
        return new SPlayerAnimationEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                SPlayerAnimationEvent.PlayerAnimationType.convert(event.getAnimationType().name())
        );
    }
}
