package org.screamingsandals.lib.material.meta;

import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.RomanToDecimal;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PotionEffectMapping {

    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(?:(?<namespace>[A-Za-z][A-Za-z0-9_.\\-]*):)?(?<duration>[A-Za-z][A-Za-z0-9_.\\-/]*)(\\s+(?<level>(\\d+|(?=[MDCLXVI])M*(C[MD]|D?C*)(X[CL]|L?X*)(I[XV]|V?I*)))?)?$");
    private static PotionEffectMapping mapping = null;
    protected final Map<String, PotionEffectHolder> potionEffectMapping = new HashMap<>();

    protected BidirectionalConverter<PotionEffectHolder> potionEffectConverter = BidirectionalConverter.<PotionEffectHolder>build()
            .registerW2P(String.class, PotionEffectHolder::getPlatformName)
            .registerP2W(PotionEffectHolder.class, e -> e)
            .registerP2W(ConfigurationNode.class, node -> {
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
            });

    public static Optional<PotionEffectHolder> resolve(Object potionEffectObject) {
        if (mapping == null) {
            throw new UnsupportedOperationException("PotionEffect mapping is not initialized yet.");
        }
        Optional<PotionEffectHolder> opt = mapping.potionEffectConverter.convertOptional(potionEffectObject);
        if (opt.isPresent()) {
            return opt;
        }
        String enchantment = potionEffectObject.toString().trim();

        Matcher matcher = RESOLUTION_PATTERN.matcher(enchantment);

        if (!matcher.matches()) {
            return Optional.empty();
        }

        if (matcher.group("effect") != null) {

            String namespace = matcher.group("namespace") != null ? matcher.group("namespace").toUpperCase() : "MINECRAFT";
            String name = matcher.group("effect").toUpperCase();
            String duration_str = matcher.group("duration");

            if (mapping.potionEffectMapping.containsKey(namespace + ":" + name)) {
                if (duration_str != null && !duration_str.isEmpty()) {
                    int duration;
                    try {
                        duration = Integer.parseInt(duration_str);
                    } catch (Throwable t) {
                        duration = RomanToDecimal.romanToDecimal(duration_str);
                    }
                    return Optional.of(mapping.potionEffectMapping.get(namespace + ":" + name).duration(duration));
                } else {
                    return Optional.of(mapping.potionEffectMapping.get(namespace + ":" + name));
                }
            } else if (mapping.potionEffectMapping.containsKey(name)) {
                if (duration_str != null && !duration_str.isEmpty()) {
                    int duration;
                    try {
                        duration = Integer.parseInt(duration_str);
                    } catch (Throwable t) {
                        duration = RomanToDecimal.romanToDecimal(duration_str);
                    }
                    return Optional.of(mapping.potionEffectMapping.get(name).duration(duration));
                } else {
                    return Optional.of(mapping.potionEffectMapping.get(name));
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
        if (potionEffectMapping.containsKey(potionEffect.toUpperCase()) && !potionEffectMapping.containsKey(legacyPotionEffect.toUpperCase())) {
            potionEffectMapping.put(legacyPotionEffect.toUpperCase(), potionEffectMapping.get(potionEffect.toUpperCase()));
        } else if (potionEffectMapping.containsKey(legacyPotionEffect.toUpperCase()) && !potionEffectMapping.containsKey(potionEffect.toUpperCase())) {
            potionEffectMapping.put(potionEffect.toUpperCase(), potionEffectMapping.get(legacyPotionEffect.toUpperCase()));
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
