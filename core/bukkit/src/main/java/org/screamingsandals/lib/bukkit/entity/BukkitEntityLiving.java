package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.attribute.AttributeHolder;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.PotionEffectMapping;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BukkitEntityLiving extends BukkitEntityBasic implements EntityLiving {
    protected BukkitEntityLiving(LivingEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Optional<AttributeHolder> getAttribute(AttributeTypeHolder attributeType) {
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
    public LocationHolder getEyeLocation() {
        return LocationMapper.wrapLocation(((LivingEntity) wrappedObject).getEyeLocation());
    }

    @Override
    public BlockHolder getTargetBlock(Collection<BlockTypeHolder> transparent, int maxDistance) {
        return BlockMapper.wrapBlock(((LivingEntity) wrappedObject)
                .getTargetBlock(transparent
                        .stream()
                        .map(material -> material.as(Material.class))
                        .collect(Collectors.toSet()), maxDistance));
    }

    @Override
    public Optional<BlockHolder> getTargetBlock(int maxDistance) {
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
    public Optional<EntityLiving> getHumanKiller() {
        var player = ((LivingEntity) wrappedObject).getKiller();
        var entity = EntityMapper.wrapEntity(player).orElse(null);

        if (entity instanceof EntityLiving) {
            return Optional.of((EntityLiving) entity);
        }

        return Optional.empty();
    }

    @Override
    public boolean addPotionEffect(PotionEffectHolder effect) {
        return ((LivingEntity) wrappedObject).addPotionEffect(effect.as(PotionEffect.class));
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffectHolder> effects) {
        return ((LivingEntity) wrappedObject).addPotionEffects(effects
                .stream()
                .map(effect -> effect.as(PotionEffect.class))
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean hasPotionEffect(PotionEffectHolder type) {
        return ((LivingEntity) wrappedObject).hasPotionEffect(type.as(PotionEffectType.class));
    }

    @Override
    public void removePotionEffect(PotionEffectHolder type) {
        ((LivingEntity) wrappedObject).removePotionEffect(type.as(PotionEffectType.class));
    }

    @Override
    public List<PotionEffectHolder> getActivePotionEffects() {
        return ((LivingEntity) wrappedObject).getActivePotionEffects()
                .stream()
                .map(potionEffect -> PotionEffectMapping.resolve(potionEffect).orElseThrow())
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
    public Optional<EntityBasic> getLeashHolder() {
        if (isLeashed()) {
            return EntityMapper.wrapEntity(((LivingEntity) wrappedObject).getLeashHolder());
        }
        return Optional.empty();
    }

    @Override
    public boolean setLeashHolder(EntityBasic holder) {
        return ((LivingEntity) wrappedObject).setLeashHolder(holder.as(Entity.class));
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
    public void attack(EntityBasic target) {
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
    public void damage(double amount, EntityBasic damageSource) {
        ((LivingEntity) wrappedObject).damage(amount, damageSource.as(Entity.class));
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

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType) {
        return EntityMapper.wrapEntity(((LivingEntity) wrappedObject).launchProjectile((Class<Projectile>) projectileType.as(EntityType.class).getEntityClass()));
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType, Vector3D velocity) {
        return EntityMapper.wrapEntity(((LivingEntity) wrappedObject).launchProjectile((Class<Projectile>) projectileType.as(EntityType.class).getEntityClass(), new Vector(velocity.getX(), velocity.getY(), velocity.getZ())));
    }
}
