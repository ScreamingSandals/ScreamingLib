package org.screamingsandals.lib.plugin.event;

import lombok.Data;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.plugin.PluginDescription;

/**
 * NOTE: Only Platforms that support plugin enabling and disabling of plugins have this event.
 */
@Data
public class PluginDisabledEvent implements SEvent {
    private final PluginDescription plugin;
}