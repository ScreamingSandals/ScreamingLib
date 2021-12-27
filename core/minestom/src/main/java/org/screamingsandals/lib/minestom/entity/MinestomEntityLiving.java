package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.attribute.Attribute;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.DamageType;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.attribute.AttributeHolder;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
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
    public Optional<AttributeHolder> getAttribute(AttributeTypeHolder attributeType) {
        return AttributeMapping.wrapAttribute(((LivingEntity) wrappedObject).getAttribute(attributeType.as(Attribute.class)));
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
        return 0;// TODO
    }

    @Override
    public void setRemainingAir(int ticks) {
// TODO
    }

    @Override
    public int getMaximumAir() {
        return 0;// TODO
    }

    @Override
    public void setMaximumAir(int ticks) {
// TODO
    }

    @Override
    public int getArrowCooldown() {
        return 0;// TODO
    }

    @Override
    public void setArrowCooldown(int ticks) {
// TODO
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
        return 0;// TODO
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
// TODO
    }

    @Override
    public double getLastDamage() {
        return 0;// TODO
    }

    @Override
    public void setLastDamage(double damage) {
// TODO
    }

    @Override
    public int getNoDamageTicks() {
        return 0;// TODO
    }

    @Override
    public void setNoDamageTicks(int ticks) {
// TODO
    }

    @Override
    public Optional<EntityLiving> getHumanKiller() {
        return Optional.empty();// TODO
    }

    @Override
    public boolean addPotionEffect(PotionEffectHolder effect) {
        return false;// TODO
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffectHolder> effects) {
        return false;// TODO
    }

    @Override
    public boolean hasPotionEffect(PotionEffectHolder type) {
        return false;// TODO
    }

    @Override
    public void removePotionEffect(PotionEffectHolder type) {
// TODO
    }

    @Override
    public List<PotionEffectHolder> getActivePotionEffects() {
        return null;// TODO
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return false;// TODO
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
// TODO
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
// TODO
    }

    @Override
    public boolean getCanPickupItems() {
        return false;// TODO
    }

    @Override
    public boolean isLeashed() {
        return false;// TODO
    }

    @Override
    public Optional<EntityBasic> getLeashHolder() {
        return Optional.empty();// TODO
    }

    @Override
    public boolean setLeashHolder(EntityBasic holder) {
        return false;// TODO
    }

    @Override
    public boolean removeLeashHolder() {
        return false;// TODO
    }

    @Override
    public boolean isGliding() {
        return false;// TODO
    }

    @Override
    public void setGliding(boolean gliding) {
// TODO
    }

    @Override
    public boolean isSwimming() {
        return false;// TODO
    }

    @Override
    public void setSwimming(boolean swimming) {
// TODO
    }

    @Override
    public boolean isRiptiding() {
        return false;// TODO
    }

    @Override
    public boolean isSleeping() {
        return false;// TODO
    }

    @Override
    public void setAI(boolean ai) {
// TODO
    }

    @Override
    public boolean hasAI() {
        return false;// TODO
    }

    @Override
    public void attack(EntityBasic target) {
// TODO
    }

    @Override
    public void swingMainHand() {
// TODO
    }

    @Override
    public void swingOffHand() {
// TODO
    }

    @Override
    public void setCollidable(boolean collidable) {
// TODO
    }

    @Override
    public boolean isCollidable() {
        return false;// TODO
    }

    @Override
    public void setInvisible(boolean invisible) {
// TODO
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    private LivingEntity getWrappedObject() {
        return (LivingEntity) wrappedObject;
    }

    @Override
    public void damage(double amount) {
        ((LivingEntity) wrappedObject).damage(DamageType.VOID, (float) amount); // probably don't use Void
    }

    @Override
    public void damage(double amount, EntityBasic damageSource) {
        ((LivingEntity) wrappedObject).damage(DamageType.fromEntity(damageSource.as(Entity.class)), (float) amount);
    }

    @Override
    public double getAbsorptionAmount() {
        return 0; // TODO
    }

    @Override
    public double getHealth() {
        return ((LivingEntity) wrappedObject).getHealth();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        // TODO
    }

    @Override
    public void setHealth(double health) {
        ((LivingEntity) wrappedObject).setHealth((float) health);
    }
}
