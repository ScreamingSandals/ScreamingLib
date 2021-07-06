package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityShootBowEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;

public class EntityShootBowEventListener extends AbstractBukkitEventHandlerFactory<EntityShootBowEvent, SEntityShootBowEvent> {

    public EntityShootBowEventListener(Plugin plugin) {
        super(EntityShootBowEvent.class, SEntityShootBowEvent.class, plugin);
    }

    @Override
    protected SEntityShootBowEvent wrapEvent(EntityShootBowEvent event, EventPriority priority) {
        return new SEntityShootBowEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                ItemFactory.build(event.getBow()).orElse(null),
                ItemFactory.build(event.getConsumable()).orElse(null),
                EntityMapper.wrapEntity(event.getProjectile()).orElseThrow(),
                EquipmentSlotMapping.resolve(event.getHand()).orElse(null),
                event.getForce(),
                event.shouldConsumeItem()
        );
    }

    @Override
    protected void postProcess(SEntityShootBowEvent wrappedEvent, EntityShootBowEvent event) {
        event.setConsumeItem(wrappedEvent.isConsumeItem());
        //event.setProjectile(wrappedEvent.getProjectile().as(Entity.class));
    }
}
