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

package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.entity.type.BukkitEntityType1_11;
import org.screamingsandals.lib.bukkit.entity.type.BukkitEntityType1_8;
import org.screamingsandals.lib.bukkit.entity.type.InternalEntityLegacyConstants;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.ext.paperlib.PaperLib;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.nms.accessors.EnumZombieTypeAccessor;
import org.screamingsandals.lib.nms.accessors.ZombieAccessor;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.Locations;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class BukkitEntity extends BasicWrapper<org.bukkit.entity.Entity> implements Entity {
    public BukkitEntity(@NotNull org.bukkit.entity.Entity wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull EntityType getEntityType() {
        if (!Server.isVersion(1, 11)) {
            if (wrappedObject instanceof Horse) {
                var variant = ((Horse) wrappedObject).getVariant();
                switch (variant) {
                    case DONKEY:
                        return new BukkitEntityType1_8(wrappedObject.getType(), InternalEntityLegacyConstants.HORSE_VARIANT_DONKEY);
                    case MULE:
                        return new BukkitEntityType1_8(wrappedObject.getType(), InternalEntityLegacyConstants.HORSE_VARIANT_MULE);
                    case SKELETON_HORSE:
                        return new BukkitEntityType1_8(wrappedObject.getType(), InternalEntityLegacyConstants.HORSE_VARIANT_SKELETON);
                    case UNDEAD_HORSE:
                        return new BukkitEntityType1_8(wrappedObject.getType(), InternalEntityLegacyConstants.HORSE_VARIANT_ZOMBIE);
                }
            } else if (wrappedObject instanceof Zombie) {
                if (((Zombie) wrappedObject).isVillager()) {
                    return new BukkitEntityType1_8(wrappedObject.getType(), InternalEntityLegacyConstants.ZOMBIE_VARIANT_VILLAGER);
                }
                if (Server.isVersion(1, 10)) {
                    var villager = Reflect.fastInvoke(ZombieAccessor.getMethodGetVillagerType1(), ClassStorage.getHandle(wrappedObject));
                    if (villager != null && villager == EnumZombieTypeAccessor.getFieldHUSK()) {
                        return new BukkitEntityType1_8(wrappedObject.getType(), InternalEntityLegacyConstants.ZOMBIE_VARIANT_HUSK);
                    }
                }
            } else if (wrappedObject instanceof Skeleton) {
                var variant = ((Skeleton) wrappedObject).getSkeletonType();
                switch (variant.name()) {
                    case "WITHER":
                        return new BukkitEntityType1_8(wrappedObject.getType(), InternalEntityLegacyConstants.SKELETON_VARIANT_WITHER);
                    case "STRAY": // not present in 1.8-1.9
                        return new BukkitEntityType1_8(wrappedObject.getType(), InternalEntityLegacyConstants.SKELETON_VARIANT_STRAY);
                }
            } else if (wrappedObject instanceof Guardian && ((Guardian) wrappedObject).isElder()) {
                return new BukkitEntityType1_8(wrappedObject.getType(), InternalEntityLegacyConstants.ELDER_GUARDIAN);
            } else {
                return new BukkitEntityType1_8(wrappedObject.getType());
            }
        }
        return new BukkitEntityType1_11(wrappedObject.getType());
    }

    @Override
    public @NotNull Location getLocation() {
        return Locations.wrapLocation(wrappedObject.getLocation());
    }

    @Override
    public @NotNull Vector3D getVelocity() {
        var platformVector = wrappedObject.getVelocity();
        return new Vector3D(platformVector.getX(), platformVector.getY(), platformVector.getZ());
    }

    @Override
    public void setVelocity(@NotNull Vector3D velocity) {
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
    public @NotNull CompletableFuture<@NotNull Boolean> teleport(@NotNull Location locationHolder) {
        return PaperLib.teleportAsync(wrappedObject, locationHolder.as(org.bukkit.Location.class));
    }

    @Override
    public @NotNull CompletableFuture<Void> teleport(@NotNull Location location, @NotNull Runnable callback, boolean forceCallback) {
        return teleport(location)
                .thenAccept(result -> {
                    if (result || forceCallback) {
                        callback.run();
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    @Override
    public boolean teleportSync(@NotNull Location location) {
        return wrappedObject.teleport(location.as(org.bukkit.Location.class));
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
    public @NotNull List<@NotNull Entity> getPassengers() {
        return wrappedObject.getPassengers()
                .stream()
                .map(Entities::wrapEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addPassenger(@NotNull Entity passenger) {
        var bukkitPassenger = passenger.as(org.bukkit.entity.Entity.class);
        try {
            return wrappedObject.addPassenger(bukkitPassenger);
        } catch (Throwable ignored) { // probably old version
            return wrappedObject.setPassenger(bukkitPassenger);
        }
    }

    @Override
    public boolean removePassenger(@NotNull Entity passenger) {
        try {
            return wrappedObject.removePassenger(passenger.as(org.bukkit.entity.Entity.class));
        } catch (Throwable ignored) { // probably old version
            return wrappedObject.eject();
        }
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
    public @NotNull UUID getUniqueId() {
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
    public @Nullable Entity getVehicle() {
        return Entities.wrapEntity(wrappedObject.getVehicle());
    }

    @Override
    public void setCustomName(@Nullable String name) {
        wrappedObject.setCustomName(name);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            wrappedObject.customName(name == null ? null : name.as(net.kyori.adventure.text.Component.class));
        } else {
            wrappedObject.setCustomName(name == null ? null : name.toLegacy());
        }
    }

    @Override
    public @Nullable Component getCustomName() {
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(wrappedObject.customName());
        } else {
            return Component.fromLegacy(wrappedObject.getCustomName());
        }
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

    @Override
    public boolean hasMetadata(@NotNull String metadata) {
        return BukkitEntityMetadataMapper.has(wrappedObject, metadata);
    }

    @Override
    public Object getMetadata(@NotNull String metadata) {
        return BukkitEntityMetadataMapper.get(wrappedObject, metadata, Object.class);
    }

    @Override
    public int getIntMetadata(@NotNull String metadata) {
        var i = BukkitEntityMetadataMapper.get(wrappedObject, metadata, Integer.class);
        return i == null ? 0 : i;
    }

    @Override
    public boolean getBooleanMetadata(@NotNull String metadata) {
        var b = BukkitEntityMetadataMapper.get(wrappedObject, metadata, Boolean.class);
        return b != null && b;
    }

    @Override
    public byte getByteMetadata(@NotNull String metadata) {
        var b = BukkitEntityMetadataMapper.get(wrappedObject, metadata, Byte.class);
        return b == null ? 0 : b;
    }

    @Override
    public long getLongMetadata(@NotNull String metadata) {
        var b = BukkitEntityMetadataMapper.get(wrappedObject, metadata, Long.class);
        return b == null ? 0 : b;
    }

    @Override
    public String getStringMetadata(@NotNull String metadata) {
        return BukkitEntityMetadataMapper.get(wrappedObject, metadata, String.class);
    }

    @Override
    public Component getComponentMetadata(@NotNull String metadata) {
        return BukkitEntityMetadataMapper.get(wrappedObject, metadata, Component.class);
    }

    @Override
    public Location getLocationMetadata(@NotNull String metadata) {
        return BukkitEntityMetadataMapper.get(wrappedObject, metadata, Location.class);
    }

    @Override
    public Color getColorMetadata(@NotNull String metadata) {
        return BukkitEntityMetadataMapper.get(wrappedObject, metadata, Color.class);
    }

    @Override
    public Vector3D getVectorMetadata(@NotNull String metadata) {
        return BukkitEntityMetadataMapper.get(wrappedObject, metadata, Vector3D.class);
    }

    @Override
    public Vector3Df getFloatVectorMetadata(@NotNull String metadata) {
        return BukkitEntityMetadataMapper.get(wrappedObject, metadata, Vector3Df.class);
    }

    @Override
    public void setMetadata(@NotNull String metadata, Object value) {
        BukkitEntityMetadataMapper.set(wrappedObject, metadata, value);
    }

    @Override
    public boolean holdsInventory() {
        return wrappedObject instanceof InventoryHolder;
    }

    @Override
    public @Nullable Container getInventory() {
        if (!(wrappedObject instanceof InventoryHolder)) {
            return null;
        }
        return ContainerFactory.wrapContainer(((InventoryHolder) wrappedObject).getInventory());
    }
}
