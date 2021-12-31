package org.screamingsandals.lib.bungee.spectator.event.hover;

import net.md_5.bungee.api.chat.hover.content.Entity;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.spectator.BungeeBackend;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.UUID;

public class BungeeEntityContent extends BasicWrapper<Entity> implements EntityContent {
    public BungeeEntityContent(Entity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public UUID id() {
        return UUID.fromString(wrappedObject.getId());
    }

    @Override
    public NamespacedMappingKey type() {
        var type = wrappedObject.getType();
        if (type == null) {
            return NamespacedMappingKey.of("minecraft:pig"); // md_5's nice api said: will be pig if null
        }
        return NamespacedMappingKey.of(type);
    }

    @Override
    @Nullable
    public Component name() {
        return BungeeBackend.wrapComponent(wrappedObject.getName());
    }
}
