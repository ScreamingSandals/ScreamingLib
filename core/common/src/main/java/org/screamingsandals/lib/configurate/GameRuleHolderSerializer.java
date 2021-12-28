package org.screamingsandals.lib.configurate;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class GameRuleHolderSerializer implements TypeSerializer<GameRuleHolder> {
    @Override
    public GameRuleHolder deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            return GameRuleHolder.of(node.getString());
        } catch (Throwable t) {
            throw new SerializationException(t);
        }
    }

    @Override
    public void serialize(Type type, @Nullable GameRuleHolder obj, ConfigurationNode node) throws SerializationException {
        node.set(obj == null ? null : obj.getPlatformName());
    }
}
