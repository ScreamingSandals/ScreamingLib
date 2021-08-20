package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SVillagerAcquireTradeEvent;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

public class VillagerAcquireTradeEventListener extends AbstractBukkitEventHandlerFactory<VillagerAcquireTradeEvent, SVillagerAcquireTradeEvent> {

    public VillagerAcquireTradeEventListener(Plugin plugin) {
        super(VillagerAcquireTradeEvent.class, SVillagerAcquireTradeEvent.class, plugin);
    }

    @Override
    protected SVillagerAcquireTradeEvent wrapEvent(VillagerAcquireTradeEvent event, EventPriority priority) {
        return new SVillagerAcquireTradeEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getEntity()).orElseThrow()),
                ObjectLink.of(event::getRecipe, o -> event.setRecipe((MerchantRecipe) o))
        );
    }
}
