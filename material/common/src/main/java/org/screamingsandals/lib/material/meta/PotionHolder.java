package org.screamingsandals.lib.material.meta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.Wrapper;

@Getter
@RequiredArgsConstructor
public class PotionHolder implements Wrapper {
    private final String platformName;

    public <R> R as(Class<R> type) {
        return PotionMapping.convertPotionHolder(this, type);
    }
}
