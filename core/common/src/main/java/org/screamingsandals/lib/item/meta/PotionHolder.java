package org.screamingsandals.lib.item.meta;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@ConfigSerializable // this shouldn't have config serializable annotation ig
public interface PotionHolder extends ComparableWrapper {

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    String platformName();

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    static PotionHolder of(Object potion) {
        return ofOptional(potion).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    static Optional<PotionHolder> ofOptional(Object potion) {
        if (potion instanceof PotionHolder) {
            return Optional.of((PotionHolder) potion);
        }
        return PotionMapping.resolve(potion);
    }

    static List<PotionHolder> all() {
        return PotionMapping.getValues();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION)
    @Override
    boolean is(Object... objects);
}
