package org.screamingsandals.lib.material.meta;

import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Optional;
import java.util.function.Supplier;

@AbstractService(pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$")
public abstract class PotionMapping extends AbstractTypeMapper<PotionHolder> {
    private static PotionMapping potionMapping = null;
    protected BidirectionalConverter<PotionHolder> potionConverter = BidirectionalConverter.<PotionHolder>build()
            .registerW2P(String.class, PotionHolder::getPlatformName)
            .registerP2W(PotionHolder.class, e -> e);

    @SneakyThrows
    public static void init(Supplier<PotionMapping> potionMappingSupplier) {
        if (potionMapping != null) {
            throw new UnsupportedOperationException("Potion mapping is already initialized.");
        }

        potionMapping = potionMappingSupplier.get();

        potionMapping.bukkit2minecraftMapping();
    }

    private void bukkit2minecraftMapping() {
        mapAlias("EMPTY", "UNCRAFTABLE");
        mapAlias("LEAPING", "JUMP");
        mapAlias("SWIFTNESS", "SPEED");
        mapAlias("HEALING", "INSTANT_HEAL");
        mapAlias("HARMING", "INSTANT_DAMAGE");
        mapAlias("REGENERATION", "REGEN");
    }

    protected void mapAlias(String potion, String potionBukkit) {
        if (potion == null || potionBukkit == null) {
            throw new IllegalArgumentException("Both potions mustn't be null!");
        }

        super.mapAlias(potion, potionBukkit);
        super.mapAlias("long_" + potion, "long_" + potionBukkit);
        super.mapAlias("strong_" + potion, "strong_" + potionBukkit);
    }

    public static Optional<PotionHolder> resolve(Object potionObject) {
        if (potionMapping == null) {
            throw new UnsupportedOperationException("Potion mapping is not initialized yet.");
        }
        if (potionObject == null) {
            return Optional.empty();
        }

        return potionMapping.potionConverter.convertOptional(potionObject).or(() -> potionMapping.resolveFromMapping(potionObject));
    }

    public static <T> T convertPotionHolder(PotionHolder holder, Class<T> newType) {
        if (potionMapping == null) {
            throw new UnsupportedOperationException("Potion mapping is not initialized yet.");
        }
        return potionMapping.potionConverter.convert(holder, newType);
    }

    public static boolean isInitialized() {
        return potionMapping != null;
    }
}
