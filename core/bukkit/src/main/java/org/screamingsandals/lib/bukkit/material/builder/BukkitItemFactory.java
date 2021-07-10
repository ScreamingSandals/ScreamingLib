package org.screamingsandals.lib.bukkit.material.builder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.screamingsandals.lib.bukkit.material.BukkitMaterialMapping;
import org.screamingsandals.lib.bukkit.attribute.BukkitAttributeMapping;
import org.screamingsandals.lib.bukkit.attribute.BukkitItemAttribute;
import org.screamingsandals.lib.bukkit.container.BukkitContainer;
import org.screamingsandals.lib.bukkit.container.BukkitPlayerContainer;
import org.screamingsandals.lib.bukkit.firework.BukkitFireworkEffectMapping;
import org.screamingsandals.lib.bukkit.material.meta.BukkitEnchantmentMapping;
import org.screamingsandals.lib.bukkit.material.meta.BukkitPotionEffectMapping;
import org.screamingsandals.lib.bukkit.material.meta.BukkitPotionMapping;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.material.data.ItemData;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.InventoryType;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;
import org.screamingsandals.lib.utils.adventure.ComponentUtils;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.*;
import java.util.stream.Collectors;

@Service(dependsOn = {
        BukkitMaterialMapping.class,
        BukkitEnchantmentMapping.class,
        BukkitPotionMapping.class,
        BukkitPotionEffectMapping.class,
        BukkitAttributeMapping.class,
        BukkitFireworkEffectMapping.class
})
public class BukkitItemFactory extends ItemFactory {
    private final Plugin plugin;

    public static void init(Plugin plugin) {
        ItemFactory.init(() -> new BukkitItemFactory(plugin));
    }

    @SuppressWarnings({"unchecked", "deprecation"}) //cause we can
    public BukkitItemFactory(Plugin plugin) {
        this.plugin = plugin;

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
                            AdventureUtils
                                    .get(meta, "displayName", Component.class)
                                    .ifPresentOrElse(classMethod ->
                                                    classMethod.invokeInstance(meta, item.getDisplayName()),
                                            () -> meta.setDisplayName(AdventureHelper.toLegacy(item.getDisplayName())));
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
                            AdventureUtils
                                    .get(meta, "lore", List.class)
                                    .ifPresentOrElse(classMethod ->
                                                    classMethod.invokeInstance(meta, item.getLore()
                                                            .stream()
                                                            .map(ComponentUtils::componentToPlatform)
                                                            .collect(Collectors.toList()))
                                            , () ->
                                                    meta.setLore(item.getLore()
                                                            .stream()
                                                            .map(AdventureHelper::toLegacy)
                                                            .collect(Collectors.toList()))
                                    );
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
                            if (item.getColor() != null) {
                                ((PotionMeta) meta).setColor(Color.fromRGB(item.getColor().red(), item.getColor().green(), item.getColor().blue()));
                            }
                        }

                        if (Reflect.hasMethod(meta, "hasAttributeModifiers")) { // 1.13.1
                            item.getItemAttributes()
                                    .stream()
                                    .map(holder -> holder.as(BukkitItemAttribute.class))
                                    .forEach(holder -> meta.addAttributeModifier(holder.getAttribute(), holder.getAttributeModifier()));
                        }

                        if (Reflect.hasMethod(meta, "getPersistentDataContainer")) { // 1.14
                            var data = item.getData();
                            if (data instanceof BukkitItemData && !data.isEmpty()) {
                                var origDataContainer = ((BukkitItemData) data).getDataContainer();
                                Reflect.getMethod(meta.getPersistentDataContainer(), "putAll", Map.class)
                                        .invoke(Reflect.fastInvoke(origDataContainer, "getRaw"));
                            }
                        }

                        if (Reflect.has("org.bukkit.inventory.meta.KnowledgeBookMeta")) { // 1.12+
                            if (meta instanceof KnowledgeBookMeta) {
                                ((KnowledgeBookMeta) meta).setRecipes(item.getRecipes()
                                        .stream()
                                        .map(key -> NamespacedKey.fromString(key.toString()))
                                        .collect(Collectors.toList())
                                );
                            }
                        }

                        if (item.getColor() != null && meta instanceof LeatherArmorMeta) {
                            ((LeatherArmorMeta) meta).setColor(Color.fromRGB(item.getColor().red(), item.getColor().green(), item.getColor().blue()));
                        }

                        if (item.getSkullOwner() != null && meta instanceof SkullMeta) {
                            ((SkullMeta) meta).setOwner(item.getSkullOwner());
                        }

                        if (meta instanceof FireworkMeta) {
                            ((FireworkMeta) meta).setPower(item.getPower());
                            ((FireworkMeta) meta).addEffects(item.getFireworkEffects().stream().map(effect -> effect.as(FireworkEffect.class)).collect(Collectors.toList()));
                        }

