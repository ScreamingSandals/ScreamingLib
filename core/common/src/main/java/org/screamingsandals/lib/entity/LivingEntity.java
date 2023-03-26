/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.attribute.AttributeHolder;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.world.Location;

import java.util.Collection;
import java.util.List;

public interface LivingEntity extends BasicEntity, ProjectileShooter {

    @Nullable AttributeHolder getAttribute(@NotNull AttributeType attributeType);

    double getEyeHeight();

    double getEyeHeight(boolean ignorePose);

    @NotNull Location getEyeLocation();

    @NotNull Block getTargetBlock(@Nullable Collection<@NotNull BlockTypeHolder> transparent, int maxDistance);

    @Nullable Block getTargetBlock(int maxDistance);

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

    @Nullable HumanEntity getHumanKiller();

    boolean addPotionEffect(@NotNull PotionEffectHolder effect);

    boolean addPotionEffects(@NotNull Collection<@NotNull PotionEffectHolder> effects);

    boolean hasPotionEffect(@NotNull PotionEffectHolder type);

    /* Currently removes all potion effects of this type, should be changed */
    void removePotionEffect(@NotNull PotionEffectHolder type);

    @NotNull List<@NotNull PotionEffectHolder> getActivePotionEffects();

    boolean getRemoveWhenFarAway();

    void setRemoveWhenFarAway(boolean remove);

    void setCanPickupItems(boolean pickup);

    boolean getCanPickupItems();

    boolean isLeashed();

    @Nullable BasicEntity getLeashHolder();

    boolean setLeashHolder(@Nullable BasicEntity holder);

    boolean removeLeashHolder();

    boolean isGliding();

    void setGliding(boolean gliding);

    boolean isSwimming();

    void setSwimming(boolean swimming);

    boolean isRiptiding();

    boolean isSleeping();

    void setAI(boolean ai);

    boolean hasAI();

    void attack(@NotNull BasicEntity target);

    void swingMainHand();

    void swingOffHand();

    void setCollidable(boolean collidable);

    boolean isCollidable();

    void setInvisible(boolean invisible);

    boolean isInvisible();

    void damage(double amount);

    void damage(double amount, @Nullable BasicEntity damageSource);

    double getAbsorptionAmount();

    double getHealth();

    void setAbsorptionAmount(double amount);

    void setHealth(double health);

    @Nullable ItemStack getHelmet();

    @Nullable ItemStack getChestplate();

    @Nullable ItemStack getLeggings();

    @Nullable ItemStack getBoots();

    void setHelmet(@Nullable ItemStack helmet);

    void setChestplate(@Nullable ItemStack chestplate);

    void setLeggings(@Nullable ItemStack leggings);

    void setBoots(@Nullable ItemStack boots);

    @Nullable ItemStack getItemInMainHand();

    void setItemInMainHand(@Nullable ItemStack item);

    @Nullable ItemStack getItemInOffHand();

    void setItemInOffHand(@Nullable ItemStack item);

    /**
     * Gets the entity's target.
     *
     * @return the player's target (the living entity the player is looking at)
     */
    default @Nullable LivingEntity getTarget() {
        return getTarget(3);
    }

    /**
     * Gets the entity's target.
     *
     * @param radius the max distance that the target can be detected from
     * @return the player's target (the living entity the player is looking at)
     */
    default @Nullable LivingEntity getTarget(int radius) {
        for (LivingEntity e : getLocation().getNearbyEntitiesByClass(LivingEntity.class, radius)) {
            final Location eye = getEyeLocation();
            final double dot = e.getLocation().asVector().subtract(eye.asVector()).normalize().dot(eye.getFacingDirection());
            if (dot > 0.99D) {
                return e;
            }
        }
        return null;
    }


}
