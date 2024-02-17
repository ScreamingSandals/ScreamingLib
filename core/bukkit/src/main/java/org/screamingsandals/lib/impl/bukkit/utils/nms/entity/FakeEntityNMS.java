/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.utils.nms.entity;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.*;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.List;

// copied from BedWars 0.2.2x
// TODO: maybe try to use less Bukkit API for manipulating with the entity?
// TODO: De-listener this class in favor of more global lazy-initialized listener
@Getter
public class FakeEntityNMS<E extends Entity> extends EntityNMS implements Listener {
    @Getter
    protected final @NotNull List<@NotNull Player> viewers = new ArrayList<>();
    private E entity;
    private boolean visible;

    @SuppressWarnings("unchecked")
    protected FakeEntityNMS(final @NotNull Object nmsEntity) {
        try {
            entity = (E) Reflect.fastInvoke(nmsEntity, "getBukkitEntity");
            this.handler = nmsEntity;
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            this.visible = visible;

            if (visible) {
                viewers.forEach(this::onViewerAdded);
            } else {
                viewers.forEach(viewer -> ClassStorage.sendNMSConstructedPacket(viewer, this.createDespawnPacket()));
            }
        }
    }

    public void setHealth(double health) {
        if (entity instanceof Damageable) {
            Damageable entity = (Damageable) this.entity;
            entity.setHealth(health * (entity.getMaxHealth() - 0.1D) + 0.1D);
            Object metadataPacket = createMetadataPacket();
            viewers.forEach(viewer -> ClassStorage.sendNMSConstructedPacket(viewer, metadataPacket));
        }
    }

    public void addViewer(@NotNull Player viewer) {
        if (viewers.isEmpty()) {
            Bukkit.getPluginManager().registerEvents(this, BukkitCore.getPlugin());
        }

        if (!viewers.contains(viewer)) {
            viewers.add(viewer);
            onViewerAdded(viewer);
        }
    }

    public void removeViewer(@NotNull Player viewer) {
        if (viewers.contains(viewer)) {
            onViewerRemoved(viewer);
            viewers.remove(viewer);
        }

        if (viewers.isEmpty()) {
            HandlerList.unregisterAll(this);
        }
    }

    public void onViewerAdded(@NotNull Player viewer) {
        if (visible) {
            ClassStorage.sendNMSConstructedPacket(viewer, createSpawnPacket());
            teleport(viewer, createPosition(viewer));
        }
    }

    public void onViewerRemoved(@NotNull Player viewer) {
        if (visible) {
            ClassStorage.sendNMSConstructedPacket(viewer, createDespawnPacket());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player viewer = event.getPlayer();
        if (viewers.contains(viewer) && visible) {
            teleport(viewer, createPosition(viewer));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player viewer = event.getPlayer();
        if (viewers.contains(viewer) && visible) {
            teleport(viewer, createPosition(event.getTo()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final Player viewer = event.getPlayer();
        if (viewers.contains(viewer) && visible) {
            teleport(viewer, createPosition(viewer));
        }
    }

    public @NotNull Location createPosition(final Player viewer) {
        return createPosition(viewer.getLocation());
    }

    public @NotNull Location createPosition(final Location position) {
        final Location clone = position.clone();
        clone.setPitch(clone.getPitch() - 30);
        clone.setYaw(clone.getYaw());
        clone.add(clone.getDirection().multiply(40));
        return clone;
    }

    public Object createSpawnPacket() {
        if (this.entity instanceof LivingEntity) {
            return Reflect.construct(ClientboundAddMobPacketAccessor.CONSTRUCTOR_0.get(), handler);
        }
        return null;
    }

    public Object createDespawnPacket() {
        return Reflect.construct(ClientboundRemoveEntitiesPacketAccessor.CONSTRUCTOR_0.get(), (Object) new int[] {this.entity.getEntityId()});
    }

    public Object createLocationPacket() {
        return Reflect.construct(ClientboundTeleportEntityPacketAccessor.CONSTRUCTOR_0.get(), handler);
    }

    public void teleport(Player viewer, Location location) {
        Reflect.fastInvoke(handler, EntityAccessor.METHOD_ABS_MOVE_TO.get(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
        ClassStorage.sendNMSConstructedPacket(viewer, createLocationPacket());
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        Object metadataPacket = createMetadataPacket();
        viewers.forEach(viewer -> ClassStorage.sendNMSConstructedPacket(viewer, metadataPacket));
    }

    public void metadata(final int position, final Object data) {
        final Object dataWatcher = getDataWatcher();
        if (dataWatcher != null) {
            // 1.8.8 ONLY
            Reflect.fastInvoke(dataWatcher, SynchedEntityDataAccessor.METHOD_WATCH.get(), position, data);
            Object metadataPacket = createMetadataPacket();
            viewers.forEach(viewer -> ClassStorage.sendNMSConstructedPacket(viewer, metadataPacket));
        }
    }

    public Object createMetadataPacket() {
        final Object dataWatcher = getDataWatcher();
        return Reflect.construct(ClientboundSetEntityDataPacketAccessor.CONSTRUCTOR_0.get(), entity.getEntityId(), dataWatcher, false);
    }
}