                        if (meta instanceof FireworkEffectMeta && !item.getFireworkEffects().isEmpty()) {
                            ((FireworkEffectMeta) meta).setEffect(item.getFireworkEffects().stream().map(effect -> effect.as(FireworkEffect.class)).findFirst().orElseThrow());
                        }

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
                            AdventureUtils
                                    .get(meta, "displayName")
                                    .ifPresentOrElse(classMethod ->
                                                    item.setDisplayName(classMethod.invokeInstanceResulted(meta).as(Component.class)),
                                            () -> item.setDisplayName(AdventureHelper.toComponent(meta.getDisplayName())));
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
                        if (meta.hasLore()) {
                            AdventureUtils
                                    .get(meta, "lore")
                                    .ifPresentOrElse(classMethod ->
                                                    classMethod.invokeInstanceResulted(meta)
                                                            .as(List.class)
                                                            .stream()
                                                            .map(ComponentUtils::componentFromPlatform)
                                                            .forEach(l -> item.addLore((Component) l)), // wtf??
                                            () -> Objects.requireNonNull(meta.getLore())
                                                    .stream()
                                                    .map(AdventureHelper::toComponent)
                                                    .forEach(item::addLore)
                                    );
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
                                if (((PotionMeta) meta).getColor() != null) {
                                    var color = ((LeatherArmorMeta) meta).getColor();
                                    item.setColor(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
                                }
                                item.getPotionEffects().addAll(((PotionMeta) meta).getCustomEffects().stream()
                                        .map(PotionEffectMapping::resolve)
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .collect(Collectors.toList()));
                            } catch (Throwable ignored) {
                            }
                        }

                        if (Reflect.hasMethod(meta, "hasAttributeModifiers")) { // 1.13.1
                            if (meta.hasAttributeModifiers() && meta.getAttributeModifiers() != null) {
                                meta.getAttributeModifiers()
                                        .forEach((attribute, attributeModifier) ->
                                                AttributeMapping.wrapItemAttribute(new BukkitItemAttribute(attribute, attributeModifier))
                                                        .ifPresent(item::addItemAttribute)
                                        );
                            }
                        }

                        if (Reflect.has("org.bukkit.inventory.meta.KnowledgeBookMeta")) { // 1.12+
                            if (meta instanceof KnowledgeBookMeta) {
                                ((KnowledgeBookMeta) meta).getRecipes()
                                        .stream()
                                        .map(namespacedKey -> NamespacedMappingKey.of(namespacedKey.toString()))
                                        .forEach(item::addRecipe);
                            }
                        }

                        if (meta instanceof LeatherArmorMeta) {
                            var color = ((LeatherArmorMeta) meta).getColor();
                            if (!color.equals(Bukkit.getItemFactory().getDefaultLeatherColor())) {
                                item.setColor(TextColor.color(color.getRed(), color.getGreen(), color.getBlue()));
                            }
                        }

                        if (meta instanceof SkullMeta && ((SkullMeta) meta).hasOwner()) {
                            item.setSkullOwner(((SkullMeta) meta).getOwner());
                        }

                        if (meta instanceof FireworkMeta) {
                            item.setPower(((FireworkMeta) meta).getPower());
                            item.getFireworkEffects().addAll(((FireworkMeta) meta).getEffects()
                                    .stream()
                                    .map(FireworkEffectMapping::resolve)
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .collect(Collectors.toList())
                            );
                        }

                        if (meta instanceof FireworkEffectMeta && ((FireworkEffectMeta) meta).getEffect() != null) {
                            FireworkEffectMapping
                                    .resolve(((FireworkEffectMeta) meta).getEffect())
                                    .ifPresent(item.getFireworkEffects()::add);
                        }

                        if (Reflect.hasMethod(meta, "getPersistentDataContainer")) { // 1.14
                            item.setData(new BukkitItemData(plugin, meta.getPersistentDataContainer()));
                        }
                    }
                    return item;
                })
                .normalizeType(ItemStack.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends Container> Optional<C> wrapContainer0(Object container) {
        if (container instanceof PlayerInventory) {
            return (Optional<C>) Optional.of(new BukkitPlayerContainer((PlayerInventory) container));
        }

        if (container instanceof Inventory) {
            return (Optional<C>) Optional.of(new BukkitContainer((Inventory) container));
        }
        return Optional.empty();
    }

    @Override
    public <C extends Container> Optional<C> createContainer0(InventoryType type, Component name) {
        var container = AdventureUtils
                .get(Bukkit.getServer(), "createInventory", InventoryHolder.class, org.bukkit.event.inventory.InventoryType.class, Component.class)
                .ifPresentOrElseGet(classMethod ->
                                classMethod
                                        .invokeInstanceResulted(Bukkit.getServer(), null, org.bukkit.event.inventory.InventoryType.valueOf(type.name()), name)
                                        .as(Inventory.class),
                        () -> Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.valueOf(type.name()), AdventureHelper.toLegacy(name)));
        return wrapContainer0(container);
    }

    @Override
    public ItemData createNewItemData0() {
        var r = Reflect.constructor(ClassStorage.NMS.CraftPersistentDataContainer, ClassStorage.NMS.CraftPersistentDataTypeRegistry)
                .constructResulted(Reflect.getField(ClassStorage.NMS.CraftMetaItem, "DATA_TYPE_REGISTRY"));
        if (r.raw() != null) {
            return new BukkitItemData(plugin, (PersistentDataContainer) r.raw());
        }
        return ItemData.EMPTY;
    }
}
