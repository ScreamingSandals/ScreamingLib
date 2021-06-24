package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SFoodLevelChangeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;

public class FoodLevelChangeEventListener extends AbstractBukkitEventHandlerFactory<FoodLevelChangeEvent, SFoodLevelChangeEvent> {

    public FoodLevelChangeEventListener(Plugin plugin) {
        super(FoodLevelChangeEvent.class, SFoodLevelChangeEvent.class, plugin);
    }

    @Override
    protected SFoodLevelChangeEvent wrapEvent(FoodLevelChangeEvent event, EventPriority priority) {
        return new SFoodLevelChangeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getFoodLevel(),
                ItemFactory.build(event.getItem()).orElse(null)
        );
    }

    @Override
    protected void postProcess(SFoodLevelChangeEvent wrappedEvent, FoodLevelChangeEvent event) {
        event.setFoodLevel(wrappedEvent.getLevel());
    }
}
