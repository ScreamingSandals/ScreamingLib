/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.ContainerHolder;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface EntityBasic extends Wrapper, RawValueHolder, ContainerHolder {
    EntityTypeHolder getEntityType();

    LocationHolder getLocation();

    Vector3D getVelocity();

    void setVelocity(Vector3D velocity);

    double getHeight();

    double getWidth();

    /**
     * Checks if the entity is on ground.
     *
     * @return is the entity on ground?
     */
    boolean isOnGround();

    boolean isInWater();

    /**
     * Teleports this entity to a location asynchronously.
     *
     * @param location the location to teleport to
     * @return the teleport future
     */
    CompletableFuture<Boolean> teleport(LocationHolder location);

    /**
     * Teleports this entity to a location asynchronously.
     *
     * @param location the location to teleport to
     * @param callback the callback runnable
     * @return the teleport future
     */
    default CompletableFuture<Void> teleport(LocationHolder location, Runnable callback) {
        return teleport(location, callback, false);
    }

    /**
     * Teleports this entity to a location asynchronously.
     *
     * @param location the location to teleport to
     * @param callback the callback runnable
     * @param forceCallback should the callback be run even if the teleport didn't succeed?
     * @return the teleport future
     */
    CompletableFuture<Void> teleport(LocationHolder location, Runnable callback, boolean forceCallback);

    /**
     * Teleports this entity to a location synchronously.
     *
     * @param location the location to teleport to
     * @return true if the teleport was successful
     */
    boolean teleportSync(LocationHolder location);

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

    boolean hasMetadata(String metadata);

    Object getMetadata(String metadata);

    int getIntMetadata(String metadata);

    boolean getBooleanMetadata(String metadata);

    byte getByteMetadata(String metadata);

    long getLongMetadata(String metadata);

    String getStringMetadata(String metadata);

    Component getComponentMetadata(String metadata);

    LocationHolder getLocationMetadata(String metadata);

    RGBLike getColorMetadata(String metadata);

    Vector3D getVectorMetadata(String metadata);

    Vector3Df getFloatVectorMetadata(String metadata);

    void setMetadata(String metadata, Object value);
}
