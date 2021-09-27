package org.screamingsandals.lib.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.UUID;

// TODO: Metadata
public interface EntityBasic extends Wrapper {
    EntityTypeHolder getEntityType();

    LocationHolder getLocation();

    Vector3D getVelocity();

    void setVelocity(Vector3D velocity);

    double getHeight();

    double getWidth();

    boolean isOnGround();

    boolean isInWater();

    boolean teleport(LocationHolder locationHolder);

    int getEntityId();

    int getFireTicks();

    int getMaxFireTicks();

    void setFireTicks(int fireTicks);

    void remove();

    boolean isDead();

    boolean isPersistent();

    void setPersistent(boolean persistent);

    List<EntityBasic> getPassengers();

    boolean addPassenger(EntityBasic passenger);

    boolean removePassenger(EntityBasic passenger);

    boolean hasPassengers();

    boolean ejectPassengers();

    float getFallDistance();

    void setFallDistance(float distance);

    UUID getUniqueId();

    int getTicksLived();

    void setTicksLived(int value);

    boolean isInsideVehicle();

    boolean leaveVehicle();

    EntityBasic getVehicle();

    void setCustomName(String name);

    void setCustomName(Component name);

    default void setCustomName(ComponentLike name) {
        setCustomName(name.asComponent());
    }

    @Nullable
    Component getCustomName();

    void setCustomNameVisible(boolean flag);

    boolean isCustomNameVisible();

    void setGlowing(boolean flag);

    boolean isGlowing();

    void setInvulnerable(boolean flag);

    boolean isInvulnerable();

    boolean isSilent();

    void setSilent(boolean flag);

    boolean hasGravity();

    void setGravity(boolean gravity);

    int getPortalCooldown();

    void setPortalCooldown(int cooldown);
}
