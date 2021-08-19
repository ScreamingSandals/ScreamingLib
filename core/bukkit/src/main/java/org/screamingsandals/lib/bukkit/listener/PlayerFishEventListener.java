package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerFishEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class PlayerFishEventListener extends AbstractBukkitEventHandlerFactory<PlayerFishEvent, SPlayerFishEvent> {

    public PlayerFishEventListener(Plugin plugin) {
        super(PlayerFishEvent.class, SPlayerFishEvent.class, plugin);
    }

    @Override
    protected SPlayerFishEvent wrapEvent(PlayerFishEvent event, EventPriority priority) {
        return new SPlayerFishEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getCaught()).orElse(null)),
                ObjectLink.of(event::getExpToDrop, event::setExpToDrop),
                ImmutableObjectLink.of(() -> SPlayerFishEvent.State.convert(event.getState().name())),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getHook()).orElse(null))
        );
    }
}
