package org.screamingsandals.lib.spectator.configurate;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

public class EntityContentSerializer implements TypeSerializer<EntityContent> {
    public static final EntityContentSerializer INSTANCE = new EntityContentSerializer();

    private static final String TYPE_KEY = "type";
    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";

    @Override
    public EntityContent deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            var entityType = NamespacedMappingKey.of(node.node(TYPE_KEY).getString("minecraft:pig"));
            var id = node.node(ID_KEY).get(UUID.class, UUID.randomUUID());
            @Nullable
            var name = node.node(NAME_KEY).get(Component.class);

            return EntityContent.builder()
                    .type(entityType)
                    .id(id)
                    .name(name)
                    .build();
        } catch (Throwable throwable) {
            throw new SerializationException(throwable);
        }
    }

    @Override
    public void serialize(Type type, @Nullable EntityContent obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        node.node(TYPE_KEY).set(obj.type().asString());
        node.node(ID_KEY).set(UUID.class, obj.id());
        node.node(NAME_KEY).set(Component.class, obj.name());
    }
}
