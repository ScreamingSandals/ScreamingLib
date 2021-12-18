package org.screamingsandals.lib.item.old;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.ItemAttributeHolder;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.item.data.ItemData;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.NormalizableWrapper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Data
@ConfigSerializable
public class Item implements Cloneable, NormalizableWrapper<Item>, ParticleData {
    private final List<EnchantmentHolder> enchantments = new LinkedList<>();
    /**
     * Item types: Potion, tipped arrow
     */
    private final List<PotionEffectHolder> potionEffects = new LinkedList<>();
    private final List<Component> lore = new LinkedList<>();
    private final List<String> itemFlags = new LinkedList<>();
    private final List<ItemAttributeHolder> itemAttributes = new LinkedList<>();
    /**
     * Item types: Knowledge book
     */
    private final List<NamespacedMappingKey> recipes = new LinkedList<>();
    /**
     * Item types: Firework rocket, Firework star
     *
     * NOTE: for Firework start only first effect is used
     */
    private final List<FireworkEffectHolder> fireworkEffects = new LinkedList<>();
    //@Nullable // in initial state its null
    private ItemTypeHolder material;
    @Nullable
    private Component displayName;
    @Nullable
    private Component localizedName;
    private int amount = 1;
    private Integer customModelData;
    private int repair;
    private boolean unbreakable;
    /**
     * Item types: Potion, tipped arrow
     */
    @Nullable
    private PotionHolder potion;
    @NotNull
    private ItemData data = ItemFactory.createNewItemData();
    /**
     * Item types: Leather armor, potion
     */
    @Nullable
    private RGBLike color;
    /**
     * Item types: Skull
     */
    @Nullable
    private String skullOwner;
    /**
     * Item types: Firework rocket
     */
    private int power;

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
        item.getItemAttributes().addAll(itemAttributes);
        item.getRecipes().addAll(recipes);
        item.getFireworkEffects().addAll(fireworkEffects);
        item.setMaterial(material);
        item.setDisplayName(displayName);
        item.setLocalizedName(localizedName);
        item.setAmount(amount);
        item.setCustomModelData(customModelData);
        item.setRepair(repair);
        item.setUnbreakable(unbreakable);
        item.setPotion(potion);
        item.setPlatformMeta(platformMeta);
        item.setColor(color);
        item.setSkullOwner(skullOwner);
        item.setPower(power);
        return item;
    }

    public void addPotionEffect(PotionEffectHolder holder) {
        potionEffects.add(holder);
    }

    public void addItemAttribute(ItemAttributeHolder holder) {
        itemAttributes.add(holder);
    }

    public void addEnchant(EnchantmentHolder holder) {
        enchantments.add(holder);
    }

    public void addLore(Component line) {
        lore.add(line);
    }

    public void addLore(String line) {
        lore.add(AdventureHelper.toComponent(line));
    }

    public void addFlag(String flag) {
        itemFlags.add(flag);
    }

    public void addRecipe(NamespacedMappingKey key) {
        recipes.add(key);
    }

    public void addFireworkEffect(FireworkEffectHolder effect) {
        fireworkEffects.add(effect);
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
                && itemAttributes.equals(item.itemAttributes)
                && recipes.equals(item.recipes)
                && Objects.equals(item.color, color)
                && Objects.equals(item.skullOwner, skullOwner)
                && fireworkEffects.equals(item.fireworkEffects)
                && item.power == power;
    }
}
