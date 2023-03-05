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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.block.Blocks;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.Locations;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public class BukkitLivingEntity extends BukkitBasicEntity implements LivingEntity {
    public BukkitLivingEntity(@NotNull org.bukkit.entity.LivingEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @Nullable AttributeHolder getAttribute(@NotNull AttributeTypeHolder attributeType) {
        return AttributeMapping.wrapAttribute(((org.bukkit.entity.LivingEntity) wrappedObject).getAttribute(attributeType.as(Attribute.class)));
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
    public @NotNull Block getTargetBlock(@Nullable Collection<@NotNull BlockTypeHolder> transparent, int maxDistance) {
        return Blocks.wrapBlock(((org.bukkit.entity.LivingEntity) wrappedObject)
                .getTargetBlock((transparent == null) ? null : transparent
                        .stream()
                        .map(material -> material.as(Material.class))
                        .collect(Collectors.toSet()), maxDistance));
    }

    @Override
    public @Nullable Block getTargetBlock(int maxDistance) {
        return Blocks.resolve(((org.bukkit.entity.LivingEntity) wrappedObject)
                .getTargetBlockExact(maxDistance));
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
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getArrowCooldown();
    }

    @Override
    public void setArrowCooldown(int ticks) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setArrowCooldown(ticks);
    }

    @Override
    public int getArrowsInBody() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getArrowsInBody();
    }

    @Override
    public void setArrowsInBody(int count) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setArrowsInBody(count);
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
    public boolean addPotionEffect(@NotNull PotionEffectHolder effect) {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).addPotionEffect(effect.as(PotionEffect.class));
    }

    @Override
    public boolean addPotionEffects(@NotNull Collection<@NotNull PotionEffectHolder> effects) {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).addPotionEffects(effects
                .stream()
                .map(effect -> effect.as(PotionEffect.class))
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean hasPotionEffect(@NotNull PotionEffectHolder type) {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).hasPotionEffect(type.as(PotionEffect.class).getType());
    }

    @Override
    public void removePotionEffect(@NotNull PotionEffectHolder type) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).removePotionEffect(type.as(PotionEffect.class).getType());
    }

    @Override
    public @NotNull List<@NotNull PotionEffectHolder> getActivePotionEffects() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getActivePotionEffects()
                .stream()
                .map(PotionEffectHolder::of)
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
    public @Nullable BasicEntity getLeashHolder() {
        if (isLeashed()) {
            return Entities.wrapEntity(((org.bukkit.entity.LivingEntity) wrappedObject).getLeashHolder());
        }
        return null;
    }

    @Override
    public boolean setLeashHolder(@Nullable BasicEntity holder) {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).setLeashHolder(holder != null ? holder.as(Entity.class) : null);
    }

    @Override
    public boolean removeLeashHolder() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).setLeashHolder(null);
    }

    @Override
    public boolean isGliding() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).isGliding();
    }

    @Override
    public void setGliding(boolean gliding) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setGliding(gliding);
    }

    @Override
    public boolean isSwimming() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).isSwimming();
    }

    @Override
    public void setSwimming(boolean swimming) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setSwimming(swimming);
    }

    @Override
    public boolean isRiptiding() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).isRiptiding();
    }

    @Override
    public boolean isSleeping() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).isSleeping();
    }

    @Override
    public void setAI(boolean ai) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setAI(ai);
    }

    @Override
    public boolean hasAI() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).hasAI();
    }

    @Override
    public void attack(@NotNull BasicEntity target) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).attack(target.as(Entity.class));
    }

    @Override
    public void swingMainHand() {
        ((org.bukkit.entity.LivingEntity) wrappedObject).swingMainHand();
    }

    @Override
    public void swingOffHand() {
        ((org.bukkit.entity.LivingEntity) wrappedObject).swingOffHand();
    }

    @Override
    public void setCollidable(boolean collidable) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setCollidable(collidable);
    }

    @Override
    public boolean isCollidable() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).isCollidable();
    }

    @Override
    public void setInvisible(boolean invisible) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setInvisible(invisible);
    }

    @Override
    public boolean isInvisible() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).isInvisible();
    }

    @Override
    public void damage(double amount) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).damage(amount);
    }

    @Override
    public void damage(double amount, @Nullable BasicEntity damageSource) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).damage(amount, damageSource != null ? damageSource.as(Entity.class) : null);
    }

    @Override
    public double getAbsorptionAmount() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getAbsorptionAmount();
    }

    @Override
    public double getHealth() {
        return ((org.bukkit.entity.LivingEntity) wrappedObject).getHealth();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        ((org.bukkit.entity.LivingEntity) wrappedObject).setAbsorptionAmount(amount);
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

        return new BukkitItem(eq.getItemInMainHand());
    }

    @Override
    public void setItemInMainHand(@Nullable ItemStack item) {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (item == null) {
            eq.setItemInMainHand(null);
        } else {
            eq.setItemInMainHand(item.as(org.bukkit.inventory.ItemStack.class));
        }
    }

    @Override
    public @Nullable ItemStack getItemInOffHand() {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return null;
        }

        return new BukkitItem(eq.getItemInOffHand());
    }

    @Override
    public void setItemInOffHand(@Nullable ItemStack item) {
        var eq = ((org.bukkit.entity.LivingEntity) wrappedObject).getEquipment();
        if (eq == null) {
            return;
        }

        if (item == null) {
            eq.setItemInOffHand(null);
        } else {
            eq.setItemInOffHand(item.as(org.bukkit.inventory.ItemStack.class));
        }
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public @Nullable ProjectileEntity launchProjectile(@NotNull EntityTypeHolder projectileType) {
        var projectileBukkit = projectileType.as(EntityType.class).getEntityClass();
        if (!Projectile.class.isAssignableFrom(projectileBukkit)) {
            return null;
        }
        return Entities.wrapEntityProjectile(((org.bukkit.entity.LivingEntity) wrappedObject).launchProjectile((Class<Projectile>) projectileBukkit));
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public @Nullable ProjectileEntity launchProjectile(@NotNull EntityTypeHolder projectileType, @NotNull Vector3D velocity) {
        var projectileBukkit = projectileType.as(EntityType.class).getEntityClass();
        if (!Projectile.class.isAssignableFrom(projectileBukkit)) {
            return null;
        }
        return Entities.wrapEntityProjectile(((org.bukkit.entity.LivingEntity) wrappedObject).launchProjectile((Class<Projectile>) projectileBukkit, new Vector(velocity.getX(), velocity.getY(), velocity.getZ())));
    }
}
