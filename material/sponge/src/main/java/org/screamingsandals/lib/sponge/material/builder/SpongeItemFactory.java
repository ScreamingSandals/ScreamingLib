package org.screamingsandals.lib.sponge.material.builder;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.material.meta.PotionEffectMapping;
import org.screamingsandals.lib.material.meta.PotionMapping;
import org.screamingsandals.lib.sponge.material.SpongeMaterialMapping;
import org.screamingsandals.lib.sponge.material.container.SpongeContainer;
import org.screamingsandals.lib.sponge.material.meta.SpongeEnchantmentMapping;
import org.screamingsandals.lib.sponge.material.meta.SpongePotionEffectMapping;
import org.screamingsandals.lib.sponge.material.meta.SpongePotionMapping;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.AutoInitialization;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.potion.PotionType;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@AutoInitialization(platform = PlatformType.SPONGE, loadAfter = {
        SpongeMaterialMapping.class,
        SpongeEnchantmentMapping.class,
        SpongePotionMapping.class,
        SpongePotionEffectMapping.class
})
public class SpongeItemFactory extends ItemFactory {

    public static void init() {
        ItemFactory.init(SpongeItemFactory::new);
    }

    public SpongeItemFactory() {
        InitUtils.doIfNot(SpongeMaterialMapping::isInitialized, SpongeMaterialMapping::init);
        InitUtils.doIfNot(SpongeEnchantmentMapping::isInitialized, SpongeEnchantmentMapping::init);
        InitUtils.doIfNot(SpongePotionMapping::isInitialized, SpongePotionMapping::init);
        InitUtils.doIfNot(SpongePotionEffectMapping::isInitialized, SpongePotionEffectMapping::init);

        itemConverter
                .registerW2P(ItemStack.class, item -> {
                    var stack = item.getMaterial().as(ItemStack.class);
                    stack.setQuantity(item.getAmount());
                    /*if (item.getPlatformMeta() != null) {
                        if (item.getPlatformMeta() instanceof ItemMeta) {
                            stack.setItemMeta((ItemMeta) item.getPlatformMeta());
                        }
                    }*/

                    if (item.getDisplayName() != null) {
                        stack.offer(Keys.DISPLAY_NAME, LegacyComponentSerializer.legacySection().deserialize(item.getDisplayName()));
                    }
                    //if (item.getLocalizedName() != null) {
                        // where is that?
                    //}
                    if (item.getCustomModelData() != null) {
                        stack.offer(Keys.CUSTOM_MODEL_DATA, item.getCustomModelData());
                    }
                    // repair
                    stack.offer(Keys.IS_UNBREAKABLE, item.isUnbreakable());
                    if (!item.getLore().isEmpty()) {
                        stack.offer(Keys.LORE, item.getLore().stream().map(LegacyComponentSerializer.legacySection()::deserialize).collect(Collectors.toList()));
                    }
                    if (stack.supports(Keys.STORED_ENCHANTMENTS)) {
                        stack.offer(Keys.STORED_ENCHANTMENTS, item.getEnchantments().stream().map(holder -> holder.as(Enchantment.class)).collect(Collectors.toList()));
                    } else {
                        stack.offer(Keys.APPLIED_ENCHANTMENTS, item.getEnchantments().stream().map(holder -> holder.as(Enchantment.class)).collect(Collectors.toList()));
                    }
                    if (item.getPotion() != null && stack.supports(Keys.POTION_TYPE)) {
                        stack.offer(Keys.POTION_TYPE, item.getPotion().as(PotionType.class));
                    }
                    if (!item.getPotionEffects().isEmpty() && stack.supports(Keys.POTION_EFFECTS)) {
                        stack.offer(Keys.POTION_EFFECTS, item.getPotionEffects()
                                .stream().map(holder -> holder.as(PotionEffect.class)).collect(Collectors.toList()));
                    }
                    if (!item.getItemFlags().isEmpty()) {
                        stack.offer(Keys.HIDE_ATTRIBUTES, item.getItemFlags().contains("HIDE_ATTRIBUTES"));
                        stack.offer(Keys.HIDE_CAN_DESTROY, item.getItemFlags().contains("HIDE_DESTROYS"));
                        // HIDE_DYE
                        stack.offer(Keys.HIDE_ENCHANTMENTS, item.getItemFlags().contains("HIDE_ENCHANTS"));
                        stack.offer(Keys.HIDE_CAN_PLACE, item.getItemFlags().contains("HIDE_PLACED_ON"));
                        stack.offer(Keys.HIDE_MISCELLANEOUS, item.getItemFlags().contains("HIDE_POTION_EFFECTS"));
                        stack.offer(Keys.HIDE_UNBREAKABLE, item.getItemFlags().contains("HIDE_UNBREAKABLE"));
                    }

                    return stack;
                })
                .registerP2W(ItemStack.class, stack -> {
                    var item = new Item();
                    var material = SpongeMaterialMapping.resolve(stack);
                    if (material.isEmpty()) {
                        return null; // WHAT??
                    }

                    item.setMaterial(material.get());
                    item.setAmount(stack.getQuantity());
                    //if (stack.getItemMeta() != null) {
                    //    item.setPlatformMeta(stack.getItemMeta().clone());
                    //}
                    stack.get(Keys.DISPLAY_NAME).ifPresent(component ->
                            item.setDisplayName(LegacyComponentSerializer.legacySection().serialize(component))
                    );
                    // localized name
                    stack.get(Keys.CUSTOM_MODEL_DATA).ifPresent(item::setCustomModelData);
                    // repair
                    stack.get(Keys.IS_UNBREAKABLE).ifPresent(item::setUnbreakable);
                    stack.get(Keys.LORE).ifPresent(components ->
                            item.getLore().addAll(components.stream().map(LegacyComponentSerializer.legacySection()::serialize).collect(Collectors.toList()))
                    );
                    if (stack.supports(Keys.STORED_ENCHANTMENTS)) {
                        stack.get(Keys.STORED_ENCHANTMENTS).ifPresent(enchantments ->
                                enchantments.stream().map(SpongeEnchantmentMapping::resolve).forEach(en ->
                                        item.getEnchantments().add(en.orElseThrow())
                                )
                        );
                    } else {
                        stack.get(Keys.APPLIED_ENCHANTMENTS).ifPresent(enchantments ->
                                enchantments.stream().map(SpongeEnchantmentMapping::resolve).forEach(en ->
                                        item.getEnchantments().add(en.orElseThrow())
                                )
                        );
                    }
                    stack.get(Keys.HIDE_ATTRIBUTES).ifPresent(aBoolean -> {
                        if (aBoolean) {
                            item.getItemFlags().add("HIDE_ATTRIBUTES");
                        }
                    });
                    stack.get(Keys.HIDE_CAN_DESTROY).ifPresent(aBoolean -> {
                        if (aBoolean) {
                            item.getItemFlags().add("HIDE_DESTROYS");
                        }
                    });
                    /*stack.get(Keys.HIDE_DYE).ifPresent(aBoolean -> {
                        item.getItemFlags().add("HIDE_DYE");
                    });*/
                    stack.get(Keys.HIDE_ENCHANTMENTS).ifPresent(aBoolean -> {
                    });
                    stack.get(Keys.HIDE_CAN_PLACE).ifPresent(aBoolean -> {
                        if (aBoolean) {
                            item.getItemFlags().add("HIDE_PLACED_ON");
                        }
                    });
                    stack.get(Keys.HIDE_MISCELLANEOUS).ifPresent(aBoolean -> {
                        if (aBoolean) {
                            item.getItemFlags().add("HIDE_POTION_EFFECTS");
                        }
                    });
                    stack.get(Keys.HIDE_UNBREAKABLE).ifPresent(aBoolean -> {
                        if (aBoolean) {
                            item.getItemFlags().add("HIDE_UNBREAKABLE");
                        }
                    });
                    stack.get(Keys.POTION_TYPE).flatMap(PotionMapping::resolve).ifPresent(item::setPotion);
                    stack.get(Keys.POTION_EFFECTS).ifPresent(potionEffects ->
                        item.getPotionEffects().addAll(potionEffects.stream()
                                .map(PotionEffectMapping::resolve)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(Collectors.toList()))
                    );

                    return item;

                })
                .normalizeType(ItemStack.class);
    }

    @Override
    public Optional<Container> wrapContainer0(Object container) {
        if (container instanceof Inventory) {
            return Optional.of(new SpongeContainer((Inventory) container));
        }
        return Optional.empty();
    }
}
