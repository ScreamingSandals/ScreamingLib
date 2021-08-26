package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityShootBowEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class EntityShootBowEventListener extends AbstractBukkitEventHandlerFactory<EntityShootBowEvent, SEntityShootBowEvent> {

    public EntityShootBowEventListener(Plugin plugin) {
        super(EntityShootBowEvent.class, SEntityShootBowEvent.class, plugin);
    }

    @Override
    protected SEntityShootBowEvent wrapEvent(EntityShootBowEvent event, EventPriority priority) {
        return new SEntityShootBowEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getBow()).orElse(null)),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getConsumable()).orElse(null)),
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getProjectile()).orElseThrow()),
                ImmutableObjectLink.of(() -> EquipmentSlotHolder.ofOptional(event.getHand()).orElse(null)),
                ImmutableObjectLink.of(event::getForce),
                ObjectLink.of(event::shouldConsumeItem, event::setConsumeItem)
        );
    }
}
