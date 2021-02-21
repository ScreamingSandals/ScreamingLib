package org.screamingsandals.lib.bukkit.material.builder;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.screamingsandals.lib.bukkit.material.BukkitMaterialMapping;
import org.screamingsandals.lib.bukkit.material.attribute.BukkitAttributeMapping;
import org.screamingsandals.lib.bukkit.material.attribute.BukkitItemAttribute;
import org.screamingsandals.lib.bukkit.material.container.BukkitContainer;
import org.screamingsandals.lib.bukkit.material.meta.BukkitEnchantmentMapping;
import org.screamingsandals.lib.bukkit.material.meta.BukkitPotionEffectMapping;
import org.screamingsandals.lib.bukkit.material.meta.BukkitPotionMapping;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.attribute.AttributeMapping;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(dependsOn = {
        BukkitMaterialMapping.class,
        BukkitEnchantmentMapping.class,
        BukkitPotionMapping.class,
        BukkitPotionEffectMapping.class,
        BukkitAttributeMapping.class
})
public class BukkitItemFactory extends ItemFactory {
    public static void init() {
        ItemFactory.init(BukkitItemFactory::new);
    }

    @SuppressWarnings({"unchecked", "deprecation"}) //cause we can
    public BukkitItemFactory() {
        InitUtils.doIfNot(BukkitMaterialMapping::isInitialized, BukkitMaterialMapping::init);
        InitUtils.doIfNot(BukkitEnchantmentMapping::isInitialized, BukkitEnchantmentMapping::init);
        InitUtils.doIfNot(BukkitPotionMapping::isInitialized, BukkitPotionMapping::init);
        InitUtils.doIfNot(BukkitPotionEffectMapping::isInitialized, BukkitPotionEffectMapping::init);
        InitUtils.doIfNot(BukkitAttributeMapping::isInitialized, BukkitAttributeMapping::init);

        itemConverter
                .registerW2P(ItemStack.class, item -> {
                    ItemStack stack = item.getMaterial().as(ItemStack.class);
                    stack.setAmount(item.getAmount());
                    if (item.getPlatformMeta() != null) {
                        if (item.getPlatformMeta() instanceof ItemMeta) {
                            try {
                                stack.setItemMeta((ItemMeta) item.getPlatformMeta());
                            } catch (Throwable ignored) {
                            }
                        } else if (item.getPlatformMeta() instanceof Map) {
                            try {
                                stack.setItemMeta((ItemMeta) ConfigurationSerialization.deserializeObject((Map<String, ?>) item.getPlatformMeta()));
                            } catch (Throwable ignored) {
                            }
                        }
                    }

                    ItemMeta meta = stack.getItemMeta();

                    if (meta != null) {
                        if (item.getDisplayName() != null) {
                            meta.setDisplayName(AdventureHelper.toLegacy(item.getDisplayName()));
                        }
                        if (item.getLocalizedName() != null) {
                            try {
                                meta.setLocalizedName(AdventureHelper.toLegacy(item.getLocalizedName()));
                            } catch (Throwable ignored) {
                            }
                        }
                        try {
                            if (item.getCustomModelData() != null) {
                                meta.setCustomModelData(item.getCustomModelData());
                            }
                        } catch (Throwable ignored) {
                        }
                        if (meta instanceof Repairable) {
                            ((Repairable) meta).setRepairCost(item.getRepair());
                        }
                        if (Reflect.hasMethod(meta, "setUnbreakable", boolean.class)) {
                            meta.setUnbreakable(item.isUnbreakable());
                        } else {
                            var spigot = Reflect.fastInvoke(meta, "spigot");
                            if (spigot != null) {
                                Reflect.getMethod(spigot, "setUnbreakable", boolean.class).invoke(item.isUnbreakable());
                            }
                        }
                        if (!item.getLore().isEmpty()) {

                            meta.setLore(item.getLore()
                                    .stream()
                                    .map(AdventureHelper::toLegacy)
                                    .collect(Collectors.toList()));
                        }
                        item.getEnchantments().forEach(e -> {
                            if (meta instanceof EnchantmentStorageMeta) {
                                ((EnchantmentStorageMeta) meta).addStoredEnchant(e.as(Enchantment.class), e.getLevel(), true);
                            } else {
                                meta.addEnchant(e.as(Enchantment.class), e.getLevel(), true);
                            }
                        });
                        if (!item.getItemFlags().isEmpty()) {
                            try {
                                meta.addItemFlags(item.getItemFlags().stream().map(ItemFlag::valueOf).toArray(ItemFlag[]::new));
                            } catch (IllegalArgumentException ignored) {
                            }
                        }
                        if (meta instanceof PotionMeta) {
                            if (item.getPotion() != null) {
                                try {
                                    ((PotionMeta) meta).setBasePotionData(item.getPotion().as(PotionData.class));
                                } catch (Throwable ignored) {
                                }
                            }
                            if (!item.getPotionEffects().isEmpty()) {
                                item.getPotionEffects().forEach(potionEffectHolder -> ((PotionMeta) meta).addCustomEffect(potionEffectHolder.as(PotionEffect.class), true));
                            }
                        }

                        item.getItemAttributes()
                                .stream()
                                .map(holder -> holder.as(BukkitItemAttribute.class))
                                .forEach(holder -> meta.addAttributeModifier(holder.getAttribute(), holder.getAttributeModifier()));

                        stack.setItemMeta(meta);
                    }

                    return stack;
                })
                .registerP2W(ItemStack.class, stack -> {
                    Item item = new Item();
                    Optional<MaterialHolder> material = BukkitMaterialMapping.resolve(stack.getType());
                    if (material.isEmpty()) {
                        return null; // WHAT??
                    }
                    item.setMaterial(material.get().newDurability(stack.getDurability()));
                    item.setAmount(stack.getAmount());
                    ItemMeta meta = stack.getItemMeta();
                    item.setPlatformMeta(meta);
                    if (meta != null) {
                        if (meta.hasDisplayName()) {
                            item.setDisplayName(AdventureHelper.toComponent(meta.getDisplayName()));
                        }
                        try {
                            if (meta.hasLocalizedName()) {
                                item.setLocalizedName(AdventureHelper.toComponent(meta.getLocalizedName()));
                            }
                        } catch (Throwable ignored) {
                        }
                        try {
                            if (meta.hasCustomModelData()) {
                                item.setCustomModelData(meta.getCustomModelData());
                            }
                        } catch (Throwable ignored) {
                        }
                        if (meta instanceof Repairable) {
                            item.setRepair(((Repairable) meta).getRepairCost());
                        }
                        if (Reflect.hasMethod(meta, "isUnbreakable")) {
                            item.setUnbreakable(meta.isUnbreakable());
                        } else {
                            var spigot = Reflect.fastInvoke(meta, "spigot");
                            if (spigot != null) {
                                item.setUnbreakable((boolean) Reflect.fastInvoke(spigot, "isUnbreakable"));
                            }
                        }
                        if (meta.hasLore() && meta.getLore() != null) {
                            item.getLore().addAll(meta.getLore()
                                    .stream()
                                    .map(AdventureHelper::toComponent)
                                    .collect(Collectors.toList()));
                        }
                        if (meta instanceof EnchantmentStorageMeta) {
                            ((EnchantmentStorageMeta) meta).getStoredEnchants().entrySet().forEach(entry ->
                                    BukkitEnchantmentMapping.resolve(entry).ifPresent(item.getEnchantments()::add)
                            );
                        } else {
                            meta.getEnchants().entrySet().forEach(entry ->
                                    BukkitEnchantmentMapping.resolve(entry).ifPresent(item.getEnchantments()::add)
                            );
                        }
                        item.getItemFlags().addAll(meta.getItemFlags().stream().map(ItemFlag::name).collect(Collectors.toList()));

                        if (meta instanceof PotionMeta) {
                            try {
                                BukkitPotionMapping.resolve(((PotionMeta) meta).getBasePotionData()).ifPresent(item::setPotion);
                                item.getPotionEffects().addAll(((PotionMeta) meta).getCustomEffects().stream()
                                        .map(PotionEffectMapping::resolve)
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .collect(Collectors.toList()));
                            } catch (Throwable ignored) {
                            }
                        }

                        if (meta.hasAttributeModifiers() && meta.getAttributeModifiers() != null) {
                            meta.getAttributeModifiers()
                                    .forEach((attribute, attributeModifier) ->
                                            AttributeMapping.wrapItemAttribute(new BukkitItemAttribute(attribute, attributeModifier))
                                                    .ifPresent(item::addItemAttribute)
                                    );
                        }
                    }
                    return item;
                })
                .normalizeType(ItemStack.class);
    }

    @Override
    public Optional<Container> wrapContainer0(Object container) {
        if (container instanceof Inventory) {
            return Optional.of(new BukkitContainer((Inventory) container));
        }
        return Optional.empty();
    }
}
