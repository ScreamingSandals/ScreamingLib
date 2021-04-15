package org.screamingsandals.lib.plugin.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.plugin.PluginDescription;

/**
 * NOTE: Only Platforms that support plugin enabling and disabling of plugins have this event.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PluginDisabledEvent  extends AbstractEvent {
    private final PluginDescription plugin;
}