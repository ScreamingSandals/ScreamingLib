package org.screamingsandals.lib.bukkit.plugin.event;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.plugin.event.PluginDisabledEvent;

public class PluginDisabledEventListener extends AbstractBukkitEventHandlerFactory<PluginDisableEvent, PluginDisabledEvent> {
    public PluginDisabledEventListener(Plugin plugin) {
        super(PluginDisableEvent.class, PluginDisabledEvent.class, plugin);
    }

    @Override
    protected PluginDisabledEvent wrapEvent(PluginDisableEvent event, EventPriority priority) {
        return new PluginDisabledEvent(PluginManager.getPluginFromPlatformObject(event.getPlugin()).orElseThrow());
    }
}
