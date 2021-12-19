package org.screamingsandals.lib.bukkit.item;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.bukkit.attribute.BukkitItemAttribute;
import org.screamingsandals.lib.bukkit.item.builder.BukkitItemBuilder;
import org.screamingsandals.lib.bukkit.item.data.BukkitItemDataCustomTags;
import org.screamingsandals.lib.bukkit.item.data.BukkitItemDataPersistentContainer;
import org.screamingsandals.lib.bukkit.item.data.CraftBukkitItemData;
import org.screamingsandals.lib.bukkit.item.meta.BukkitEnchantmentMapping;
import org.screamingsandals.lib.item.HideFlags;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemBuilder;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.nms.accessors.CompoundTagAccessor;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;
import org.screamingsandals.lib.utils.adventure.ComponentUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.*;
import java.util.stream.Collectors;

public class BukkitItem extends BasicWrapper<ItemStack> implements Item {
    public BukkitItem(ItemStack wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public ItemTypeHolder getType() {
        return ItemTypeHolder.of(wrappedObject.getType());
    }

    @Override
    public int getAmount() {
        return wrappedObject.getAmount();
    }

    @Override
    @Nullable
    public Component getDisplayName() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null && meta.hasDisplayName()) {
            return ComponentObjectLink.processGetter(meta, "displayName", meta::getDisplayName);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Component> getLore() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null && meta.hasLore()) {
            var list = new ArrayList<Component>();
            AdventureUtils
                    .get(meta, "lore")
                    .ifPresentOrElse(classMethod ->
                                    classMethod.invokeInstanceResulted(meta)
                                            .as(List.class)
                                            .stream()
                                            .map(ComponentUtils::componentFromPlatform)
                                            .forEach(o -> list.add((Component) o)),
                            () -> Objects.requireNonNull(meta.getLore())
                                    .stream()
                                    .map(AdventureHelper::toComponent)
                                    .forEach(list::add)
                    );
            return List.copyOf(list); // immutable
        }
        return List.of();
    }

    @Override
    public List<ItemAttributeHolder> getAttributeModifiers() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            // TODO: find solution: missing Bukkit API for older versions
            if (Reflect.hasMethod(meta, "hasAttributeModifiers")) { // 1.13.1
                if (meta.hasAttributeModifiers()) {
                    var list = new ArrayList<ItemAttributeHolder>();
                    meta.getAttributeModifiers()
                            .forEach((attribute, attributeModifier) ->
                                    AttributeMapping.wrapItemAttribute(new BukkitItemAttribute(attribute, attributeModifier))
                                            .ifPresent(list::add)
                            );
                    return list;
                }
            }
        }
        return List.of();
    }

    @Override
    public List<EnchantmentHolder> getEnchantments() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            var list = new ArrayList<EnchantmentHolder>();
            if (meta instanceof EnchantmentStorageMeta) {
                ((EnchantmentStorageMeta) meta).getStoredEnchants().entrySet().forEach(entry ->
                        EnchantmentHolder.ofOptional(entry).ifPresent(list::add)
                );
            } else {
                meta.getEnchants().entrySet().forEach(entry ->
                        EnchantmentHolder.ofOptional(entry).ifPresent(list::add)
                );
            }
            return list;
        }
        return List.of();
    }

    @Override
    public ItemData getData() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            if (Reflect.hasMethod(meta, "getPersistentDataContainer")) { // 1.14+
                return new BukkitItemDataPersistentContainer(BukkitCore.getPlugin(), meta.getPersistentDataContainer());
            } else if (Reflect.hasMethod(meta, "getCustomTagContainer")) { // 1.13.2
                return new BukkitItemDataCustomTags(BukkitCore.getPlugin(), meta.getCustomTagContainer());
            } else {
                var unhandled = (Map<String, Object>) Reflect.getField(meta, "unhandledTags");
                if (unhandled.containsKey("PublicBukkitValues")) {
                    // TODO: check if this rly copies the tags and not just reference
                    var compound = unhandled.get("PublicBukkitValues");
                    var nmap = new HashMap<String, Object>();
                    if (CompoundTagAccessor.getType().isInstance(compound)) {
                        var keys = (Set) Reflect.fastInvoke(compound, CompoundTagAccessor.getMethodGetAllKeys1());
                        for (var key : keys) {
                            nmap.put(key.toString(), Reflect.fastInvoke(compound, CompoundTagAccessor.getMethodGet1(), key));
                        }
                    }
                    return new CraftBukkitItemData(nmap);
                } else {
                    return new CraftBukkitItemData(new HashMap<>());
                }
            }
        }
        return null;
    }

    @Override
    public List<HideFlags> getHideFlags() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            return meta.getItemFlags().stream().map(ItemFlag::name).map(HideFlags::convert).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public Integer getCustomModelData() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            try {
                if (meta.hasCustomModelData()) {
                    return meta.getCustomModelData();
                }
            } catch (Throwable ignored) {}
        }
        return null;
    }

    @Override
    public boolean isUnbreakable() {
        var meta = wrappedObject.getItemMeta();
        if (meta != null) {
            if (Reflect.hasMethod(meta, "isUnbreakable")) {
                return meta.isUnbreakable();
            } else {
                var spigot = Reflect.fastInvoke(meta, "spigot");
                if (spigot != null) {
                    return (boolean) Reflect.fastInvoke(spigot, "isUnbreakable");
                }
            }
        }
        return false;
    }

    @Override
    public int getRepairCost() {
        var meta = wrappedObject.getItemMeta();
        if (meta instanceof Repairable) {
            return ((Repairable) meta).getRepairCost();
        }
        return 0;
    }

    @Override
    public ItemBuilder builder() {
        return new BukkitItemBuilder(wrappedObject.clone());
    }

    @Override
    public boolean isSimilar(Item item) {
        return wrappedObject.isSimilar(item.as(ItemStack.class));
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Item clone() {
        return new BukkitItem(wrappedObject.clone());
    }
}
