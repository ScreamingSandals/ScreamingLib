package org.screamingsandals.lib.firework;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.configurate.FireworkEffectHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.*;

@SuppressWarnings("AlternativeMethodAvailable")
@AbstractService
public abstract class FireworkEffectMapping extends AbstractTypeMapper<FireworkEffectHolder> {
    private static FireworkEffectMapping fireworkEffectMapping = null;

    protected BidirectionalConverter<FireworkEffectHolder> fireworkEffectConverter = BidirectionalConverter.<FireworkEffectHolder>build()
            .registerP2W(FireworkEffectHolder.class, e -> e)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return FireworkEffectHolderSerializer.INSTANCE.deserialize(FireworkEffectHolder.class, node);
                } catch (SerializationException e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .registerP2W(Map.class, map -> {
                try {
                    FireworkEffectHolderSerializer.INSTANCE.deserialize(FireworkEffectHolder.class, BasicConfigurationNode.root().set(map));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
                return null;
            });

    @ApiStatus.Internal
    public FireworkEffectMapping() {
        if (fireworkEffectMapping != null) {
            throw new UnsupportedOperationException("FireworkEffectMapping is already initialized.");
        }

        fireworkEffectMapping = this;
    }

    @OnPostConstruct
    public void postConstruct() {
        mapAlias("SMALL", "BALL");
        mapAlias("LARGE", "BALL_LARGE");
    }

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    @OfMethodAlternative(value = FireworkEffectHolder.class, methodName = "ofOptional")
    public static Optional<FireworkEffectHolder> resolve(Object fireworkEffectObject) {
        if (fireworkEffectMapping == null) {
            throw new UnsupportedOperationException("FireworkEffectMapping is not initialized yet.");
        }
        if (fireworkEffectObject == null) {
            return Optional.empty();
        }

        return fireworkEffectMapping.fireworkEffectConverter.convertOptional(fireworkEffectObject).or(() -> fireworkEffectMapping.resolveFromMapping(fireworkEffectObject));
    }

    @OfMethodAlternative(value = FireworkEffectHolder.class, methodName = "all")
    public static List<FireworkEffectHolder> getValues() {
        if (fireworkEffectMapping == null) {
            throw new UnsupportedOperationException("FireworkEffectMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(fireworkEffectMapping.values);
    }
}
