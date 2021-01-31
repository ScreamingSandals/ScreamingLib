package org.screamingsandals.lib.material.meta;

import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.key.MappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;

@AbstractService(pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$")
public class PotionMapping {
    private static PotionMapping mapping = null;
    protected final Map<MappingKey, PotionHolder> potionMapping = new HashMap<>();
    protected BidirectionalConverter<PotionHolder> potionConverter = BidirectionalConverter.<PotionHolder>build()
            .registerW2P(String.class, PotionHolder::getPlatformName)
            .registerP2W(PotionHolder.class, e -> e);

    @SneakyThrows
    public static void init(Supplier<PotionMapping> potionMapping) {
        if (mapping != null) {
            throw new UnsupportedOperationException("Potion mapping is already initialized.");
        }

        mapping = potionMapping.get();

        mapping.bukkit2minecraftMapping();
    }

    private void bukkit2minecraftMapping() {
        map("EMPTY", "UNCRAFTABLE");
        map("LEAPING", "JUMP");
        map("SWIFTNESS", "SPEED");
        map("HEALING", "INSTANT_HEAL");
        map("HARMING", "INSTANT_DAMAGE");
        map("REGENERATION", "REGEN");
    }

    private void map(String potion, String potionBukkit) {
        if (potion == null || potionBukkit == null) {
            throw new IllegalArgumentException("Both potions mustn't be null!");
        }

        var potionNamespaced = NamespacedMappingKey.of(potion);
        var longPotionNamespaced = NamespacedMappingKey.of("long_" + potion);
        var strongPotionNamespaced = NamespacedMappingKey.of("strong_" + potion);

        var potionBukkitNamespaced = NamespacedMappingKey.of(potionBukkit);
        var longPotionBukkitNamespaced = NamespacedMappingKey.of("long_" + potionBukkit);
        var strongPotionBukkitNamespaced = NamespacedMappingKey.of("strong_" + potionBukkit);

        if (potionMapping.containsKey(potionNamespaced) && !potionMapping.containsKey(potionBukkitNamespaced)) {
            potionMapping.put(potionBukkitNamespaced, potionMapping.get(potionNamespaced));
        } else if (potionMapping.containsKey(potionBukkitNamespaced) && !potionMapping.containsKey(potionNamespaced)) {
            potionMapping.put(potionNamespaced, potionMapping.get(potionBukkitNamespaced));
        }
        if (potionMapping.containsKey(longPotionNamespaced) && !potionMapping.containsKey(longPotionBukkitNamespaced)) {
            potionMapping.put(longPotionBukkitNamespaced, potionMapping.get(longPotionNamespaced));
        } else if (potionMapping.containsKey(longPotionBukkitNamespaced) && !potionMapping.containsKey(longPotionNamespaced)) {
            potionMapping.put(longPotionNamespaced, potionMapping.get(longPotionBukkitNamespaced));
        }
        if (potionMapping.containsKey(strongPotionNamespaced) && !potionMapping.containsKey(strongPotionBukkitNamespaced)) {
            potionMapping.put(strongPotionBukkitNamespaced, potionMapping.get(strongPotionNamespaced));
        } else if (potionMapping.containsKey(strongPotionBukkitNamespaced) && !potionMapping.containsKey(strongPotionNamespaced)) {
            potionMapping.put(strongPotionNamespaced, potionMapping.get(strongPotionBukkitNamespaced));
        }
    }

    public static Optional<PotionHolder> resolve(Object potionObject) {
        if (mapping == null) {
            throw new UnsupportedOperationException("Potion mapping is not initialized yet.");
        }
        Optional<PotionHolder> opt = mapping.potionConverter.convertOptional(potionObject);
        if (opt.isPresent()) {
            return opt;
        }
        String potion = potionObject.toString().trim();

        var namespaced = NamespacedMappingKey.ofOptional(potion);

        if (namespaced.isEmpty()) {
            return Optional.empty();
        }

        if (mapping.potionMapping.containsKey(namespaced.get())) {
            return Optional.of(mapping.potionMapping.get(namespaced.get()));
        }

        return Optional.empty();
    }

    public static <T> T convertPotionHolder(PotionHolder holder, Class<T> newType) {
        if (mapping == null) {
            throw new UnsupportedOperationException("Potion mapping is not initialized yet.");
        }
        return mapping.potionConverter.convert(holder, newType);
    }

    public static boolean isInitialized() {
        return mapping != null;
    }
}
