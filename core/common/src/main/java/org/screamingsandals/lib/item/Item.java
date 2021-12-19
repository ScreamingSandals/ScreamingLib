package org.screamingsandals.lib.item;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.stream.Collectors;

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

    List<ItemAttributeHolder> getAttributeModifiers();

    default List<ItemAttributeHolder> getItemAttributes() { // alternative getter (old name)
        return getAttributeModifiers();
    }

    List<EnchantmentHolder> getEnchantments();

    ItemData getData();

    List<HideFlags> getHideFlags();

    @Deprecated
    default List<String> getItemFlags() {
        return getHideFlags().stream().map(HideFlags::getBukkitName).collect(Collectors.toList());
    }

    Integer getCustomModelData();

    boolean isUnbreakable();

    int getRepairCost();

    default int getRepair() { // alternative getter (old name)
        return getRepairCost();
    }

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

    @Contract(value = "_ -> new", pure = true)
    default Item withAttributeModifiers(@Nullable List<@NotNull ItemAttributeHolder> modifiers) {
        return builder().attributeModifiers(modifiers).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withAttributeModifier(@NotNull ItemAttributeHolder modifier) {
        return builder().attributeModifier(modifier).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withData(@NotNull ItemData data) {
        return builder().data(data).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withHideFlags(@Nullable List<@NotNull HideFlags> flags) {
        return builder().hideFlags(flags).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withHideFlag(@NotNull HideFlags flag) {
        return builder().hideFlag(flag).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withEnchantments(@Nullable List<@NotNull EnchantmentHolder> enchantments) {
        return builder().enchantments(enchantments).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withEnchantment(@NotNull EnchantmentHolder enchantment) {
        return builder().enchantment(enchantment).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withCustomModelData(@Nullable Integer customModelData) {
        return builder().customModelData(customModelData).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withUnbreakable(boolean unbreakable) {
        return builder().unbreakable(unbreakable).build().orElseThrow();
    }

    @Contract(value = "_ -> new", pure = true)
    default Item withRepairCost(int repairCost) {
        return builder().repairCost(repairCost).build().orElseThrow();
    }

    Item clone();
}
