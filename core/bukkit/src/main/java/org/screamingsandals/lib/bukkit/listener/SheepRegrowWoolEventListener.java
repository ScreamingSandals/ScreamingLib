package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SSheepRegrowWoolEvent;
import org.screamingsandals.lib.event.EventPriority;

public class SheepRegrowWoolEventListener extends AbstractBukkitEventHandlerFactory<SheepRegrowWoolEvent, SSheepRegrowWoolEvent> {

    public SheepRegrowWoolEventListener(Plugin plugin) {
        super(SheepRegrowWoolEvent.class, SSheepRegrowWoolEvent.class, plugin);
    }

    @Override
    protected SSheepRegrowWoolEvent wrapEvent(SheepRegrowWoolEvent event, EventPriority priority) {
        return new SSheepRegrowWoolEvent(EntityMapper.wrapEntity(event.getEntity()).orElseThrow());
    }
}
