package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerShearEntityEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class PlayerShearEntityEventListener extends AbstractBukkitEventHandlerFactory<PlayerShearEntityEvent, SPlayerShearEntityEvent> {

    public PlayerShearEntityEventListener(Plugin plugin) {
        super(PlayerShearEntityEvent.class, SPlayerShearEntityEvent.class, plugin);
    }

    @Override
    protected SPlayerShearEntityEvent wrapEvent(PlayerShearEntityEvent event, EventPriority priority) {
        return new SPlayerShearEntityEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getItem()).orElse(null)),
                ImmutableObjectLink.of(() -> EquipmentSlotHolder.ofOptional(event.getHand()).orElse(null))
        );
    }
}
