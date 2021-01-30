package org.screamingsandals.lib.material;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.attribute.AttributeModifierHolder;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.meta.EnchantmentHolder;
import org.screamingsandals.lib.material.meta.PotionEffectHolder;
import org.screamingsandals.lib.material.meta.PotionHolder;
import org.screamingsandals.lib.utils.NormalizableWrapper;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Data
@ConfigSerializable
public class Item implements Cloneable, NormalizableWrapper<Item> {
    private final List<EnchantmentHolder> enchantments = new LinkedList<>();
    private final List<PotionEffectHolder> potionEffects = new LinkedList<>();
    private final List<String> lore = new LinkedList<>();
    private final List<String> itemFlags = new LinkedList<>();
    private final List<AttributeModifierHolder> attributeModifiers = new LinkedList<>();
    //@Nullable // in initial state it's null
    private MaterialHolder material;
    @Nullable
    private String displayName;
    @Nullable
    private String localizedName;
    private int amount = 1;
    private Integer customModelData;
    private int repair;
    private boolean unbreakable;
    @Nullable
    private PotionHolder potion;

    @Deprecated
    @Nullable
    private Object platformMeta;

    public <R> R as(Class<R> type) {
        return ItemFactory.convertItem(this, type);
    }

    @Override
    public Item normalize() {
        return ItemFactory.normalize(this);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Item clone() {
        Item item = new Item();
        item.getLore().addAll(lore);
        item.getItemFlags().addAll(itemFlags);
        item.getPotionEffects().addAll(potionEffects);
        item.getEnchantments().addAll(enchantments);
        item.getAttributeModifiers().addAll(attributeModifiers);
        item.setMaterial(material);
        item.setDisplayName(displayName);
        item.setLocalizedName(localizedName);
        item.setAmount(amount);
        item.setCustomModelData(customModelData);
        item.setRepair(repair);
        item.setUnbreakable(unbreakable);
        item.setPotion(potion);
        item.setPlatformMeta(platformMeta);
        return item;
    }

    public void addPotionEffect(PotionEffectHolder holder) {
        potionEffects.add(holder);
    }

    public void addAttributeModifier(AttributeModifierHolder holder) {
        attributeModifiers.add(holder);
    }

    public void addEnchant(EnchantmentHolder holder) {
        enchantments.add(holder);
    }

    public void addLore(String line) {
        lore.add(line);
    }

    public void addFlag(String flag) {
        lore.add(flag);
    }

    public boolean isSimilar(Item item) {
        if (item == null) {
            return false;
        }

        return Objects.equals(item.material, material)
                && Objects.equals(item.displayName, displayName)
                && Objects.equals(item.localizedName, localizedName)
                && Objects.equals(customModelData, item.customModelData)
                && repair == item.repair
                && unbreakable == item.unbreakable
                && lore.equals(item.lore)
                && enchantments.equals(item.enchantments)
                && itemFlags.equals(item.itemFlags)
                && Objects.equals(item.potion, potion)
                && potionEffects.equals(item.potionEffects)
                && attributeModifiers.equals(item.attributeModifiers);
    }
}
