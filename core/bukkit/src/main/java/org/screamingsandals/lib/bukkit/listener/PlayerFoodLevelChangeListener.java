package org.screamingsandals.lib.bukkit.listener;


import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerFoodLevelChangeEvent;

public class PlayerFoodLevelChangeListener  extends AbstractBukkitEventHandlerFactory<FoodLevelChangeEvent, SPlayerFoodLevelChangeEvent> {

    public PlayerFoodLevelChangeListener(Plugin plugin) {
        super(FoodLevelChangeEvent.class, SPlayerFoodLevelChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerFoodLevelChangeEvent wrapEvent(FoodLevelChangeEvent event, EventPriority priority) {
        if (event.getEntity() instanceof Player) {
            return new SPlayerFoodLevelChangeEvent(
                    PlayerMapper.wrapPlayer((Player) event.getEntity()),
                    ItemFactory.build(event.getItem()).orElse(null),
                    event.getFoodLevel()
            );
        }
        return null;
    }

    @Override
    protected void postProcess(SPlayerFoodLevelChangeEvent wrappedEvent, FoodLevelChangeEvent event) {
        event.setFoodLevel(wrappedEvent.getFoodLevel());
    }
}
