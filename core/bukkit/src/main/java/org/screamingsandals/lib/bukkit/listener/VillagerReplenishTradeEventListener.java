package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SVillagerReplenishTradeEvent;
import org.screamingsandals.lib.event.EventPriority;

public class VillagerReplenishTradeEventListener extends AbstractBukkitEventHandlerFactory<VillagerReplenishTradeEvent, SVillagerReplenishTradeEvent> {

    public VillagerReplenishTradeEventListener(Plugin plugin) {
        super(VillagerReplenishTradeEvent.class, SVillagerReplenishTradeEvent.class, plugin);
    }

    @Override
    protected SVillagerReplenishTradeEvent wrapEvent(VillagerReplenishTradeEvent event, EventPriority priority) {
        return new SVillagerReplenishTradeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getRecipe(),
                event.getBonus()
        );
    }

    @Override
    protected void postProcess(SVillagerReplenishTradeEvent wrappedEvent, VillagerReplenishTradeEvent event) {
        event.setBonus(wrappedEvent.getBonus());
        event.setRecipe((MerchantRecipe) wrappedEvent.getRecipe());
    }
}
