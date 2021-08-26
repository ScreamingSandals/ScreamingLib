package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SFoodLevelChangeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class FoodLevelChangeEventListener extends AbstractBukkitEventHandlerFactory<FoodLevelChangeEvent, SFoodLevelChangeEvent> {

    public FoodLevelChangeEventListener(Plugin plugin) {
        super(FoodLevelChangeEvent.class, SFoodLevelChangeEvent.class, plugin);
    }

    @Override
    protected SFoodLevelChangeEvent wrapEvent(FoodLevelChangeEvent event, EventPriority priority) {
        return new SFoodLevelChangeEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ObjectLink.of(event::getFoodLevel, event::setFoodLevel),
                ImmutableObjectLink.of(() -> ItemFactory.build(event.getItem()).orElse(null))
        );
    }
}
