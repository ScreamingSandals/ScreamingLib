/*
 * Copyright 2023 ScreamingSandals
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.ContainerHolder;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.particle.Vibration;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.tasker.ThreadProperty;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.Location;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Entity extends Wrapper, RawValueHolder, ContainerHolder, ThreadProperty, Vibration.Destination {
    @NotNull EntityType getEntityType();

    @NotNull Location getLocation();

    @NotNull Vector3D getVelocity();

    void setVelocity(@NotNull Vector3D velocity);

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
    @NotNull CompletableFuture<@NotNull Boolean> teleport(@NotNull Location location);

    /**
     * Teleports this entity to a location asynchronously.
     *
     * @param location the location to teleport to
     * @param callback the callback runnable
     * @return the teleport future
     */
    default @NotNull CompletableFuture<Void> teleport(@NotNull Location location, @NotNull Runnable callback) {
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
    @NotNull CompletableFuture<Void> teleport(@NotNull Location location, @NotNull Runnable callback, boolean forceCallback);

    /**
     * Teleports this entity to a location synchronously.
     *
     * @param location the location to teleport to
     * @return true if the teleport was successful
     */
    boolean teleportSync(@NotNull Location location);

    int getEntityId();

    int getFireTicks();

    int getMaxFireTicks();

    void setFireTicks(int fireTicks);

    void remove();

    boolean isDead();

    boolean isPersistent();

    void setPersistent(boolean persistent);

    @NotNull List<@NotNull Entity> getPassengers();

    boolean addPassenger(@NotNull Entity passenger);

    boolean removePassenger(@NotNull Entity passenger);

    boolean hasPassengers();

    boolean ejectPassengers();

    float getFallDistance();

    void setFallDistance(float distance);

    @NotNull UUID getUniqueId();

    int getTicksLived();

    void setTicksLived(int value);

    boolean isInsideVehicle();

    boolean leaveVehicle();

    @Nullable Entity getVehicle();

    void setCustomName(@Nullable String name);

    void setCustomName(@Nullable Component name);

    default void setCustomName(@Nullable ComponentLike name) {
        setCustomName(name.asComponent());
    }

    @Nullable Component getCustomName();

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
