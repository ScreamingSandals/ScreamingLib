package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerEggThrowEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class PlayerEggThrowEventListener extends AbstractBukkitEventHandlerFactory<PlayerEggThrowEvent, SPlayerEggThrowEvent> {

    public PlayerEggThrowEventListener(Plugin plugin) {
        super(PlayerEggThrowEvent.class, SPlayerEggThrowEvent.class, plugin);
    }

    @Override
    protected SPlayerEggThrowEvent wrapEvent(PlayerEggThrowEvent event, EventPriority priority) {
        return new SPlayerEggThrowEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEgg()).orElseThrow()),
                ObjectLink.of(event::isHatching, event::setHatching),
                ObjectLink.of(
                        () -> EntityTypeHolder.ofOptional(event.getEgg()).orElse(null),
                        entityTypeHolder -> event.setHatchingType(entityTypeHolder.as(EntityType.class))
                ),
                ObjectLink.of(event::getNumHatches, event::setNumHatches)
        );
    }
}
