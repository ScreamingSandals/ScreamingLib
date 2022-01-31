package org.screamingsandals.lib.bungee.spectator.event.hover;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class BungeeLegacyItemContent extends BasicWrapper<String> implements ItemContent {
    public BungeeLegacyItemContent(String snbt) {
        super(snbt);
    }

    // TODO: parse snbt
    @Override
    public NamespacedMappingKey id() {
        return null; // TODO
    }

    @Override
    public int count() {
        return 0; // TODO
    }

    @Override
    public @Nullable String tag() {
        return wrappedObject; // TODO
    }
}
