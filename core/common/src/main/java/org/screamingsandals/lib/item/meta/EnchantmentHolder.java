package org.screamingsandals.lib.item.meta;

import org.jetbrains.annotations.Contract;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface EnchantmentHolder extends ComparableWrapper {

    String platformName();

    int level();

    @Contract(value = "_ -> new", pure = true)
    EnchantmentHolder withLevel(int level);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Override
    boolean is(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    @Override
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    boolean isSameType(Object object);

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    boolean isSameType(Object... objects);

    /**
     * Inconsistent naming (should be isSameType like in other holders)
     */
    @Deprecated(forRemoval = true)
    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    default boolean isType(Object object) {
        return isSameType(object);
    }

    /**
     * Inconsistent naming (should be isSameType like in other holders)
     */
    @Deprecated(forRemoval = true)
    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    default boolean isType(Object... objects) {
        return isSameType(objects);
    }

    /**
     * Use fluent variant
     */
    @Deprecated(forRemoval = true)
    default String getPlatformName() {
        return platformName();
    }

    /**
     * Use fluent variant
     */
    @Deprecated(forRemoval = true)
    default int getLevel() {
        return level();
    }

    /**
     * Inconsistent naming
     */
    @Deprecated(forRemoval = true)
    default EnchantmentHolder newLevel(int level) {
        return withLevel(level);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    static EnchantmentHolder of(Object enchantment) {
        return ofOptional(enchantment).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENCHANTMENT)
    static Optional<EnchantmentHolder> ofOptional(Object enchantment) {
        if (enchantment instanceof EnchantmentHolder) {
            return Optional.of((EnchantmentHolder) enchantment);
        }
        return EnchantmentMapping.resolve(enchantment);
    }

    static List<EnchantmentHolder> all() {
        return EnchantmentMapping.getValues();
    }
}
