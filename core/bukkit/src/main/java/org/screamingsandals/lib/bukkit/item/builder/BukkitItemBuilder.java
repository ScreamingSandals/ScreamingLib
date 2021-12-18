package org.screamingsandals.lib.bukkit.item.builder;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.BukkitItemBlockIdsRemapper;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;
import org.screamingsandals.lib.utils.adventure.ComponentUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BukkitItemBuilder implements ItemBuilder {
    private ItemStack item;

    @Override
    public ItemBuilder type(@NotNull ItemTypeHolder type) {
        if (item == null) {
            item = new ItemStack(Material.AIR);
        }

        this.item.setType(type.as(Material.class));
        if (BukkitItemBlockIdsRemapper.getBPlatform() == Platform.JAVA_LEGACY) {
            this.item.setDurability(type.durability());
        } else {
            var meta = item.getItemMeta();
            if (meta instanceof Damageable) {
                ((Damageable) meta).setDamage(type.durability());
                item.setItemMeta(meta);
            }
        }
        return this;
    }

    @Override
    public ItemBuilder amount(int amount) {
        if (item == null) {
            return this;
        }
        item.setAmount(amount);
        return this;
    }

    @Override
    public ItemBuilder displayName(@Nullable Component displayName) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            ComponentObjectLink.processSetter(meta, "displayName", meta::setDisplayName, displayName);
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public ItemBuilder lore(@Nullable List<@NotNull Component> lore) {
        if (item == null) {
            return this;
        }
        var meta = item.getItemMeta();
        if (meta != null) {
            if (lore == null) {
                meta.setLore(null);
            } else {
                AdventureUtils
                        .get(meta, "lore", List.class)
                        .ifPresentOrElse(classMethod ->
                                        classMethod.invokeInstance(meta, lore
                                                .stream()
                                                .map(ComponentUtils::componentToPlatform)
                                                .collect(Collectors.toList()))
                                , () ->
                                        meta.setLore(lore
                                                .stream()
                                                .map(AdventureHelper::toLegacy)
                                                .collect(Collectors.toList()))
                        );
            }
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public Optional<Item> build() {
        if (item != null) {
            return Optional.of(new BukkitItem(item.clone()));
        } else {
            return Optional.empty();
        }
    }
}
