package org.screamingsandals.lib.bukkit.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BukkitEntityBasic extends BasicWrapper<Entity> implements EntityBasic {
    protected BukkitEntityBasic(Entity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public EntityTypeHolder getEntityType() {
        return EntityTypeMapping.resolve(wrappedObject.getType()).orElseThrow();
    }

    @Override
    public LocationHolder getLocation() {
        return LocationMapper.wrapLocation(wrappedObject.getLocation());
    }

    @Override
    public Vector3D getVelocity() {
        var platformVector = wrappedObject.getVelocity();
        return new Vector3D(platformVector.getX(), platformVector.getY(), platformVector.getZ());
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        wrappedObject.setVelocity(new Vector(velocity.getX(), velocity.getY(), velocity.getZ()));
    }

    @Override
    public double getHeight() {
        return wrappedObject.getHeight();
    }

    @Override
    public double getWidth() {
        return wrappedObject.getWidth();
    }

    @Override
    public boolean isOnGround() {
        return wrappedObject.isOnGround();
    }

    @Override
    public boolean isInWater() {
        return wrappedObject.isInWater();
    }

    @Override
    public boolean teleport(LocationHolder locationHolder) {
        return wrappedObject.teleport(locationHolder.as(Location.class));
    }

    @Override
    public int getEntityId() {
        return wrappedObject.getEntityId();
    }

    @Override
    public int getFireTicks() {
        return wrappedObject.getFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return wrappedObject.getMaxFireTicks();
    }

    @Override
    public void setFireTicks(int fireTicks) {
        wrappedObject.setFireTicks(fireTicks);
    }

    @Override
    public void remove() {
        wrappedObject.remove();
    }

    @Override
    public boolean isDead() {
        return wrappedObject.isDead();
    }

    @Override
    public boolean isPersistent() {
        return wrappedObject.isPersistent();
    }

    @Override
    public void setPersistent(boolean persistent) {
        wrappedObject.setPersistent(persistent);
    }

    @Override
    public List<EntityBasic> getPassengers() {
        return wrappedObject.getPassengers()
                .stream()
                .map(EntityMapper::wrapEntity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addPassenger(EntityBasic passenger) {
        return wrappedObject.addPassenger(passenger.as(Entity.class));
    }

    @Override
    public boolean removePassenger(EntityBasic passenger) {
        return wrappedObject.removePassenger(passenger.as(Entity.class));
    }

    @Override
    public boolean hasPassengers() {
        return !wrappedObject.isEmpty();
    }

    @Override
    public boolean ejectPassengers() {
        return wrappedObject.eject();
    }

    @Override
    public float getFallDistance() {
        return wrappedObject.getFallDistance();
    }

    @Override
    public void setFallDistance(float distance) {
        wrappedObject.setFallDistance(distance);
    }

    @Override
    public UUID getUniqueId() {
        return wrappedObject.getUniqueId();
    }

    @Override
    public int getTicksLived() {
        return wrappedObject.getTicksLived();
    }

    @Override
    public void setTicksLived(int value) {
        wrappedObject.setTicksLived(value);
    }

    @Override
    public boolean isInsideVehicle() {
        return wrappedObject.isInsideVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        return wrappedObject.leaveVehicle();
    }

    @Override
    public EntityBasic getVehicle() {
        return EntityMapper.wrapEntity(wrappedObject.getVehicle()).orElseThrow();
    }

    @Override
    public void setCustomName(String name) {
        wrappedObject.setCustomName(name);
    }

    @Override
    public void setCustomName(Component name) {
        setCustomName(AdventureHelper.toLegacy(name));
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        wrappedObject.setCustomNameVisible(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return wrappedObject.isCustomNameVisible();
    }

    @Override
    public void setGlowing(boolean flag) {
        wrappedObject.setGlowing(flag);
    }

    @Override
    public boolean isGlowing() {
        return wrappedObject.isGlowing();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        wrappedObject.setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return wrappedObject.isInvulnerable();
    }

    @Override
    public boolean isSilent() {
        return wrappedObject.isSilent();
    }

    @Override
    public void setSilent(boolean flag) {
        wrappedObject.setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return wrappedObject.hasGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        wrappedObject.setGravity(gravity);
    }

    @Override
    public int getPortalCooldown() {
        return wrappedObject.getPortalCooldown();
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        wrappedObject.setPortalCooldown(cooldown);
    }
}
