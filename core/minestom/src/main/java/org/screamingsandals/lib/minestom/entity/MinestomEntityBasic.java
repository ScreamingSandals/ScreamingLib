package org.screamingsandals.lib.minestom.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.fluid.Fluid;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MinestomEntityBasic extends BasicWrapper<Entity> implements EntityBasic {
    protected MinestomEntityBasic(Entity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public EntityTypeHolder getEntityType() {
        return EntityTypeHolder.of(wrappedObject.getEntityType());
    }

    @Override
    public LocationHolder getLocation() {
        return LocationMapper.wrapLocation(wrappedObject);
    }

    @Override
    public Vector3D getVelocity() {
        var platformVector = wrappedObject.getVelocity();
        return new Vector3D(platformVector.x(), platformVector.y(), platformVector.z());
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        wrappedObject.setVelocity(new Vec(velocity.getX(), velocity.getY(), velocity.getZ()));
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
        if (wrappedObject.getInstance() == null) {
            return false;
        }
        return wrappedObject.getInstance().getBlock(wrappedObject.getPosition()).namespace().equals(Fluid.WATER.getNamespaceID())
                || wrappedObject.getInstance().getBlock(wrappedObject.getPosition()).namespace().equals(Fluid.FLOWING_WATER.getNamespaceID());
    }

    @Override
    public CompletableFuture<Boolean> teleport(LocationHolder location) {
        return wrappedObject.teleport(location.as(Pos.class)).handle((v, ex) -> ex == null);
    }

    @Override
    public boolean teleportSync(LocationHolder location) {
        return teleport(location).join();
    }

    @Override
    public int getEntityId() {
        return wrappedObject.getEntityId();
    }

    @Override
    public int getFireTicks() {
        return 0;
    }

    @Override
    public int getMaxFireTicks() {
        return 0;
    }

    @Override
    public void setFireTicks(int fireTicks) {
        // empty stub
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
        return true;
    }

    @Override
    public void setPersistent(boolean persistent) {
        // empty stub
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
        List.copyOf(wrappedObject.getPassengers()).forEach(Entity::remove);
        return true;
    }

    @Override
    public float getFallDistance() {
        return 0;
    }

    @Override
    public void setFallDistance(float distance) {
        // empty stub
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
        try {
            Reflect.setField(wrappedObject, "ticks", (long) value);
        } catch (Throwable ignored) {
            // ignored
        }
    }

    @Override
    public boolean isInsideVehicle() {
        return wrappedObject.getVehicle() != null;
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
        wrappedObject.setCustomName(AdventureHelper.toComponent(name));
    }

    @Override
    public void setCustomName(Component name) {
        wrappedObject.setCustomName(name);
    }

    @Override
    public @Nullable Component getCustomName() {
        return wrappedObject.getCustomName();
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
        // empty stub
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
        return 0;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        // empty stub
    }

    @Override
    public boolean hasMetadata(String metadata) {
        return false;
    }

    @Override
    public Object getMetadata(String metadata) {
        return null;
    }

    @Override
    public int getIntMetadata(String metadata) {
        return 0;
    }

    @Override
    public boolean getBooleanMetadata(String metadata) {
        return false;
    }

    @Override
    public byte getByteMetadata(String metadata) {
        return 0;
    }

    @Override
    public long getLongMetadata(String metadata) {
        return 0;
    }

    @Override
    public String getStringMetadata(String metadata) {
        return null;
    }

    @Override
    public Component getComponentMetadata(String metadata) {
        return null;
    }

    @Override
    public LocationHolder getLocationMetadata(String metadata) {
        return null;
    }

    @Override
    public RGBLike getColorMetadata(String metadata) {
        return null;
    }

    @Override
    public Vector3D getVectorMetadata(String metadata) {
        return null;
    }

    @Override
    public Vector3Df getFloatVectorMetadata(String metadata) {
        return null;
    }

    @Override
    public void setMetadata(String metadata, Object value) {

    }

    @Override
    public boolean holdsInventory() {
        return false;
    }

    @Override
    public Optional<Container> getInventory() {
        return Optional.empty();
    }
}
