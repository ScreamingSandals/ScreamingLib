package org.screamingsandals.lib.minestom.entity;

import com.extollit.gaming.ai.path.HydrazinePathFinder;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.pathfinding.NavigableEntity;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.utils.block.BlockIterator;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeHolder;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.minestom.world.MinestomLocationMapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldMapper;

import java.util.*;
import java.util.stream.Collectors;

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
        return getLocation().add(0, wrappedObject.getEyeHeight(), 0);
    }

    @Override
    public BlockHolder getTargetBlock(Collection<BlockTypeHolder> transparent, int maxDistance) {
        if (wrappedObject.getInstance() == null) {
            throw new IllegalStateException("Entity doesn't have an Instance yet");
        }
        final var world = WorldMapper.wrapWorld(wrappedObject.getInstance());
        final var point = getTargetBlockPosition(transparent, maxDistance);
        return MinestomLocationMapper.wrapPoint(Objects.requireNonNull(point, "No target block found"), world).getBlock();
    }

    private Point getTargetBlockPosition(Collection<BlockTypeHolder> transparent, int maxDistance) {
        final List<Block> minestomTransparents = transparent.stream().map(e -> e.as(Block.class)).collect(Collectors.toList());
        final Iterator<Point> it = new BlockIterator(wrappedObject, maxDistance);
        while (it.hasNext()) {
            final Point position = it.next();
            if (!minestomTransparents.contains(Objects.requireNonNull(wrappedObject.getInstance()).getBlock(position))) {
                return position;
            }
        }
        return null;
    }

    @Override
    public Optional<BlockHolder> getTargetBlock(int maxDistance) {
        if (wrappedObject.getInstance() == null) {
            throw new IllegalStateException("Entity doesn't have an Instance yet");
        }
        final var world = WorldMapper.wrapWorld(wrappedObject.getInstance());
        final var point = ((LivingEntity) wrappedObject).getTargetBlockPosition(maxDistance);
        return (point != null) ? Optional.of(MinestomLocationMapper.wrapPoint(point, world).getBlock()) : Optional.empty();
    }

    @Override
    public int getRemainingAir() {
        return wrappedObject.getEntityMeta().getAirTicks();
    }

    @Override
    public void setRemainingAir(int ticks) {
        wrappedObject.getEntityMeta().setAirTicks(ticks);
    }

    @Override
    public int getMaximumAir() {
        return 300;
    }

    @Override
    public void setMaximumAir(int ticks) {
        // empty stub
    }

    @Override
    public int getArrowCooldown() {
        return 0;
    }

    @Override
    public void setArrowCooldown(int ticks) {
        // empty stub
    }

    @Override
    public int getArrowsInBody() {
        return ((LivingEntity) wrappedObject).getArrowCount();
    }

    @Override
    public void setArrowsInBody(int count) {
        ((LivingEntity) wrappedObject).setArrowCount(count);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return 0;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        // empty stub
    }

    @Override
    public double getLastDamage() {
        return 0;
    }

    @Override
    public void setLastDamage(double damage) {
        // empty stub
    }

    @Override
    public int getNoDamageTicks() {
        return 0;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        // empty stub
    }

    @Override
    public Optional<EntityLiving> getHumanKiller() {
        return Optional.empty();
    }

    @Override
    public boolean addPotionEffect(PotionEffectHolder effect) {
        wrappedObject.addEffect(effect.as(Potion.class));
        return true;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffectHolder> effects) {
        effects.forEach(this::addPotionEffect);
        return true;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectHolder type) {
        return wrappedObject.getActiveEffects().stream().anyMatch(e -> PotionEffectHolder.of(e).equals(type));
    }

    @Override
    public void removePotionEffect(PotionEffectHolder type) {
        wrappedObject.removeEffect(type.as(PotionEffect.class));
    }

    @Override
    public List<PotionEffectHolder> getActivePotionEffects() {
        return wrappedObject.getActiveEffects().stream()
                .map(PotionEffectHolder::of)
                .collect(Collectors.toList());
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        // empty stub
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        // empty stub
    }

    @Override
    public boolean getCanPickupItems() {
        return true;
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
        return wrappedObject.getPose() == Entity.Pose.FALL_FLYING;
    }

    @Override
    public void setGliding(boolean gliding) {
        wrappedObject.setPose(gliding ? Entity.Pose.FALL_FLYING : Entity.Pose.STANDING);
    }

    @Override
    public boolean isSwimming() {
        return wrappedObject.getPose() == Entity.Pose.SWIMMING;
    }

    @Override
    public void setSwimming(boolean swimming) {
        wrappedObject.setPose(swimming ? Entity.Pose.SWIMMING : Entity.Pose.STANDING);
    }

    @Override
    public boolean isRiptiding() {
        return wrappedObject.getPose() == Entity.Pose.SPIN_ATTACK;
    }

    @Override
    public boolean isSleeping() {
        return wrappedObject.getPose() == Entity.Pose.SLEEPING;
    }

    @Override
    public void setAI(boolean ai) {
        if (wrappedObject.getInstance() == null) {
            return;
        }
        if (wrappedObject instanceof NavigableEntity) {
            final var navigator = ((NavigableEntity) wrappedObject).getNavigator();
            //noinspection UnstableApiUsage
            navigator.setPathFinder(ai ? new HydrazinePathFinder(navigator.getPathingEntity(), wrappedObject.getInstance().getInstanceSpace()) : null);
        }
    }

    @Override
    public boolean hasAI() {
        if (wrappedObject instanceof NavigableEntity && wrappedObject.getInstance() != null) {
            return Reflect.getField(((NavigableEntity) wrappedObject).getNavigator(), "pathFinder") != null;
        }
        return false;
    }

    @Override
    public void attack(EntityBasic target) {
        // TODO: hook into MinestomPvP and do it properly
        ((LivingEntity) wrappedObject).swingMainHand();
        EventDispatcher.call(new EntityAttackEvent(wrappedObject, target.as(Entity.class)));
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
        // TODO
    }

    @Override
    public boolean isCollidable() {
        return true; // TODO
    }

    @Override
    public void setInvisible(boolean invisible) {
        wrappedObject.setInvisible(invisible);
    }

    @Override
    public boolean isInvisible() {
        return wrappedObject.isInvisible();
    }

    @Override
    public void damage(double amount) {
        ((LivingEntity) wrappedObject).damage(DamageType.VOID, (float) amount);
    }

    @Override
    public void damage(double amount, EntityBasic damageSource) {
        ((LivingEntity) wrappedObject).damage(DamageType.fromEntity(damageSource.as(Entity.class)), (float) amount);
    }

    @Override
    public double getAbsorptionAmount() {
        return ((LivingEntity) wrappedObject).getLivingEntityMeta().getHealthAddedByAbsorption();
    }

    @Override
    public double getHealth() {
        return ((LivingEntity) wrappedObject).getHealth();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        ((LivingEntity) wrappedObject).getLivingEntityMeta().setHealthAddedByAbsorption((int) amount);
    }

    @Override
    public void setHealth(double health) {
        ((LivingEntity) wrappedObject).setHealth((float) health);
    }

    @Override
    public @Nullable Item getHelmet() {
        return ItemFactory.build(((LivingEntity) wrappedObject).getHelmet()).orElseThrow();
    }

    @Override
    public @Nullable Item getChestplate() {
        return ItemFactory.build(((LivingEntity) wrappedObject).getChestplate()).orElseThrow();
    }

    @Override
    public @Nullable Item getLeggings() {
        return ItemFactory.build(((LivingEntity) wrappedObject).getLeggings()).orElseThrow();
    }

    @Override
    public @Nullable Item getBoots() {
        return ItemFactory.build(((LivingEntity) wrappedObject).getBoots()).orElseThrow();
    }

    @Override
    public void setHelmet(@Nullable Item helmet) {
        ((LivingEntity) wrappedObject).setHelmet(helmet != null ? helmet.as(ItemStack.class) : ItemStack.AIR);
    }

    @Override
    public void setChestplate(@Nullable Item chestplate) {
        ((LivingEntity) wrappedObject).setChestplate(chestplate != null ? chestplate.as(ItemStack.class) : ItemStack.AIR);
    }

    @Override
    public void setLeggings(@Nullable Item leggings) {
        ((LivingEntity) wrappedObject).setLeggings(leggings != null ? leggings.as(ItemStack.class) : ItemStack.AIR);
    }

    @Override
    public void setBoots(@Nullable Item boots) {
        ((LivingEntity) wrappedObject).setBoots(boots != null ? boots.as(ItemStack.class) : ItemStack.AIR);
    }

    @Override
    public @Nullable Item getItemInMainHand() {
        return ItemFactory.build(((LivingEntity) wrappedObject).getItemInMainHand()).orElseThrow();
    }

    @Override
    public void setItemInMainHand(@Nullable Item item) {
        ((LivingEntity) wrappedObject).setItemInMainHand(item != null ? item.as(ItemStack.class) : ItemStack.AIR);
    }

    @Override
    public @Nullable Item getItemInOffHand() {
        return ItemFactory.build(((LivingEntity) wrappedObject).getItemInOffHand()).orElseThrow();
    }

    @Override
    public void setItemInOffHand(@Nullable Item item) {
        ((LivingEntity) wrappedObject).setItemInOffHand(item != null ? item.as(ItemStack.class) : ItemStack.AIR);
    }

    // TODO

    @Override
    public Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType) {
        return Optional.empty();
    }

    @Override
    public Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType, Vector3D velocity) {
        return Optional.empty();
    }

    @Override
    public void setFireTicks(int fireTicks) {
        ((LivingEntity) wrappedObject).setFireForDuration(fireTicks, TimeUnit.SERVER_TICK);
    }

    @Override
    public boolean isInvulnerable() {
        return ((LivingEntity) wrappedObject).isInvulnerable();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        ((LivingEntity) wrappedObject).setInvulnerable(flag);
    }
}
