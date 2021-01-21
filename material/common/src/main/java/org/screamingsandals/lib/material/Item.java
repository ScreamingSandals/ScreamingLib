package org.screamingsandals.lib.material;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
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
    private List<EnchantmentHolder> enchantments = new LinkedList<>();
    private List<PotionEffectHolder> potionEffects = new LinkedList<>();
    private List<String> lore = new LinkedList<>();
    private List<String> itemFlags = new LinkedList<>();
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
        item.setLore(new LinkedList<>(!lore.isEmpty() ? lore : List.of()));
        item.setItemFlags(new LinkedList<>(!itemFlags.isEmpty() ? itemFlags : List.of()));
        item.setPotionEffects(new LinkedList<>(!potionEffects.isEmpty() ? potionEffects : List.of()));
        item.setEnchantments(new LinkedList<>(!enchantments.isEmpty() ? enchantments : List.of()));
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
                && Objects.equals(item.lore, lore)
                && enchantments.equals(item.enchantments)
                && Objects.equals(item.itemFlags, itemFlags)
                && Objects.equals(item.potion, potion)
                && Objects.equals(item.potionEffects, potionEffects);
    }
}
