package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerFishEvent;

public class PlayerFishEventListener extends AbstractBukkitEventHandlerFactory<PlayerFishEvent, SPlayerFishEvent> {

    public PlayerFishEventListener(Plugin plugin) {
        super(PlayerFishEvent.class, SPlayerFishEvent.class, plugin);
    }

    @Override
    protected SPlayerFishEvent wrapEvent(PlayerFishEvent event, EventPriority priority) {
        return new SPlayerFishEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                EntityMapper.wrapEntity(event.getCaught()).orElse(null),
                event.getExpToDrop(),
                SPlayerFishEvent.State.convert(event.getState().name()),
                EntityMapper.wrapEntity(event.getHook()).orElse(null)
        );
    }

    @Override
    protected void postProcess(SPlayerFishEvent wrappedEvent, PlayerFishEvent event) {
        event.setExpToDrop(wrappedEvent.getExp());
    }
}
