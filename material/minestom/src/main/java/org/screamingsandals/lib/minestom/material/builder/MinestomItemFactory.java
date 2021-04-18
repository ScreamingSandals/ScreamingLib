package org.screamingsandals.lib.minestom.material.builder;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.attribute.ItemAttribute;
import net.minestom.server.item.metadata.EnchantedBookMeta;
import net.minestom.server.item.metadata.ItemMeta;
import net.minestom.server.item.metadata.PotionMeta;
import net.minestom.server.potion.CustomPotionEffect;
import net.minestom.server.potion.PotionType;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.attribute.AttributeMapping;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.material.container.PlayerContainer;
import org.screamingsandals.lib.material.data.ItemData;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.minestom.material.MinestomMaterialMapping;
import org.screamingsandals.lib.minestom.material.attribute.MinestomAttributeMapping;
import org.screamingsandals.lib.minestom.material.container.MinestomContainer;
import org.screamingsandals.lib.minestom.material.meta.MinestomEnchantmentMapping;
import org.screamingsandals.lib.minestom.material.meta.MinestomPotionEffectMapping;
import org.screamingsandals.lib.minestom.material.meta.MinestomPotionMapping;
import org.screamingsandals.lib.minestom.utils.MinestomAdventureHelper;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(dependsOn = {
        MinestomMaterialMapping.class,
        MinestomEnchantmentMapping.class,
        MinestomPotionMapping.class,
        MinestomPotionEffectMapping.class,
        MinestomAttributeMapping.class
})
// TODO: Update Minestom and use new Item api
public class MinestomItemFactory extends ItemFactory {
    public static void init() {
        ItemFactory.init(MinestomItemFactory::new);
    }

