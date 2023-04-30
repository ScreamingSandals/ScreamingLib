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

package org.screamingsandals.lib.impl.bukkit.entity;

import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.attribute.BukkitAttribute1_8;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockPlacement;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItem;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.entity.*;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.attribute.Attribute;
import org.screamingsandals.lib.impl.attribute.Attributes;
import org.screamingsandals.lib.attribute.AttributeType;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.lib.item.meta.PotionEffect;
import org.screamingsandals.lib.nms.accessors.LivingEntityAccessor;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.block.BlockPlacement;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.impl.world.Locations;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitLivingEntity extends BukkitEntity implements LivingEntity {
    public BukkitLivingEntity(@NotNull org.bukkit.entity.LivingEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable Attribute getAttribute(@NotNull AttributeType attributeType) {
        if (BukkitFeature.ATTRIBUTES_API.isSupported()) {
            return Attributes.wrapAttribute(((org.bukkit.entity.LivingEntity) wrappedObject).getAttribute(attributeType.as(org.bukkit.attribute.Attribute.class)));
        } else if (attributeType instanceof BukkitAttribute1_8) {
            return Attributes.wrapAttribute(Reflect.fastInvoke(ClassStorage.getHandle(wrappedObject), LivingEntityAccessor.getMethodGetAttributeInstance1(), attributeType.raw()));
        }
        return null;
    }

    @Override
    public double getEyeHeight() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getEyeHeight(ignorePose);
    }

    @Override
    public @NotNull Location getEyeLocation() {
        return Locations.wrapLocation(((org.bukkit.entity.LivingEntity) wrappedObject).getEyeLocation());
    }

    @Override
    public @NotNull BlockPlacement getTargetBlock(@Nullable Collection<@NotNull Block> transparent, int maxDistance) {
        return new BukkitBlockPlacement(((org.bukkit.entity.LivingEntity) wrappedObject)
                .getTargetBlock((transparent == null) ? null : transparent
                        .stream()
                        .map(material -> material.as(Material.class))
                        .collect(Collectors.toSet()), maxDistance));
    }

    @Override
    public @Nullable BlockPlacement getTargetBlock(int maxDistance) {
        var targetBlock = ((org.bukkit.entity.LivingEntity) wrappedObject).getTargetBlockExact(maxDistance);
        return targetBlock != null ? new BukkitBlockPlacement(targetBlock) : null;
    }

    @Override
    public int getRemainingAir() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getRemainingAir();
    }

    @Override
    public void setRemainingAir(int ticks) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setRemainingAir(ticks);
    }

    @Override
    public int getMaximumAir() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getMaximumAir();
    }

    @Override
    public void setMaximumAir(int ticks) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setMaximumAir(ticks);
    }

    @Override
    public int getArrowCooldown() {
        if (BukkitFeature.ARROW_COOLDOWN.isSupported()) {
            return ((org.bukkit.entity.LivingEntity) wrappedObject).getArrowCooldown();
        } else {
            // TODO: <= 1.16.2
            return 0;
        }
    }

    @Override
    public void setArrowCooldown(int ticks) {
        if (BukkitFeature.ARROW_COOLDOWN.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).setArrowCooldown(ticks);
        } else  {
            // TODO: <= 1.16.2
        }
    }

    @Override
    public int getArrowsInBody() {
        if (BukkitFeature.ARROWS_IN_BODY.isSupported()) {
            return ((org.bukkit.entity.LivingEntity) wrappedObject).getArrowsInBody();
        } else  {
            // TODO: <= 1.16.2
            return 0;
        }
    }

    @Override
    public void setArrowsInBody(int count) {
        if (BukkitFeature.ARROWS_IN_BODY.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).setArrowsInBody(count);
        } else {
            // TODO: <= 1.16.2
        }
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getMaximumNoDamageTicks();
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setMaximumNoDamageTicks(ticks);
    }

    @Override
    public double getLastDamage() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getLastDamage();
    }

    @Override
    public void setLastDamage(double damage) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setLastDamage(damage);
    }

    @Override
    public int getNoDamageTicks() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getNoDamageTicks();
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setNoDamageTicks(ticks);
    }

    @Override
    public @Nullable HumanEntity getHumanKiller() {
        return Entities.wrapEntityHuman(((org.bukkit.entity.LivingEntity) wrappedObject).getKiller());
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffect effect) {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).addPotionEffect(effect.as(org.bukkit.potion.PotionEffect.class));
    }

    @Override
    public boolean addPotionEffects(@NotNull Collection<@NotNull PotionEffect> effects) {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).addPotionEffects(effects
                .stream()
                .map(effect -> effect.as(org.bukkit.potion.PotionEffect.class))
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean hasPotionEffect(@NotNull PotionEffect type) {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).hasPotionEffect(type.as(org.bukkit.potion.PotionEffect.class).getType());
    }

    @Override
    public void removePotionEffect(@NotNull PotionEffect type) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).removePotionEffect(type.as(org.bukkit.potion.PotionEffect.class).getType());
    }

    @Override
    public @NotNull List<@NotNull PotionEffect> getActivePotionEffects() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getActivePotionEffects()
                .stream()
                .map(PotionEffect::of)
                .collect(Collectors.toList());
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getRemoveWhenFarAway();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setRemoveWhenFarAway(remove);
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setCanPickupItems(pickup);
    }

    @Override
    public boolean getCanPickupItems() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getCanPickupItems();
    }

    @Override
    public boolean isLeashed() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).isLeashed();
    }

    @Override
    public @Nullable Entity getLeashHolder() {
        if (isLeashed()) {
            return Entities.wrapEntity(((org.bukkit.entity.LivingEntity) wrappedObject).getLeashHolder());
        }
        return null;
    }

    @Override
    public boolean setLeashHolder(@Nullable Entity holder) {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).setLeashHolder(holder != null ? holder.as(org.bukkit.entity.Entity.class) : null);
    }

    @Override
    public boolean removeLeashHolder() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).setLeashHolder(null);
    }

    @Override
    public boolean isGliding() {
        if (BukkitFeature.GLIDING.isSupported()) {
            return ((org.bukkit.entity.LivingEntity) wrappedObject).isGliding();
        } else {
            return false; // 1.8.8: no Elytra
        }
    }

    @Override
    public void setGliding(boolean gliding) {
        if (BukkitFeature.GLIDING.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).setGliding(gliding);
        } else {
            // 1.8.8: no Elytra
        }
    }

    @Override
    public boolean isSwimming() {
        if (BukkitFeature.SWIMMING.isSupported()) {
            return ((org.bukkit.entity.LivingEntity) wrappedObject).isSwimming();
        } else {
            return false; // <= 1.12.2: no swimming animation
        }
    }

    @Override
    public void setSwimming(boolean swimming) {
        if (BukkitFeature.SWIMMING.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).setSwimming(swimming);
        } else {
            // <= 1.12.2: no swimming animation
        }
    }

    @Override
    public boolean isRiptiding() {
        if (BukkitFeature.RIPTIDING.isSupported()) {
            return ((org.bukkit.entity.LivingEntity) wrappedObject).isRiptiding();
        } else {
            return false; // <= 1.12.2: not supported
        }
    }

    @Override
    public boolean isSleeping() {
        if (BukkitFeature.SLEEPING.isSupported()) {
            return ((org.bukkit.entity.LivingEntity) wrappedObject).isSleeping();
        } else {
            // <= 1.13.2: Only HumanEntity (Player) can sleep
            if (wrappedObject instanceof org.bukkit.entity.HumanEntity) {
                return ((org.bukkit.entity.HumanEntity) wrappedObject).isSleeping();
            } else {
                return false;
            }
        }
    }

    @Override
    public void setAI(boolean ai) {
        if (BukkitFeature.SET_AI.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).setAI(ai);
        } else {
            // TODO: 1.8.8
        }
    }

    @Override
    public boolean hasAI() {
        if (BukkitFeature.SET_AI.isSupported()) {
            return ((org.bukkit.entity.LivingEntity) wrappedObject).hasAI();
        } else {
            return true; // TODO: 1.8.8: who knows
        }
    }

    @Override
    public void attack(@NotNull Entity target) {
        if (BukkitFeature.ATTACK.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).attack(target.as(org.bukkit.entity.Entity.class));
        } else {
            // TODO: <= 1.15.2
        }
    }

    @Override
    public void swingMainHand() {
        if (BukkitFeature.SWING_HAND.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).swingMainHand();
        } else {
            // TODO: <= 1.15.2
        }
    }

    @Override
    public void swingOffHand() {
        if (BukkitFeature.SWING_HAND.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).swingOffHand();
        } else {
            // TODO: <= 1.15.2
        }
    }

    @Override
    public void setCollidable(boolean collidable) {
        if (BukkitFeature.COLLIDABLE.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).setCollidable(collidable);
        } else {
            // 1.8.8: Collisions can't be controlled
        }
    }

    @Override
    public boolean isCollidable() {
        if (BukkitFeature.COLLIDABLE.isSupported()) {
            return ((org.bukkit.entity.LivingEntity) wrappedObject).isCollidable();
        } else {
            // TODO: 1.8.8: how am I supposed to figure this out? Players don't have collisions, but some living entities do
            return !(wrappedObject instanceof org.bukkit.entity.HumanEntity);
        }
    }

    @Override
    public void setInvisible(boolean invisible) {
        if (BukkitFeature.INVISIBLE.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).setInvisible(invisible);
        } else {
            if (wrappedObject instanceof ArmorStand) {
                ((ArmorStand) wrappedObject).setVisible(!invisible);
            }
            // TODO: <= 1.16.3
        }
    }

    @Override
    public boolean isInvisible() {
        if (BukkitFeature.INVISIBLE.isSupported()) {
            return ((org.bukkit.entity.LivingEntity) wrappedObject).isInvisible();
        } else {
            if (wrappedObject instanceof ArmorStand) {
                return !((ArmorStand) wrappedObject).isVisible();
            }
            return false; // TODO: <= 1.16.3
        }
    }

    @Override
    public void damage(double amount) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).damage(amount);
    }

    @Override
    public void damage(double amount, @Nullable Entity damageSource) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).damage(amount, damageSource != null ? damageSource.as(org.bukkit.entity.Entity.class) : null);
    }

    @Override
    public double getAbsorptionAmount() {
        if (BukkitFeature.ABSORPTION_AMOUNT.isSupported()) {
            return ((org.bukkit.entity.LivingEntity) wrappedObject).getAbsorptionAmount();
        } else {
            return 0; // TODO: <= 1.14.4
        }
    }

    @Override
    public double getHealth() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getHealth();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        if (BukkitFeature.ABSORPTION_AMOUNT.isSupported()) {
            ((org.bukkit.entity.LivingEntity) wrappedObject).setAbsorptionAmount(amount);
        } else {
            // TODO: <= 1.14.4
        }
    }

    @Override
    public void setHealth(double health) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setHealth(health);
    }

    @Override
    public @Nullable ItemStack getHelmet() {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null || eq.getHelmet() == null) {
            return null;
        }

        return new BukkitItem(eq.getHelmet());
    }

    @Override
    public @Nullable ItemStack getChestplate() {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null || eq.getChestplate() == null) {
            return null;
        }

        return new BukkitItem(eq.getChestplate());
    }

    @Override
    public @Nullable ItemStack getLeggings() {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null || eq.getLeggings() == null) {
            return null;
        }

        return new BukkitItem(eq.getLeggings());
    }

    @Override
    public @Nullable ItemStack getBoots() {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null || eq.getBoots() == null) {
            return null;
        }

        return new BukkitItem(eq.getBoots());
    }

    @Override
    public void setHelmet(@Nullable ItemStack helmet) {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (helmet == null) {
            eq.setHelmet(null);
        } else {
            eq.setHelmet(helmet.as(org.bukkit.inventory.ItemStack.class));
        }
    }

    @Override
    public void setChestplate(@Nullable ItemStack chestplate) {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (chestplate == null) {
            eq.setChestplate(null);
        } else {
            eq.setChestplate(chestplate.as(org.bukkit.inventory.ItemStack.class));
        }
    }

    @Override
    public void setLeggings(@Nullable ItemStack leggings) {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (leggings == null) {
            eq.setLeggings(null);
        } else {
            eq.setLeggings(leggings.as(org.bukkit.inventory.ItemStack.class));
        }
    }

    @Override
    public void setBoots(@Nullable ItemStack boots) {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (boots == null) {
            eq.setBoots(null);
        } else {
            eq.setBoots(boots.as(org.bukkit.inventory.ItemStack.class));
        }
    }

    @Override
    public @Nullable ItemStack getItemInMainHand() {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return null;
        }

        if (BukkitFeature.OFF_HAND.isSupported()) {
            return new BukkitItem(eq.getItemInMainHand());
        } else {
            return new BukkitItem(eq.getItemInHand());
        }
    }

    @Override
    public void setItemInMainHand(@Nullable ItemStack item) {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (BukkitFeature.OFF_HAND.isSupported()) {
            if (item == null) {
                eq.setItemInMainHand(null);
            } else {
                eq.setItemInMainHand(item.as(org.bukkit.inventory.ItemStack.class));
            }
        } else {
            if (item == null) {
                eq.setItemInHand(null);
            } else {
                eq.setItemInHand(item.as(org.bukkit.inventory.ItemStack.class));
            }
        }
    }

    @Override
    public @Nullable ItemStack getItemInOffHand() {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return null;
        }

        if (BukkitFeature.OFF_HAND.isSupported()) {
            return new BukkitItem(eq.getItemInOffHand());
        } else {
            return ItemStackFactory.getAir();
        }
    }

    @Override
    public void setItemInOffHand(@Nullable ItemStack item) {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (BukkitFeature.OFF_HAND.isSupported()) {
            if (item == null) {
                eq.setItemInOffHand(null);
            } else {
                eq.setItemInOffHand(item.as(org.bukkit.inventory.ItemStack.class));
            }
        } else if (item != null && !item.isAir()) {
            // I have no idea where we should put the item, so I just pass it to the addItem method and drop it in case of failure.
            var inv = getInventory();
            if (inv != null) {
                var failure = inv.addItem(item);
                if (!failure.isEmpty()) {
                    var loc = wrappedObject.getLocation();
                    for (var fail : failure) {
                        loc.getWorld().dropItem(loc, fail.as(org.bukkit.inventory.ItemStack.class));
                    }
                }
            } else {
                var loc = wrappedObject.getLocation();
                loc.getWorld().dropItem(loc, item.as(org.bukkit.inventory.ItemStack.class));
            }
        }
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public @Nullable ProjectileEntity launchProjectile(@NotNull EntityType projectileType) {
        var projectileBukkit = projectileType.as(org.bukkit.entity.EntityType.class).getEntityClass();
        if (!Projectile.class.isAssignableFrom(projectileBukkit)) {
            return null;
        }
        return Entities.wrapEntityProjectile(((org.bukkit.entity.LivingEntity) wrappedObject).launchProjectile((Class<Projectile>) projectileBukkit));
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public @Nullable ProjectileEntity launchProjectile(@NotNull EntityType projectileType, @NotNull Vector3D velocity) {
        var projectileBukkit = projectileType.as(org.bukkit.entity.EntityType.class).getEntityClass();
        if (!Projectile.class.isAssignableFrom(projectileBukkit)) {
            return null;
        }
        return Entities.wrapEntityProjectile(((org.bukkit.entity.LivingEntity) wrappedObject).launchProjectile((Class<Projectile>) projectileBukkit, new Vector(velocity.getX(), velocity.getY(), velocity.getZ())));
    }
}
