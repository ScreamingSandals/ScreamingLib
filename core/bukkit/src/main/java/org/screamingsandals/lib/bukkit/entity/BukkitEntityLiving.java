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

package org.screamingsandals.lib.bukkit.entity;

import lombok.experimental.ExtensionMethod;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.item.BukkitItem;
import org.screamingsandals.lib.entity.*;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.attribute.AttributeHolder;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitEntityLiving extends BukkitEntityBasic implements EntityLiving {
    public BukkitEntityLiving(@NotNull LivingEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable AttributeHolder getAttribute(@NotNull AttributeTypeHolder attributeType) {
        return AttributeMapping.wrapAttribute(((LivingEntity) wrappedObject).getAttribute(attributeType.as(Attribute.class)));
    }

    @Override
    public double getEyeHeight() {
        return ((LivingEntity) wrappedObject).getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return ((LivingEntity) wrappedObject).getEyeHeight(ignorePose);
    }

    @Override
    public @NotNull LocationHolder getEyeLocation() {
        return LocationMapper.wrapLocation(((LivingEntity) wrappedObject).getEyeLocation());
    }

    @Override
    public @NotNull BlockHolder getTargetBlock(@Nullable Collection<@NotNull BlockTypeHolder> transparent, int maxDistance) {
        return BlockMapper.wrapBlock(((LivingEntity) wrappedObject)
                .getTargetBlock((transparent == null) ? null : transparent
                        .stream()
                        .map(material -> material.as(Material.class))
                        .collect(Collectors.toSet()), maxDistance));
    }

    @Override
    public @Nullable BlockHolder getTargetBlock(int maxDistance) {
        return BlockMapper.resolve(((LivingEntity) wrappedObject)
                .getTargetBlockExact(maxDistance));
    }

    @Override
    public int getRemainingAir() {
        return ((LivingEntity) wrappedObject).getRemainingAir();
    }

    @Override
    public void setRemainingAir(int ticks) {
        ((LivingEntity) wrappedObject).setRemainingAir(ticks);
    }

    @Override
    public int getMaximumAir() {
        return ((LivingEntity) wrappedObject).getMaximumAir();
    }

    @Override
    public void setMaximumAir(int ticks) {
        ((LivingEntity) wrappedObject).setMaximumAir(ticks);
    }

    @Override
    public int getArrowCooldown() {
        return ((LivingEntity) wrappedObject).getArrowCooldown();
    }

    @Override
    public void setArrowCooldown(int ticks) {
        ((LivingEntity) wrappedObject).setArrowCooldown(ticks);
    }

    @Override
    public int getArrowsInBody() {
        return ((LivingEntity) wrappedObject).getArrowsInBody();
    }

    @Override
    public void setArrowsInBody(int count) {
        ((LivingEntity) wrappedObject).setArrowsInBody(count);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return ((LivingEntity) wrappedObject).getMaximumNoDamageTicks();
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        ((LivingEntity) wrappedObject).setMaximumNoDamageTicks(ticks);
    }

    @Override
    public double getLastDamage() {
        return ((LivingEntity) wrappedObject).getLastDamage();
    }

    @Override
    public void setLastDamage(double damage) {
        ((LivingEntity) wrappedObject).setLastDamage(damage);
    }

    @Override
    public int getNoDamageTicks() {
        return ((LivingEntity) wrappedObject).getNoDamageTicks();
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        ((LivingEntity) wrappedObject).setNoDamageTicks(ticks);
    }

    @Override
    public @Nullable EntityHuman getHumanKiller() {
        return EntityMapper.wrapEntityHuman(((LivingEntity) wrappedObject).getKiller());
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffectHolder effect) {
        return ((LivingEntity) wrappedObject).addPotionEffect(effect.as(PotionEffect.class));
    }

    @Override
    public boolean addPotionEffects(@NotNull Collection<@NotNull PotionEffectHolder> effects) {
        return ((LivingEntity) wrappedObject).addPotionEffects(effects
                .stream()
                .map(effect -> effect.as(PotionEffect.class))
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean hasPotionEffect(@NotNull PotionEffectHolder type) {
        return ((LivingEntity) wrappedObject).hasPotionEffect(type.as(PotionEffect.class).getType());
    }

    @Override
    public void removePotionEffect(@NotNull PotionEffectHolder type) {
        ((LivingEntity) wrappedObject).removePotionEffect(type.as(PotionEffect.class).getType());
    }

    @Override
    public @NotNull List<@NotNull PotionEffectHolder> getActivePotionEffects() {
        return ((LivingEntity) wrappedObject).getActivePotionEffects()
                .stream()
                .map(PotionEffectHolder::of)
                .collect(Collectors.toList());
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return ((LivingEntity) wrappedObject).getRemoveWhenFarAway();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        ((LivingEntity) wrappedObject).setRemoveWhenFarAway(remove);
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        ((LivingEntity) wrappedObject).setCanPickupItems(pickup);
    }

    @Override
    public boolean getCanPickupItems() {
        return ((LivingEntity) wrappedObject).getCanPickupItems();
    }

    @Override
    public boolean isLeashed() {
        return ((LivingEntity) wrappedObject).isLeashed();
    }

    @Override
    public @Nullable EntityBasic getLeashHolder() {
        if (isLeashed()) {
            return EntityMapper.wrapEntity(((LivingEntity) wrappedObject).getLeashHolder());
        }
        return null;
    }

    @Override
    public boolean setLeashHolder(@Nullable EntityBasic holder) {
        return ((LivingEntity) wrappedObject).setLeashHolder(holder != null ? holder.as(Entity.class) : null);
    }

    @Override
    public boolean removeLeashHolder() {
        return ((LivingEntity) wrappedObject).setLeashHolder(null);
    }

    @Override
    public boolean isGliding() {
        return ((LivingEntity) wrappedObject).isGliding();
    }

    @Override
    public void setGliding(boolean gliding) {
        ((LivingEntity) wrappedObject).setGliding(gliding);
    }

    @Override
    public boolean isSwimming() {
        return ((LivingEntity) wrappedObject).isSwimming();
    }

    @Override
    public void setSwimming(boolean swimming) {
        ((LivingEntity) wrappedObject).setSwimming(swimming);
    }

    @Override
    public boolean isRiptiding() {
        return ((LivingEntity) wrappedObject).isRiptiding();
    }

    @Override
    public boolean isSleeping() {
        return ((LivingEntity) wrappedObject).isSleeping();
    }

    @Override
    public void setAI(boolean ai) {
        ((LivingEntity) wrappedObject).setAI(ai);
    }

    @Override
    public boolean hasAI() {
        return ((LivingEntity) wrappedObject).hasAI();
    }

    @Override
    public void attack(@NotNull EntityBasic target) {
        ((LivingEntity) wrappedObject).attack(target.as(Entity.class));
    }

    @Override
    public void swingMainHand() {
        ((LivingEntity) wrappedObject).swingMainHand();
    }

    @Override
    public void swingOffHand() {
        ((LivingEntity) wrappedObject).swingOffHand();
    }

    @Override
    public void setCollidable(boolean collidable) {
        ((LivingEntity) wrappedObject).setCollidable(collidable);
    }

    @Override
    public boolean isCollidable() {
        return ((LivingEntity) wrappedObject).isCollidable();
    }

    @Override
    public void setInvisible(boolean invisible) {
        ((LivingEntity) wrappedObject).setInvisible(invisible);
    }

    @Override
    public boolean isInvisible() {
        return ((LivingEntity) wrappedObject).isInvisible();
    }

    @Override
    public void damage(double amount) {
        ((LivingEntity) wrappedObject).damage(amount);
    }

    @Override
    public void damage(double amount, @Nullable EntityBasic damageSource) {
        ((LivingEntity) wrappedObject).damage(amount, damageSource != null ? damageSource.as(Entity.class) : null);
    }

    @Override
    public double getAbsorptionAmount() {
        return ((LivingEntity) wrappedObject).getAbsorptionAmount();
    }

    @Override
    public double getHealth() {
        return ((LivingEntity) wrappedObject).getHealth();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        ((LivingEntity) wrappedObject).setAbsorptionAmount(amount);
    }

    @Override
    public void setHealth(double health) {
        ((LivingEntity) wrappedObject).setHealth(health);
    }

    @Override
    public @Nullable Item getHelmet() {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null || eq.getHelmet() == null) {
            return null;
        }

        return new BukkitItem(eq.getHelmet());
    }

    @Override
    public @Nullable Item getChestplate() {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null || eq.getChestplate() == null) {
            return null;
        }

        return new BukkitItem(eq.getChestplate());
    }

    @Override
    public @Nullable Item getLeggings() {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null || eq.getLeggings() == null) {
            return null;
        }

        return new BukkitItem(eq.getLeggings());
    }

    @Override
    public @Nullable Item getBoots() {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null || eq.getBoots() == null) {
            return null;
        }

        return new BukkitItem(eq.getBoots());
    }

    @Override
    public void setHelmet(@Nullable Item helmet) {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (helmet == null) {
            eq.setHelmet(null);
        } else {
            eq.setHelmet(helmet.as(ItemStack.class));
        }
    }

    @Override
    public void setChestplate(@Nullable Item chestplate) {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (chestplate == null) {
            eq.setChestplate(null);
        } else {
            eq.setChestplate(chestplate.as(ItemStack.class));
        }
    }

    @Override
    public void setLeggings(@Nullable Item leggings) {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (leggings == null) {
            eq.setLeggings(null);
        } else {
            eq.setLeggings(leggings.as(ItemStack.class));
        }
    }

    @Override
    public void setBoots(@Nullable Item boots) {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (boots == null) {
            eq.setBoots(null);
        } else {
            eq.setBoots(boots.as(ItemStack.class));
        }
    }

    @Override
    public @Nullable Item getItemInMainHand() {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return null;
        }

        return new BukkitItem(eq.getItemInMainHand());
    }

    @Override
    public void setItemInMainHand(@Nullable Item item) {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (item == null) {
            eq.setItemInMainHand(null);
        } else {
            eq.setItemInMainHand(item.as(ItemStack.class));
        }
    }

    @Override
    public @Nullable Item getItemInOffHand() {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return null;
        }

        return new BukkitItem(eq.getItemInOffHand());
    }

    @Override
    public void setItemInOffHand(@Nullable Item item) {
        var eq = ((LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (item == null) {
            eq.setItemInOffHand(null);
        } else {
            eq.setItemInOffHand(item.as(ItemStack.class));
        }
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public @Nullable EntityProjectile launchProjectile(@NotNull EntityTypeHolder projectileType) {
        var projectileBukkit = projectileType.as(EntityType.class).getEntityClass();
        if (!Projectile.class.isAssignableFrom(projectileBukkit)) {
            return null;
        }
        return EntityMapper.wrapEntityProjectile(((LivingEntity) wrappedObject).launchProjectile((Class<Projectile>) projectileBukkit));
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public @Nullable EntityProjectile launchProjectile(@NotNull EntityTypeHolder projectileType, @NotNull Vector3D velocity) {
        var projectileBukkit = projectileType.as(EntityType.class).getEntityClass();
        if (!Projectile.class.isAssignableFrom(projectileBukkit)) {
            return null;
        }
        return EntityMapper.wrapEntityProjectile(((LivingEntity) wrappedObject).launchProjectile((Class<Projectile>) projectileBukkit, new Vector(velocity.getX(), velocity.getY(), velocity.getZ())));
    }
}
