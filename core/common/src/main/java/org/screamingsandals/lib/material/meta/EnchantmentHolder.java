package org.screamingsandals.lib.material.meta;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.Wrapper;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Optional;

@RequiredArgsConstructor
@Data
@ConfigSerializable
public final class EnchantmentHolder implements Wrapper {
    private final String platformName;
    private final int level;

    public EnchantmentHolder(String platformName) {
        this(platformName, 1);
    }

    public EnchantmentHolder newLevel(int level) {
        return new EnchantmentHolder(this.platformName, level);
    }

    public <R> R as(Class<R> type) {
        return EnchantmentMapping.convertEnchantmentHolder(this, type);
    }

    public static EnchantmentHolder of(Object enchantment) {
        return ofOptional(enchantment).orElseThrow();
    }

    public static Optional<EnchantmentHolder> ofOptional(Object enchantment) {
        if (enchantment instanceof EnchantmentHolder) {
            return Optional.of((EnchantmentHolder) enchantment);
        }
        return EnchantmentMapping.resolve(enchantment);
    }
}
