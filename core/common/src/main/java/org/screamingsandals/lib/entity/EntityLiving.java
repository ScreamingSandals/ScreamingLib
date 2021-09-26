package org.screamingsandals.lib.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.attribute.AttributeHolder;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EntityLiving extends EntityBasic, ProjectileShooter {

    Optional<AttributeHolder> getAttribute(AttributeTypeHolder attributeType);

    double getEyeHeight();

    double getEyeHeight(boolean ignorePose);

    LocationHolder getEyeLocation();

    BlockHolder getTargetBlock(Collection<BlockTypeHolder> transparent, int maxDistance);

    Optional<BlockHolder> getTargetBlock(int maxDistance);

    int getRemainingAir();

    void setRemainingAir(int ticks);

    int getMaximumAir();

    void setMaximumAir(int ticks);

    int getArrowCooldown();

    void setArrowCooldown(int ticks);

    int getArrowsInBody();

    void setArrowsInBody(int count);

    int getMaximumNoDamageTicks();

    void setMaximumNoDamageTicks(int ticks);

    double getLastDamage();

    void setLastDamage(double damage);

    int getNoDamageTicks();

    void setNoDamageTicks(int ticks);

    Optional<EntityLiving> getHumanKiller(); // change later to EntityHuman

    boolean addPotionEffect(PotionEffectHolder effect);

    boolean addPotionEffects(Collection<PotionEffectHolder> effects);

    boolean hasPotionEffect(PotionEffectHolder type);

    /* Currently removes all potion effects of this type, should be changed */
    void removePotionEffect(PotionEffectHolder type);

    List<PotionEffectHolder> getActivePotionEffects();

    boolean getRemoveWhenFarAway();

    void setRemoveWhenFarAway(boolean remove);

    void setCanPickupItems(boolean pickup);

    boolean getCanPickupItems();

    boolean isLeashed();

    Optional<EntityBasic> getLeashHolder();

    boolean setLeashHolder(EntityBasic holder);

    boolean removeLeashHolder();

    boolean isGliding();

    void setGliding(boolean gliding);

    boolean isSwimming();

    void setSwimming(boolean swimming);

    boolean isRiptiding();

    boolean isSleeping();

    void setAI(boolean ai);

    boolean hasAI();

    void attack(EntityBasic target);

    void swingMainHand();

    void swingOffHand();

    void setCollidable(boolean collidable);

    boolean isCollidable();

    void setInvisible(boolean invisible);

    boolean isInvisible();

    void damage(double amount);

    void damage(double amount, EntityBasic damageSource);

    double getAbsorptionAmount();

    double getHealth();

    void setAbsorptionAmount(double amount);

    void setHealth(double health);

    @Nullable
    Item getHelmet();

    @Nullable
    Item getChestplate();

    @Nullable
    Item getLeggings();

    @Nullable
    Item getBoots();

    void setHelmet(@Nullable Item helmet);

    void setChestplate(@Nullable Item chestplate);

    void setLeggings(@Nullable Item leggings);

    void setBoots(@Nullable Item boots);

    @Nullable
    Item getItemInMainHand();

    void setItemInMainHand(@Nullable Item item);

    @Nullable
    Item getItemInOffHand();

    void setItemInOffHand(@Nullable Item item);


}
