package org.screamingsandals.lib.material.meta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PotionHolder {
    private final String platformName;

    public <R> R as(Class<R> type) {
        return PotionMapping.convertPotionHolder(this, type);
    }
}
