package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerFoodLevelChangeEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class PlayerFoodLevelChangeListener extends AbstractBukkitEventHandlerFactory<FoodLevelChangeEvent, SPlayerFoodLevelChangeEvent> {

    public PlayerFoodLevelChangeListener(Plugin plugin) {
        super(FoodLevelChangeEvent.class, SPlayerFoodLevelChangeEvent.class, plugin);
    }

    @Override
    protected SPlayerFoodLevelChangeEvent wrapEvent(FoodLevelChangeEvent event, EventPriority priority) {
        if (event.getEntity() instanceof Player) {
            return new SPlayerFoodLevelChangeEvent(
                    ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer((Player) event.getEntity())),
                    ImmutableObjectLink.of(() -> ItemFactory.build(event.getItem()).orElse(null)),
                    ObjectLink.of(event::getFoodLevel, event::setFoodLevel)
            );
        }
        return null;
    }
}
