package org.screamingsandals.lib.adventure.spectator.event.hover;

import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.UUID;

public class AdventureEntityContent extends BasicWrapper<HoverEvent.ShowEntity> implements EntityContent {
    public AdventureEntityContent(HoverEvent.ShowEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID id() {
        return wrappedObject.id();
    }

    @Override
    public NamespacedMappingKey type() {
        return NamespacedMappingKey.of(wrappedObject.type().asString());
    }

    @Override
    @Nullable
    public Component name() {
        return AdventureBackend.wrapComponent(wrappedObject.name());
    }
}
