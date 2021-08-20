package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SVillagerReplenishTradeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class VillagerReplenishTradeEventListener extends AbstractBukkitEventHandlerFactory<VillagerReplenishTradeEvent, SVillagerReplenishTradeEvent> {

    public VillagerReplenishTradeEventListener(Plugin plugin) {
        super(VillagerReplenishTradeEvent.class, SVillagerReplenishTradeEvent.class, plugin);
    }

    @Override
    protected SVillagerReplenishTradeEvent wrapEvent(VillagerReplenishTradeEvent event, EventPriority priority) {
        return new SVillagerReplenishTradeEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ObjectLink.of(event::getRecipe, o -> event.setRecipe((MerchantRecipe) o)),
                ObjectLink.of(event::getBonus, event::setBonus)
        );
    }
}
