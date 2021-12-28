package org.screamingsandals.lib.item.meta;

import org.screamingsandals.lib.configurate.PotionEffectHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.RomanToDecimal;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.key.MappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.key.NumericMappingKey;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings("AlternativeMethodAvailable")
@AbstractService(pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$")
public abstract class PotionEffectMapping extends AbstractTypeMapper<PotionEffectHolder> {

    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(?<namespaced>[A-Za-z0-9_.\\-/:]+)(\\s+(?<duration>(\\d+|(?=[MDCLXVI])M*(C[MD]|D?C*)(X[CL]|L?X*)(I[XV]|V?I*)))?)?$");
    private static PotionEffectMapping potionEffectMapping;

    protected BidirectionalConverter<PotionEffectHolder> potionEffectConverter = BidirectionalConverter.<PotionEffectHolder>build()
            .registerP2W(PotionEffectHolder.class, e -> e)
            .registerP2W(Map.class, map -> {
                try {
                    return PotionEffectHolderSerializer.INSTANCE.deserialize(PotionEffectHolder.class, BasicConfigurationNode.root().set(map));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
                return null;
            })
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return PotionEffectHolderSerializer.INSTANCE.deserialize(PotionEffectHolder.class, node);
                } catch (SerializationException e) {
                    e.printStackTrace();
                    return null;
                }
            });

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @OfMethodAlternative(value = PotionEffectHolder.class, methodName = "ofOptional")
    public static Optional<PotionEffectHolder> resolve(Object potionEffectObject) {
        if (potionEffectMapping == null) {
            throw new UnsupportedOperationException("PotionEffect mapping is not initialized yet.");
        }
        if (potionEffectObject == null) {
            return Optional.empty();
        }

        return potionEffectMapping.potionEffectConverter.convertOptional(potionEffectObject).or(() -> {
            var potionEffect = potionEffectObject.toString().trim();

            var matcher = RESOLUTION_PATTERN.matcher(potionEffect);

            if (matcher.matches() && matcher.group("namespaced") != null) {

                MappingKey mappingKey;
                try {
                    var id = Integer.valueOf(matcher.group("namespaced"));
                    mappingKey = NumericMappingKey.of(id);
                } catch (Throwable ignored) {
                    mappingKey = NamespacedMappingKey.of(matcher.group("namespaced"));
                }

                var duration_str = matcher.group("duration");

                if (potionEffectMapping.mapping.containsKey(mappingKey)) {
                    if (duration_str != null && !duration_str.isEmpty()) {
                        int duration;
                        try {
                            duration = Integer.parseInt(duration_str);
                        } catch (Throwable t) {
                            duration = RomanToDecimal.romanToDecimal(duration_str);
                        }
                        return Optional.of(potionEffectMapping.mapping.get(mappingKey).withDuration(duration));
                    } else {
                        return Optional.of(potionEffectMapping.mapping.get(mappingKey));
                    }
                }
            }

            return Optional.empty();
        });
    }

    @OfMethodAlternative(value = PotionEffectHolder.class, methodName = "all")
    public static List<PotionEffectHolder> getValues() {
        if (potionEffectMapping == null) {
            throw new UnsupportedOperationException("PotionEffectMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(potionEffectMapping.values);
    }

    protected PotionEffectMapping() {
        if (potionEffectMapping != null) {
            throw new UnsupportedOperationException("PotionEffect mapping is already initialized.");
        }

        potionEffectMapping = this;
    }

    @OnPostConstruct
    public void legacyMapping() {
        mapAlias("SLOWNESS", "SLOW");
        mapAlias("HASTE", "FAST_DIGGING");
        mapAlias("MINING_FATIGUE", "SLOW_DIGGING");
        mapAlias("STRENGTH", "INCREASE_DAMAGE");
        mapAlias("INSTANT_HEALTH", "HEAL");
        mapAlias("INSTANT_DAMAGE", "HARM");
        mapAlias("JUMP_BOOST", "JUMP");
        mapAlias("NAUSEA", "CONFUSION");
        mapAlias("RESISTANCE", "DAMAGE_RESISTANCE");
    }
}
