package org.screamingsandals.lib.material.builder;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.attribute.AttributeMapping;
import org.screamingsandals.lib.material.meta.EnchantmentMapping;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.material.meta.PotionMapping;
import org.screamingsandals.lib.utils.AdventureHelper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ItemBuilder. Modifies or creates new Item. Simple, right?
 */
@RequiredArgsConstructor
public class ItemBuilder {
    @NotNull
    private final Item item;

    /**
     * Creates new ItemBuilder.
     *
     * @param material material to build item from
     */
    public ItemBuilder(MaterialHolder material) {
        this.item = new Item();
        item.setMaterial(material);
    }

    /**
     * Sets new type of the item
     *
     * @param type Anything that can be an Item. Name, ItemStack, serialized ItemStack and so on.
     * @return this item builder
     */
    public ItemBuilder type(@NotNull Object type) {
        ItemFactory.readShortStack(item, type);
        return this;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder name(@NotNull Component name) {
        item.setDisplayName(name);
        return this;
    }

    public ItemBuilder name(@Nullable String name) {
        if (name == null) {
            item.setDisplayName(null);
            return this;
        }
        return name(AdventureHelper.toComponent(name));
    }

    public ItemBuilder localizedName(@Nullable Component name) {
        item.setLocalizedName(name);
        return this;
    }

    public ItemBuilder localizedName(@Nullable String name) {
        if (name == null) {
            item.setLocalizedName(null);
            return this;
        }
        return localizedName(AdventureHelper.toComponent(name));
    }

    public ItemBuilder customModelData(Integer data) {
        item.setCustomModelData(data);
        return this;
    }

    public ItemBuilder repair(int repair) {
        item.setRepair(repair);
        return this;
    }

    public ItemBuilder flags(@Nullable List<Object> flags) {
        if (flags == null) {
            return this;
        } else {
            item.getItemFlags().addAll(flags.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList()));
        }
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        item.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder lore(@NotNull Component lore) {
        item.addLore(lore);
        return this;
    }

    public ItemBuilder lore(@Nullable String lore) {
        if (lore == null) {
            item.addLore(null);
            return this;
        }
        return lore(AdventureHelper.toComponent(lore));
    }

    public ItemBuilder lore(@NotNull List<Component> lore) {
        item.getLore().addAll(lore);
        return this;
    }

    public ItemBuilder enchant(@NotNull Object enchant, int level) {
        enchant(enchant + " " + level);
        return this;
    }

    public ItemBuilder enchant(@NotNull Map<Object, Integer> enchants) {
        enchants.forEach(this::enchant);
        return this;
    }

    public ItemBuilder enchant(@NotNull List<Object> enchants) {
        enchants.forEach(this::enchant);
        return this;
    }

    public ItemBuilder enchant(@NotNull Object enchant) {
        EnchantmentMapping.resolve(enchant).ifPresent(item::addEnchant);
        return this;
    }

    public ItemBuilder potion(@NotNull Object potion) {
        PotionMapping.resolve(potion).ifPresent(item::setPotion);
        return this;
    }

    public ItemBuilder attribute(@NotNull Object itemAttribute) {
        AttributeMapping.wrapItemAttribute(itemAttribute).ifPresent(item::addItemAttribute);
        return this;
    }

    public ItemBuilder effect(@NotNull Object effect) {
        if (effect instanceof List) {
            final var list = (List<?>) effect;
            list.forEach(effect1 -> PotionEffectMapping.resolve(effect1).ifPresent(item::addPotionEffect));
            return this;
        }

        PotionEffectMapping.resolve(effect).ifPresent(item::addPotionEffect);
        return this;
    }

    // For legacy versions
    @Deprecated
    public ItemBuilder damage(int damage) {
        return durability(damage);
    }

    // Or (durability is just alias for damage)
    public ItemBuilder durability(int durability) {
        item.setMaterial(item.getMaterial().newDurability(durability));
        return this;
    }

    public Optional<Item> build() {
        if (item.getMaterial() != null) {
            return Optional.of(item);
        }

        return Optional.empty();
    }
}
