package org.screamingsandals.lib.bukkit.plugin.event;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.plugin.event.PluginEnabledEvent;

public class PluginEnabledEventListener extends AbstractBukkitEventHandlerFactory<PluginEnableEvent, PluginEnabledEvent> {
    public PluginEnabledEventListener(Plugin plugin) {
        super(PluginEnableEvent.class, PluginEnabledEvent.class, plugin);
    }

    @Override
    protected PluginEnabledEvent wrapEvent(PluginEnableEvent event, EventPriority priority) {
        return new PluginEnabledEvent(PluginManager.getPluginFromPlatformObject(event.getPlugin()).orElseThrow());
    }
}
