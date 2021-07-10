package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SSheepDyeWoolEvent;
import org.screamingsandals.lib.event.EventPriority;

public class SheepDyeWoolEventListener extends AbstractBukkitEventHandlerFactory<SheepDyeWoolEvent, SSheepDyeWoolEvent> {

    public SheepDyeWoolEventListener(Plugin plugin) {
        super(SheepDyeWoolEvent.class, SSheepDyeWoolEvent.class, plugin);
    }

    @Override
    protected SSheepDyeWoolEvent wrapEvent(SheepDyeWoolEvent event, EventPriority priority) {
        return new SSheepDyeWoolEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                event.getColor()
        );
    }
}
