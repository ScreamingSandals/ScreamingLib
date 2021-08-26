package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityDeathEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class EntityDeathEventListener extends AbstractBukkitEventHandlerFactory<EntityDeathEvent, SEntityDeathEvent> {

    public EntityDeathEventListener(Plugin plugin) {
        super(EntityDeathEvent.class, SEntityDeathEvent.class, plugin);
    }

    @Override
    protected SEntityDeathEvent wrapEvent(EntityDeathEvent event, EventPriority priority) {
        return new SEntityDeathEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                new CollectionLinkedToCollection<>(
                        event.getDrops(),
                        item -> item.as(ItemStack.class),
                        itemStack -> ItemFactory.build(itemStack).orElseThrow()
                ),
                ObjectLink.of(event::getDroppedExp, event::setDroppedExp)
        );
    }
}
