package org.screamingsandals.lib.item.meta;

import org.jetbrains.annotations.Contract;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@ConfigSerializable // this shouldn't have config serializable annotation ig
public interface PotionEffectHolder extends ComparableWrapper {
    String platformName();

    int duration();

    int amplifier();

    boolean ambient();

    boolean particles();

    boolean icon();

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default int getDuration() {
        return duration();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default int getAmplifier() {
        return amplifier();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default boolean isAmbient() {
        return ambient();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default boolean isParticles() {
        return particles();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default boolean isIcon() {
        return icon();
    }

    @Contract(value = "_ -> new", pure = true)
    PotionEffectHolder withDuration(int duration);

    /**
     * Inconsistent naming: Holders use "with" prefix
     */
    @Deprecated(forRemoval = true)
    @Contract(value = "_ -> new", pure = true)
    default PotionEffectHolder duration(int duration) {
        return withDuration(duration);
    }

    @Contract(value = "_ -> new", pure = true)
    PotionEffectHolder withAmplifier(int amplifier);

    /**
     * Inconsistent naming: Holders use "with" prefix
     */
    @Deprecated(forRemoval = true)
    @Contract(value = "_ -> new", pure = true)
    default PotionEffectHolder amplifier(int amplifier) {
        return withAmplifier(amplifier);
    }

    @Contract(value = "_ -> new", pure = true)
    PotionEffectHolder withAmbient(boolean ambient);

    /**
     * Inconsistent naming: Holders use "with" prefix
     */
    @Deprecated(forRemoval = true)
    @Contract(value = "_ -> new", pure = true)
    default PotionEffectHolder ambient(boolean ambient) {
        return withAmbient(ambient);
    }

    @Contract(value = "_ -> new", pure = true)
    PotionEffectHolder withParticles(boolean particles);

    /**
     * Inconsistent naming: Holders use "with" prefix
     */
    @Deprecated(forRemoval = true)
    @Contract(value = "_ -> new", pure = true)
    default PotionEffectHolder particles(boolean particles) {
        return withParticles(particles);
    }

    @Contract(value = "_ -> new", pure = true)
    PotionEffectHolder withIcon(boolean icon);

    /**
     * Inconsistent naming: Holders use "with" prefix
     */
    @Deprecated(forRemoval = true)
    @Contract(value = "_ -> new", pure = true)
    default PotionEffectHolder icon(boolean icon) {
        return withIcon(icon);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    @Override
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    boolean isSameType(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    boolean isSameType(Object... objects);

    /**
     * Inconsistent naming (should be isSameType like in other holders)
     */
    @Deprecated(forRemoval = true)
    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    default boolean isType(Object object) {
        return isSameType(object);
    }

    /**
     * Inconsistent naming (should be isSameType like in other holders)
     */
    @Deprecated(forRemoval = true)
    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    default boolean isType(Object... objects) {
        return isSameType(objects);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    static PotionEffectHolder of(Object effect) {
        return ofOptional(effect).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.POTION_EFFECT)
    static Optional<PotionEffectHolder> ofOptional(Object effect) {
        if (effect instanceof PotionEffectHolder) {
            return Optional.of((PotionEffectHolder) effect);
        }
        return PotionEffectMapping.resolve(effect);
    }

    static List<PotionEffectHolder> all() {
        return PotionEffectMapping.getValues();
    }
}