    //we can use deprecation :)
    @SuppressWarnings("deprecation")
    public MinestomItemFactory() {
        InitUtils.doIfNot(MinestomMaterialMapping::isInitialized, MinestomMaterialMapping::init);
        InitUtils.doIfNot(MinestomEnchantmentMapping::isInitialized, MinestomEnchantmentMapping::init);
        InitUtils.doIfNot(MinestomPotionMapping::isInitialized, MinestomPotionMapping::init);
        InitUtils.doIfNot(MinestomPotionEffectMapping::isInitialized, MinestomPotionEffectMapping::init);
        InitUtils.doIfNot(MinestomAttributeMapping::isInitialized, MinestomAttributeMapping::init);

        itemConverter
                .registerW2P(ItemStack.class, item -> {
                    var stack = item.getMaterial().as(ItemStack.class);
                    stack.setAmount((byte) item.getAmount());
                    if (item.getPlatformMeta() != null) {
                        if (item.getPlatformMeta() instanceof ItemMeta) {
                            stack.setItemMeta((ItemMeta) item.getPlatformMeta());
                        }
                    }

                    if (item.getDisplayName() != null) {
                        stack.setDisplayName(MinestomAdventureHelper.toMinestom(item.getDisplayName()));
                    }
                    if (item.getLocalizedName() != null) {
                        // where is that?
                    }
                    if (item.getCustomModelData() != null) {
                        stack.setCustomModelData(item.getCustomModelData());
                    }
                    // repair
                    stack.setUnbreakable(item.isUnbreakable());
                    if (!item.getLore().isEmpty()) {
                        stack.setLore(item.getLore()
                                .stream()
                                .map(MinestomAdventureHelper::toMinestom)
                                .collect(Collectors.toCollection(ArrayList::new)));
                    }
                    item.getEnchantments().forEach(e -> {
                        if (stack.getItemMeta() instanceof EnchantedBookMeta) {
                            ((EnchantedBookMeta) stack.getItemMeta()).setStoredEnchantment(e.as(Enchantment.class), (short) e.getLevel());
                        } else {
                            stack.setEnchantment(e.as(Enchantment.class), (short) e.getLevel());
                        }
                    });
                    if (!item.getItemFlags().isEmpty()) {
                        try {
                            stack.addItemFlags(item.getItemFlags()
                                    .stream()
                                    .map(ItemFlag::valueOf)
                                    .toArray(ItemFlag[]::new));
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                    if (stack.getItemMeta() instanceof PotionMeta) {
                        if (item.getPotion() != null) {
                            ((PotionMeta) stack.getItemMeta()).setPotionType(item.getPotion().as(PotionType.class));
                        }
                        if (!item.getPotionEffects().isEmpty()) {
                            ((PotionMeta) stack.getItemMeta()).getCustomPotionEffects()
                                    .addAll(item.getPotionEffects()
                                            .stream()
                                            .map(effect -> effect.as(CustomPotionEffect.class))
                                            .collect(Collectors.toList()));
                        }
                    }
                    item.getItemAttributes()
                            .stream()
                            .map(attribute -> attribute.as(ItemAttribute.class))
                            .forEach(stack::addAttribute);

                    return stack;
                })
                .registerP2W(ItemStack.class, stack -> {
                    var item = new Item();
                    var material = MinestomMaterialMapping.resolve(stack.getMaterial());
                    if (material.isEmpty()) {
                        return null; // WHAT??
                    }

                    item.setMaterial(material.get().newDurability(stack.getDamage()));
                    item.setAmount(stack.getAmount());
                    if (stack.getItemMeta() != null) {
                        item.setPlatformMeta(stack.getItemMeta().clone());
                    }
                    if (stack.hasDisplayName()) {
                        item.setDisplayName(MinestomAdventureHelper.toComponent(Objects.requireNonNull(stack.getDisplayName())));
                    }
                    // localized name
                    item.setCustomModelData(stack.getCustomModelData());
                    // repair
                    item.setUnbreakable(stack.isUnbreakable());
                    if (stack.hasLore()) {
                        item.getLore().addAll(stack.getLore()
                                .stream()
                                .map(MinestomAdventureHelper::toComponent)
                                .collect(Collectors.toList()));
                    }
                    if (stack.getItemMeta() instanceof EnchantedBookMeta) {
                        ((EnchantedBookMeta) stack.getItemMeta()).getStoredEnchantmentMap().entrySet().stream().map(MinestomEnchantmentMapping::resolve).forEach(en ->
                                item.getEnchantments().add(en.orElseThrow())
                        );
                    } else {
                        stack.getEnchantmentMap().entrySet().stream().map(MinestomEnchantmentMapping::resolve).forEach(en ->
                                item.getEnchantments().add(en.orElseThrow())
                        );
                    }

                    item.getItemFlags().addAll(stack.getItemFlags()
                            .stream()
                            .map(ItemFlag::name)
                            .collect(Collectors.toList()));

                    if (stack.getItemMeta() instanceof PotionMeta) {
                        MinestomPotionMapping.resolve(((PotionMeta) stack.getItemMeta()).getPotionType()).ifPresent(item::setPotion);
                        item.getPotionEffects().addAll(((PotionMeta) stack.getItemMeta()).getCustomPotionEffects().stream()
                                .map(PotionEffectMapping::resolve)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(Collectors.toList()));
                    }

                    stack.getAttributes().stream()
                            .map(AttributeMapping::wrapItemAttribute)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(item::addItemAttribute);

                    item.setData(new MinestomItemData(stack));
                    return item;

                })
                .normalizeType(ItemStack.class);
    }

    @Override
    public Optional<PlayerContainer> wrapPlayerContainer0(Object container) {
            return Optional.empty();
    }

    @Override
    public Optional<Container> wrapContainer0(Object container) {
        if (container instanceof Inventory) {
            return Optional.of(new MinestomContainer((Inventory) container));
        }
        return Optional.empty();
    }

    @Override
    public ItemData createNewItemData0() {
        return ItemData.EMPTY;
    }
}
