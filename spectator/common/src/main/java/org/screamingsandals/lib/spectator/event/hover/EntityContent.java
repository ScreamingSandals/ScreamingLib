package org.screamingsandals.lib.spectator.event.hover;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.UUID;

public interface EntityContent extends Content {
    UUID id();

    NamespacedMappingKey type();

    @Nullable
    Component name();
}
