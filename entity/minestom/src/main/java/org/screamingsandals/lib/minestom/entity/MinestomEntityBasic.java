package org.screamingsandals.lib.minestom.entity;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Entity;
import net.minestom.server.utils.Vector;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.minestom.utils.MinestomAdventureHelper;
import org.screamingsandals.lib.minestom.world.InstancedPosition;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.entity.DataWatcher;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MinestomEntityBasic extends BasicWrapper<Entity> implements EntityBasic {

    protected MinestomEntityBasic(Entity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public EntityTypeHolder getEntityType() {
        return EntityTypeMapping.resolve(wrappedObject.getEntityType()).orElseThrow();
    }

    @Override
    public LocationHolder getLocation() {
        return LocationMapper.wrapLocation(
                new InstancedPosition(wrappedObject.getInstance(), wrappedObject.getPosition()));
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
        return wrappedObject.getBoundingBox().getHeight();
    }

    @Override
    public double getWidth() {
        return wrappedObject.getBoundingBox().getWidth();
    }

    @Override
    public boolean isOnGround() {
        return wrappedObject.isOnGround();
    }

    @Override
    public boolean isInWater() {
        return false; //TODO
    }

    @Override
    public boolean teleport(LocationHolder locationHolder) {
       try {
           final var location = locationHolder.as(InstancedPosition.class);
           wrappedObject.setInstance(location.getInstance());
           wrappedObject.teleport(location);
           return true;
       } catch (Throwable t) {
           t.printStackTrace();
           return false;
       }
    }

    @Override
    public int getEntityId() {
        return wrappedObject.getEntityId();
    }

    @Override
    public int getFireTicks() {
        return 0; //TODO
    }

    @Override
    public int getMaxFireTicks() {
        return 0; //TODO
    }

    @Override
    public void setFireTicks(int fireTicks) {
        //todo
    }

    @Override
    public void remove() {
        wrappedObject.remove();
    }

    @Override
    public boolean isDead() {
        return wrappedObject.isRemoved();
    }

    @Override
    public boolean isPersistent() {
        //todo
        return false;
    }

    @Override
    public void setPersistent(boolean persistent) {

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
        wrappedObject.addPassenger(passenger.as(Entity.class));
        return true;
    }

    @Override
    public boolean removePassenger(EntityBasic passenger) {
        wrappedObject.removePassenger(passenger.as(Entity.class));
        return true;
    }

    @Override
    public boolean hasPassengers() {
        return wrappedObject.hasPassenger();
    }

    @Override
    public boolean ejectPassengers() {
        List.copyOf(wrappedObject.getPassengers())
                .forEach(Entity::remove);
        return true;
    }

    @Override
    public float getFallDistance() {
        return 0; //TODO
    }

    @Override
    public void setFallDistance(float distance) {

    }

    @Override
    public UUID getUniqueId() {
        return wrappedObject.getUuid();
    }

    @Override
    public int getTicksLived() {
        return (int) wrappedObject.getAliveTicks();
    }

    @Override
    public void setTicksLived(int value) {
        //TODO (maybe entity#tick()?);
    }

    @Override
    public boolean isInsideVehicle() {
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        final var vehicle = wrappedObject.getVehicle();
        if (vehicle != null) {
            vehicle.removePassenger(wrappedObject);
            return true;
        }
        return false;
    }

    @Override
    public EntityBasic getVehicle() {
        return EntityMapper.wrapEntity(wrappedObject.getVehicle()).orElse(null);
    }

    @Override
    public void setCustomName(String name) {
        wrappedObject.setCustomName(MinestomAdventureHelper.toMinestom(AdventureHelper.toComponent(name)));
    }

    @Override
    public void setCustomName(Component name) {
        wrappedObject.setCustomName(MinestomAdventureHelper.toMinestom(name));
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

    }

    @Override
    public boolean isInvulnerable() {
        return false;
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
        return !wrappedObject.hasNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        wrappedObject.setNoGravity(!gravity);
    }

    @Override
    public int getPortalCooldown() {
        return 0; //TODO
    }

    @Override
    public void setPortalCooldown(int cooldown) {

    }

    //TODO:
    @Override
    public DataWatcher getDataWatcher() {
        return null;
    }
}
