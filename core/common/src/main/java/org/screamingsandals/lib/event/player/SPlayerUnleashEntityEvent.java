package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.event.entity.SEntityUnleashEvent;

// there's no unleasher in the base event in Bukkit, so even if the events should be merged, they can't be
public interface SPlayerUnleashEntityEvent extends SEntityUnleashEvent, SPlayerEvent {
}
