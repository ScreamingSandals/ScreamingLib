package org.screamingsandals.lib.spectator.event.hover;

import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public interface ItemContent extends Content {
    NamespacedMappingKey id();

    int count();

    // TODO: NBT api
    String tag();
}
