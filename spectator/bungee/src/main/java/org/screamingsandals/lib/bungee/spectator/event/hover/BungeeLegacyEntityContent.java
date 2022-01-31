package org.screamingsandals.lib.bungee.spectator.event.hover;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.UUID;

public class BungeeLegacyEntityContent extends BasicWrapper<String> implements EntityContent {
    public BungeeLegacyEntityContent(String snbt) {
        super(snbt);
    }

    // TODO: parse snbt
    @Override
    public UUID id() {
        return null; // TODO
    }

    @Override
    public NamespacedMappingKey type() {
        return null; // TODO
    }

    @Override
    public @Nullable Component name() {
        return null; // TODO
    }
}
