package org.screamingsandals.lib.firework;

import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Contract;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface FireworkEffectHolder extends ComparableWrapper {

    String platformName();

    List<RGBLike> colors();

    List<RGBLike> fadeColors();

    boolean flicker();

    boolean trail();

    @Contract(value = "_ -> new", pure = true)
    FireworkEffectHolder withColors(List<RGBLike> colors);

    @Contract(value = "_ -> new", pure = true)
    FireworkEffectHolder withFadeColors(List<RGBLike> fadeColors);

    @Contract(value = "_ -> new", pure = true)
    FireworkEffectHolder withFlicker(boolean flicker);

    @Contract(value = "_ -> new", pure = true)
    FireworkEffectHolder withTrail(boolean trail);

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
    default List<RGBLike> getColors() {
        return colors();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default List<RGBLike> getFadeColors() {
        return fadeColors();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default boolean isFlicker() {
        return flicker();
    }

    /**
     * Use fluent variant!
     */
    @Deprecated(forRemoval = true)
    default boolean isTrail() {
        return trail();
    }

    /**
     * Inconsistent naming (holders use with prefix)
     */
    @Deprecated(forRemoval = true)
    default FireworkEffectHolder colors(List<RGBLike> colors) {
        return withColors(colors);
    }

    /**
     * Inconsistent naming (holders use with prefix)
     */
    @Deprecated(forRemoval = true)
    default FireworkEffectHolder fadeColors(List<RGBLike> fadeColors) {
        return withFadeColors(fadeColors);
    }

    /**
     * Inconsistent naming (holders use with prefix)
     */
    @Deprecated(forRemoval = true)
    default FireworkEffectHolder flicker(boolean flicker) {
        return withFlicker(flicker);
    }

    /**
     * Inconsistent naming (holders use with prefix)
     */
    @Deprecated(forRemoval = true)
    default FireworkEffectHolder trail(boolean trail) {
        return withTrail(trail);
    }

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    boolean is(Object... objects);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    static FireworkEffectHolder of(Object effect) {
        return ofOptional(effect).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    static Optional<FireworkEffectHolder> ofOptional(Object effect) {
        if (effect instanceof FireworkEffectHolder) {
            return Optional.of((FireworkEffectHolder) effect);
        }
        return FireworkEffectMapping.resolve(effect);
    }

    static List<FireworkEffectHolder> all() {
        return FireworkEffectMapping.getValues();
    }
}
