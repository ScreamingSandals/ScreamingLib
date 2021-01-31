package org.screamingsandals.lib.material.meta;

import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.RomanToDecimal;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.key.MappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AbstractService(pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$")
public class PotionEffectMapping {

    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(?<namespaced>[A-Za-z0-9_.\\-/:]+)(\\s+(?<duration>(\\d+|(?=[MDCLXVI])M*(C[MD]|D?C*)(X[CL]|L?X*)(I[XV]|V?I*)))?)?$");
    private static final Function<ConfigurationNode, PotionEffectHolder> CONFIGURATE_METHOD = node -> {
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
    private static PotionEffectMapping mapping = null;
    protected final Map<MappingKey, PotionEffectHolder> potionEffectMapping = new HashMap<>();

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
        if (mapping == null) {
            throw new UnsupportedOperationException("PotionEffect mapping is not initialized yet.");
        }
        Optional<PotionEffectHolder> opt = mapping.potionEffectConverter.convertOptional(potionEffectObject);
        if (opt.isPresent()) {
            return opt;
        }
        String potionEffect = potionEffectObject.toString().trim();

        Matcher matcher = RESOLUTION_PATTERN.matcher(potionEffect);

        if (!matcher.matches()) {
            return Optional.empty();
        }

        if (matcher.group("namespaced") != null) {

            var namespaced = NamespacedMappingKey.of(matcher.group("namespaced"));

            var duration_str = matcher.group("duration");

            if (mapping.potionEffectMapping.containsKey(namespaced)) {
                if (duration_str != null && !duration_str.isEmpty()) {
                    int duration;
                    try {
                        duration = Integer.parseInt(duration_str);
                    } catch (Throwable t) {
                        duration = RomanToDecimal.romanToDecimal(duration_str);
                    }
                    return Optional.of(mapping.potionEffectMapping.get(namespaced).duration(duration));
                } else {
                    return Optional.of(mapping.potionEffectMapping.get(namespaced));
                }
            }
        }

        return Optional.empty();
    }

    @SneakyThrows
    public static void init(Supplier<PotionEffectMapping> mappingClass) {
        if (mapping != null) {
            throw new UnsupportedOperationException("PotionEffect mapping is already initialized.");
        }

        mapping = mappingClass.get();
        mapping.potionEffectConverter.finish();

        mapping.legacyMapping();
    }

    private void legacyMapping() {
        f2l("SLOWNESS", "SLOW");
        f2l("HASTE", "FAST_DIGGING");
        f2l("MINING_FATIGUE", "SLOW_DIGGING");
        f2l("STRENGTH", "INCREASE_DAMAGE");
        f2l("INSTANT_HEALTH", "HEAL");
        f2l("INSTANT_DAMAGE", "HARM");
        f2l("JUMP_BOOST", "JUMP");
        f2l("NAUSEA", "CONFUSION");
        f2l("RESISTANCE", "DAMAGE_RESISTANCE");
    }

    private void f2l(String potionEffect, String legacyPotionEffect) {
        if (potionEffect == null || legacyPotionEffect == null) {
            throw new IllegalArgumentException("Both effects mustn't be null!");
        }

        var potionEffectNamespaced = NamespacedMappingKey.of(potionEffect);
        var legacyPotionEffectNamespaced = NamespacedMappingKey.of(legacyPotionEffect);

        if (potionEffectMapping.containsKey(potionEffectNamespaced) && !potionEffectMapping.containsKey(legacyPotionEffectNamespaced)) {
            potionEffectMapping.put(legacyPotionEffectNamespaced, potionEffectMapping.get(potionEffectNamespaced));
        } else if (potionEffectMapping.containsKey(legacyPotionEffectNamespaced) && !potionEffectMapping.containsKey(potionEffectNamespaced)) {
            potionEffectMapping.put(potionEffectNamespaced, potionEffectMapping.get(legacyPotionEffectNamespaced));
        }
    }

    public static <T> T convertPotionEffectHolder(PotionEffectHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("PotionEffect mapping is not initialized yet.");
        }
        return mapping.potionEffectConverter.convert(holder, newType);
    }

    public static boolean isInitialized() {
        return mapping != null;
    }

}
