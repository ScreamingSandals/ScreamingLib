package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.LivingEntity;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.meta.PotionEffectHolder;
import org.screamingsandals.lib.minestom.world.InstancedBlockPosition;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MinestomEntityLiving extends MinestomEntityBasic implements EntityLiving {

    protected MinestomEntityLiving(LivingEntity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public double getEyeHeight() {
        return wrappedObject.getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return wrappedObject.getEyeHeight();
    }

    @Override
    public LocationHolder getEyeLocation() {
        //TODO
        return null;
    }

    @Override
    public BlockHolder getTargetBlock(Collection<MaterialHolder> transparent, int maxDistance) {
        return getTargetBlock(maxDistance).orElse(null);
    }

    @Override
    public Optional<BlockHolder> getTargetBlock(int maxDistance) {
        final var location = LocationMapper.wrapLocation(
                new InstancedBlockPosition(getWrappedObject().getInstance(), getWrappedObject().getTargetBlockPosition(maxDistance)));
        return Optional.ofNullable(BlockMapper.getBlockAt(location));
    }

    @Override
    public int getRemainingAir() {
        return 0;
    }

    @Override
    public void setRemainingAir(int ticks) {

    }

    @Override
    public int getMaximumAir() {
        return 0;
    }

    @Override
    public void setMaximumAir(int ticks) {

    }

    @Override
    public int getArrowCooldown() {
        return 0;
    }

    @Override
    public void setArrowCooldown(int ticks) {

    }

    @Override
    public int getArrowsInBody() {
        return getWrappedObject().getArrowCount();
    }

    @Override
    public void setArrowsInBody(int count) {
        getWrappedObject().setArrowCount(count);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return 0;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {

    }

    @Override
    public double getLastDamage() {
        return 0;
    }

    @Override
    public void setLastDamage(double damage) {

    }

    @Override
    public int getNoDamageTicks() {
        return 0;
    }

    @Override
    public void setNoDamageTicks(int ticks) {

    }

    @Override
    public Optional<EntityLiving> getHumanKiller() {
        return Optional.empty();
    }

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

    private LivingEntity getWrappedObject() {
        return (LivingEntity) wrappedObject;
    }
}
