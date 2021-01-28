package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.meta.PotionEffectHolder;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BukkitEntityLiving extends BukkitEntityBasic implements EntityLiving {
    protected BukkitEntityLiving(LivingEntity wrappedObject) {
        super(wrappedObject);
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
        return LocationMapping.wrapLocation(((LivingEntity) wrappedObject).getEyeLocation());
    }

    @Override
    public BlockHolder getTargetBlock(Collection<MaterialHolder> transparent, int maxDistance) {
        return BlockMapper.wrapBlock(((LivingEntity) wrappedObject)
                .getTargetBlock(transparent
                        .stream()
                        .map(material -> material.as(Material.class))
                        .collect(Collectors.toSet()), maxDistance));
    }

    @Override
    public BlockHolder getTargetBlock(int maxDistance) {
        return BlockMapper.wrapBlock(((LivingEntity) wrappedObject)
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

    // TODO

    @Override
    public boolean addPotionEffect(PotionEffectHolder effect) {
        return false;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffectHolder> effects) {
        return false;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectHolder type) {
        return false;
    }

    @Override
    public void removePotionEffect(PotionEffectHolder type) {

    }

    @Override
    public List<PotionEffectHolder> getActivePotionEffects() {
        return null;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {

    }

    @Override
    public void setCanPickupItems(boolean pickup) {

    }

    @Override
    public boolean getCanPickupItems() {
        return false;
    }

    @Override
    public boolean isLeashed() {
        return false;
    }

    @Override
    public Optional<EntityBasic> getLeashHolder() {
        return Optional.empty();
    }

    @Override
    public boolean setLeashHolder(EntityBasic holder) {
        return false;
    }

    @Override
    public boolean removeLeashHolder() {
        return false;
    }

    @Override
    public boolean isGliding() {
        return false;
    }

    @Override
    public void setGliding(boolean gliding) {

    }

    @Override
    public boolean isSwimming() {
        return false;
    }

    @Override
    public void setSwimming(boolean swimming) {

    }

    @Override
    public boolean isRiptiding() {
        return false;
    }

    @Override
    public boolean isSleeping() {
        return false;
    }

    @Override
    public void setAI(boolean ai) {

    }

    @Override
    public boolean hasAI() {
        return false;
    }

    @Override
    public void attack(EntityBasic target) {

    }

    @Override
    public void swingMainHand() {

    }

    @Override
    public void swingOffHand() {

    }

    @Override
    public void setCollidable(boolean collidable) {

    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public void setInvisible(boolean invisible) {

    }

    @Override
    public boolean isInvisible() {
        return false;
    }
}
