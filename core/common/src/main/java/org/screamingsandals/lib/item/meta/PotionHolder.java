package org.screamingsandals.lib.item.meta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Getter
@RequiredArgsConstructor
@ConfigSerializable
public class PotionHolder implements ComparableWrapper {
    private final String platformName;

    public <R> R as(Class<R> type) {
        return PotionMapping.convertPotionHolder(this, type);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    public static PotionHolder of(Object potion) {
        return ofOptional(potion).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    public static Optional<PotionHolder> ofOptional(Object potion) {
        if (potion instanceof PotionHolder) {
            return Optional.of((PotionHolder) potion);
        }
        return PotionMapping.resolve(potion);
    }

    public static List<PotionHolder> all() {
        return PotionMapping.getValues();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    @Override
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
