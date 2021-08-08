package org.screamingsandals.lib.material.meta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.Wrapper;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
@ConfigSerializable
public class PotionHolder implements Wrapper {
    private final String platformName;

    public <R> R as(Class<R> type) {
        return PotionMapping.convertPotionHolder(this, type);
    }

    public static PotionHolder of(Object potion) {
        return ofOptional(potion).orElseThrow();
    }

    public static Optional<PotionHolder> ofOptional(Object potion) {
        if (potion instanceof PotionHolder) {
            return Optional.of((PotionHolder) potion);
        }
        return PotionMapping.resolve(potion);
    }
}
