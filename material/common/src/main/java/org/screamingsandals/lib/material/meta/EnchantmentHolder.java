package org.screamingsandals.lib.material.meta;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public final class EnchantmentHolder {
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
}
