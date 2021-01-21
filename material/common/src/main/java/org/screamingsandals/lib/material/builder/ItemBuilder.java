package org.screamingsandals.lib.material.builder;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.meta.EnchantmentMapping;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.material.meta.PotionMapping;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ItemBuilder {
    @NotNull
    private final Item item;

    public ItemBuilder(MaterialHolder material) {
        this.item = new Item();
        item.setMaterial(material);
    }

    public ItemBuilder type(@NotNull Object type) {
        ItemFactory.readShortStack(item, type);
        return this;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder name(@Nullable String name) {
        item.setDisplayName(name);
        return this;
    }

    public ItemBuilder localizedName(@Nullable String name) {
        item.setLocalizedName(name);
        return this;
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

    public ItemBuilder lore(@NotNull String lore) {
        item.addLore(lore);
        return this;
    }

    public ItemBuilder lore(@NotNull List<String> lore) {
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

    public ItemBuilder effect(@NotNull Object effect) {
        if (effect instanceof Map) {
            try {
                final var node = BasicConfigurationNode.root().set(effect);
                PotionEffectMapping.resolve(node).ifPresent(item::addPotionEffect);
                return this;
            } catch (SerializationException e) {
                e.printStackTrace();
            }
        }

        if (effect instanceof List) {
            final var list = (List<?>) effect;
            PotionEffectMapping.resolve(list).ifPresent(item::addPotionEffect);
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
