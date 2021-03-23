package org.screamingsandals.lib.bukkit.entity.listener;

import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.event.SVillagerAcquireTradeEvent;
import org.screamingsandals.lib.event.EventPriority;

public class VillagerAcquireTradeEventListener extends AbstractBukkitEventHandlerFactory<VillagerAcquireTradeEvent, SVillagerAcquireTradeEvent> {

    public VillagerAcquireTradeEventListener(Plugin plugin) {
        super(VillagerAcquireTradeEvent.class, SVillagerAcquireTradeEvent.class, plugin);
    }

    @Override
    protected SVillagerAcquireTradeEvent wrapEvent(VillagerAcquireTradeEvent event, EventPriority priority) {
        return new SVillagerAcquireTradeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getRecipe()
        );
    }
}
