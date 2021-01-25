package org.screamingsandals.lib.material.meta;

import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AbstractService(pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$")
public class PotionMapping {
    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(?:(?<namespace>[A-Za-z][A-Za-z0-9_.\\-]*):)?(?<potion>[A-Za-z][A-Za-z0-9_.\\-/]*)$");
    private static PotionMapping mapping = null;
    protected final Map<String, PotionHolder> potionMapping = new HashMap<>();
    protected BidirectionalConverter<PotionHolder> potionConverter = BidirectionalConverter.<PotionHolder>build()
            .registerW2P(String.class, PotionHolder::getPlatformName)
            .registerP2W(PotionHolder.class, e -> e);

    @SneakyThrows
    public static void init(Supplier<PotionMapping> potionMapping) {
        if (mapping != null) {
            throw new UnsupportedOperationException("Potion mapping is already initialized.");
        }

        mapping = potionMapping.get();
        mapping.potionConverter.finish();

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
        if (potionMapping.containsKey(potion.toUpperCase()) && !potionMapping.containsKey(potionBukkit.toUpperCase())) {
            potionMapping.put(potionBukkit.toUpperCase(), potionMapping.get(potion.toUpperCase()));
        } else if (potionMapping.containsKey(potionBukkit.toUpperCase()) && !potionMapping.containsKey(potion.toUpperCase())) {
            potionMapping.put(potion.toUpperCase(), potionMapping.get(potionBukkit.toUpperCase()));
        }
        if (potionMapping.containsKey("LONG_" + potion.toUpperCase()) && !potionMapping.containsKey("LONG_" + potionBukkit.toUpperCase())) {
            potionMapping.put("LONG_" + potionBukkit.toUpperCase(), potionMapping.get("LONG_" + potion.toUpperCase()));
        } else if (potionMapping.containsKey("LONG_" + potionBukkit.toUpperCase()) && !potionMapping.containsKey("LONG_" + potion.toUpperCase())) {
            potionMapping.put("LONG_" + potion.toUpperCase(), potionMapping.get("LONG_" + potionBukkit.toUpperCase()));
        }
        if (potionMapping.containsKey("STRONG_" + potion.toUpperCase()) && !potionMapping.containsKey("STRONG_" + potionBukkit.toUpperCase())) {
            potionMapping.put("STRONG_" + potionBukkit.toUpperCase(), potionMapping.get("STRONG_" + potion.toUpperCase()));
        } else if (potionMapping.containsKey("STRONG_" + potionBukkit.toUpperCase()) && !potionMapping.containsKey("STRONG_" + potion.toUpperCase())) {
            potionMapping.put("STRONG_" + potion.toUpperCase(), potionMapping.get("STRONG_" + potionBukkit.toUpperCase()));
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

        Matcher matcher = RESOLUTION_PATTERN.matcher(potion);

        if (!matcher.matches()) {
            return Optional.empty();
        }

        if (matcher.group("potion") != null) {

            String namespace = matcher.group("namespace") != null ? matcher.group("namespace").toUpperCase() : "MINECRAFT";
            String name = matcher.group("potion").toUpperCase();

            if (mapping.potionMapping.containsKey(namespace + ":" + name)) {
                return Optional.of(mapping.potionMapping.get(namespace + ":" + name));
            } else if (mapping.potionMapping.containsKey(name)) {
                return Optional.of(mapping.potionMapping.get(name));
            }
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
