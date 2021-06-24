package org.screamingsandals.lib.material.meta;

import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.RomanToDecimal;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@AbstractService(pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$")
public abstract class PotionEffectMapping extends AbstractTypeMapper<PotionEffectHolder> {

    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(?<namespaced>[A-Za-z0-9_.\\-/:]+)(\\s+(?<duration>(\\d+|(?=[MDCLXVI])M*(C[MD]|D?C*)(X[CL]|L?X*)(I[XV]|V?I*)))?)?$");
    private static final Function<ConfigurationNode, PotionEffectHolder> CONFIGURATE_METHOD = node -> {
        if (!node.isMap()) {
            return resolve(node.getString()).orElse(null);
        }

        var effectNode = node.node("effect");
        var durationNode = node.node("duration");
        var amplifierNode = node.node("amplifier");
        var ambientNode = node.node("ambient");
        var particlesNode = node.node("particles");
        var iconNode = node.node("icon");

        var holderOptional = resolve(effectNode.getString());
        if (holderOptional.isPresent()) {
            var holder = holderOptional.get();
            return holder
                    .duration(durationNode.getInt(holder.getDuration()))
                    .amplifier(amplifierNode.getInt(holder.getAmplifier()))
                    .ambient(ambientNode.getBoolean(holder.isAmbient()))
                    .particles(particlesNode.getBoolean(holder.isParticles()))
                    .icon(iconNode.getBoolean(holder.isIcon()));
        }
        return null;
    };
    private static PotionEffectMapping potionEffectMapping = null;

    protected BidirectionalConverter<PotionEffectHolder> potionEffectConverter = BidirectionalConverter.<PotionEffectHolder>build()
            .registerW2P(String.class, PotionEffectHolder::getPlatformName)
            .registerP2W(PotionEffectHolder.class, e -> e)
            .registerP2W(ConfigurationNode.class, CONFIGURATE_METHOD)
            .registerP2W(Map.class, map -> {
                try {
                    return CONFIGURATE_METHOD.apply(BasicConfigurationNode.root().set(map));
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
                return null;
            });

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

                var namespaced = NamespacedMappingKey.of(matcher.group("namespaced"));

                var duration_str = matcher.group("duration");

                if (potionEffectMapping.mapping.containsKey(namespaced)) {
                    if (duration_str != null && !duration_str.isEmpty()) {
                        int duration;
                        try {
                            duration = Integer.parseInt(duration_str);
                        } catch (Throwable t) {
                            duration = RomanToDecimal.romanToDecimal(duration_str);
                        }
                        return Optional.of(potionEffectMapping.mapping.get(namespaced).duration(duration));
                    } else {
                        return Optional.of(potionEffectMapping.mapping.get(namespaced));
                    }
                }
            }

            return Optional.empty();
        });
    }

    @SneakyThrows
    public static void init(Supplier<PotionEffectMapping> potionEffectMappingSupplier) {
        if (potionEffectMapping != null) {
            throw new UnsupportedOperationException("PotionEffect mapping is already initialized.");
        }

        potionEffectMapping = potionEffectMappingSupplier.get();

        potionEffectMapping.legacyMapping();
    }

    private void legacyMapping() {
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

    public static <T> T convertPotionEffectHolder(PotionEffectHolder holder, Class<T> newType) {
        if (potionEffectMapping == null) {
            throw new UnsupportedOperationException("PotionEffect mapping is not initialized yet.");
        }
        return potionEffectMapping.potionEffectConverter.convert(holder, newType);
    }

    public static boolean isInitialized() {
        return potionEffectMapping != null;
    }

}
