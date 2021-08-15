package org.screamingsandals.lib.material.meta;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@RequiredArgsConstructor
@Data
@ConfigSerializable
public final class EnchantmentHolder implements ComparableWrapper {
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

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    public static EnchantmentHolder of(Object enchantment) {
        return ofOptional(enchantment).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    public static Optional<EnchantmentHolder> ofOptional(Object enchantment) {
        if (enchantment instanceof EnchantmentHolder) {
            return Optional.of((EnchantmentHolder) enchantment);
        }
        return EnchantmentMapping.resolve(enchantment);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Override
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    public boolean isType(Object object) {
        return platformName.equals(ofOptional(object).map(EnchantmentHolder::getPlatformName).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    public boolean isType(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::isType);
    }
}
