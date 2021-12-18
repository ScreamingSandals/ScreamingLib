package org.screamingsandals.lib.item;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;

/**
 * Represents an immutable Item.
 */
public interface Item extends ComparableWrapper, RawValueHolder, ParticleData, Cloneable {
    ItemTypeHolder getType();

    default ItemTypeHolder getMaterial() { // alternative getter (old name)
        return getType();
    }

    int getAmount();

    @Nullable
    Component getDisplayName();

    List<Component> getLore();

    ItemBuilder builder();

    default boolean isAir() {
        return getType().isAir();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Override
    default boolean is(Object... objects) {
        return getType().is(objects);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @Override
    default boolean is(Object object) {
        return getType().is(object);
    }

    boolean isSimilar(Item item);

    @Contract(value = "_ -> new", pure = true)
    default Item withType(@NotNull ItemTypeHolder type) {
        return builder().type(type).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withAmount(int amount) {
        return builder().amount(amount).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withDisplayName(@Nullable Component displayName) {
        return builder().displayName(displayName).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withLore(@Nullable List<@NotNull Component> lore) {
        return builder().lore(lore).build().orElseThrow();
    }

    Item clone();
}
