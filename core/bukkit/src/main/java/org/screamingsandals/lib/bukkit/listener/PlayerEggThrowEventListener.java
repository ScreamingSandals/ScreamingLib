package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerEggThrowEvent;

public class PlayerEggThrowEventListener extends AbstractBukkitEventHandlerFactory<PlayerEggThrowEvent, SPlayerEggThrowEvent> {

    public PlayerEggThrowEventListener(Plugin plugin) {
        super(PlayerEggThrowEvent.class, SPlayerEggThrowEvent.class, plugin);
    }

    @Override
    protected SPlayerEggThrowEvent wrapEvent(PlayerEggThrowEvent event, EventPriority priority) {
        return new SPlayerEggThrowEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                EntityMapper.wrapEntity(event.getEgg()).orElseThrow(),
                event.isHatching(),
                EntityTypeMapping.resolve(event.getEgg()).orElse(null),
                event.getNumHatches()
        );
    }

    @Override
    protected void postProcess(SPlayerEggThrowEvent wrappedEvent, PlayerEggThrowEvent event) {
        event.setHatchingType(wrappedEvent.getHatchType().as(EntityType.class));
        event.setHatching(wrappedEvent.isHatching());
        event.setNumHatches(wrappedEvent.getNumHatches());
    }
}
