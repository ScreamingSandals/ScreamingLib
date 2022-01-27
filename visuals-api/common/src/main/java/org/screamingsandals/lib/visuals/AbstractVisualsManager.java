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

package org.screamingsandals.lib.visuals;

import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.*;
import org.screamingsandals.lib.packet.event.SPlayerServerboundInteractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractVisualsManager<T extends TouchableVisual<T>> {
    protected final Map<UUID, T> activeVisuals = new ConcurrentHashMap<>();
    private final Map<UUID, Long> coolDownMap = new HashMap<>();

    public void addVisual(UUID uuid, T visual) {
        activeVisuals.put(uuid, visual);
    }

    public void removeVisual(T visual) {
        removeVisual(visual.uuid());
    }

    public void removeVisual(UUID uuid) {
        activeVisuals.remove(uuid);
    }

    public Map<UUID, T> getActiveVisuals() {
        return Map.copyOf(activeVisuals);
    }

    @OnPreDisable
    public void destroy() {
        getActiveVisuals()
                .values()
                .forEach(Visual::destroy);
        activeVisuals.clear();
        coolDownMap.clear();
    }

    @OnEvent
    public void onLeave(SPlayerLeaveEvent event) {
        coolDownMap.remove(event.player().getUuid());
        if (activeVisuals.isEmpty()) {
            return;
        }

        final var player = event.player();
        final var iterator = activeVisuals.entrySet().iterator();
        while (iterator.hasNext()) {
            final var visual = iterator.next().getValue();
            if (visual.viewers().contains(player)) {
                visual.removeViewer(player);
            }

            if (!visual.hasViewers()) {
                iterator.remove();
            }
        }
    }

    @OnEvent
    public void onMove(SPlayerMoveEvent event) {
        if (activeVisuals.isEmpty()) {
            return;
        }

        final var player = event.player();
        final var newLocation = event.newLocation();
        final var oldLocation = event.currentLocation();

        for (final var visual : activeVisuals.values()) {
            if (!visual.hasViewers()) {
                continue;
            }

            final var visualLocation = visual.location();
            final var viewDistance = visual.viewDistance();
            if (!visual.viewers().contains(player) || !visualLocation.isWorldSame(newLocation)) {
                continue;
            }

            if (newLocation.isInRange(visualLocation, viewDistance) && oldLocation.outOfRange(visualLocation, viewDistance)) {
                // add viewer if new location is in range of visual, and old location is out of range.
                visual.onViewerAdded(player, false);
            } else if (newLocation.outOfRange(visualLocation, viewDistance) && oldLocation.isInRange(visualLocation, viewDistance)) {
                // remove viewer if new location is out of range of visual, and old location was in range.
                visual.onViewerRemoved(player, false);
            }
        }
    }

    @OnEvent
    public void onRespawn(SPlayerRespawnEvent event) {
        if (activeVisuals.isEmpty()) {
            return;
        }

        final var player = event.player();
        final var respawnLocation = event.location();

        for (final var visual : activeVisuals.values()) {
            if (!visual.hasViewers()) {
                continue;
            }

            final var viewers = visual.viewers();
            final var visualLocation = visual.location();
            final var viewDistance = visual.viewDistance();

            if (!viewers.contains(player)) {
                continue;
            }

            if (!respawnLocation.isWorldSame(visualLocation)) {
                return;
            }

            if (respawnLocation.isInRange(visualLocation, viewDistance)) {
                softAddViewer(visual, player, 20L);
            }
        }
    }

    @OnEvent
    public void onWorldChange(SPlayerWorldChangeEvent event) {
        if (activeVisuals.isEmpty()) {
            return;
        }

        final var player = event.player();
        final var from = event.from();

        for (final var visual : activeVisuals.values()) {
            if (!visual.hasViewers()) {
                continue;
            }

            final var viewers = visual.viewers();
            final var visualLocation = visual.location();
            final var viewDistance = visual.viewDistance();

            if (!viewers.contains(player)) {
                continue;
            }

            if (!player.getLocation().isWorldSame(visualLocation)) {
                continue;
            }

            if (from.equals(visualLocation.getWorld())) {
                continue;
            }

            if (player.getLocation().isInRange(visualLocation, viewDistance)) {
                softAddViewer(visual, player, 1L);
            }
        }
    }

    @OnEvent
    public void onTeleport(SPlayerTeleportEvent event) {
        if (activeVisuals.isEmpty()) {
            return;
        }

        final var currentLocation = event.currentLocation();
        final var newLocation = event.newLocation();

        // SPlayerWorldChangeEvent handler will handle this.
        if (!currentLocation.isWorldSame(newLocation)) {
            return;
        }

        final var player = event.player();
        for (final var visual : activeVisuals.values()) {
            if (!visual.hasViewers()) {
                continue;
            }

            final var viewers = visual.viewers();
            if (!viewers.contains(player)) {
                continue;
            }

            final var visualLocation = visual.location();
            if (!visualLocation.isWorldSame(player.getLocation())) {
                continue;
            }

            final var viewDistance = visual.viewDistance();

            if (newLocation.isInRange(visualLocation, viewDistance) && currentLocation.outOfRange(visualLocation, viewDistance)) {
                softAddViewer(visual, player, 20L);
            } else if (newLocation.outOfRange(visualLocation, viewDistance) && currentLocation.isInRange(visualLocation, viewDistance)) {
                softRemoveViewer(visual, player, 20L);
            }
        }
    }

    @OnEvent
    public void onInteract(SPlayerServerboundInteractEvent event) {
        final var player = event.getPlayer();
        final var entityId = event.getEntityId();

        for (var entry : activeVisuals.entrySet()) {
            var visual = entry.getValue();
            if (visual.hasId(entityId) && visual.touchable()) {
                synchronized (coolDownMap) {
                    if (coolDownMap.containsKey(player.getUuid())) {
                        final var lastClick = coolDownMap.get(player.getUuid());
                        if (System.currentTimeMillis() - lastClick < visual.clickCooldown()) {
                            break;
                        }
                    }
                    coolDownMap.put(player.getUuid(), System.currentTimeMillis());
                }
                Server.runSynchronously(() -> fireVisualTouchEvent(player, visual, event.getInteractType()));
                break;
            }
        }
    }

    private void softAddViewer(T visual, PlayerWrapper player, long tickedDelay) {
        Tasker.build(() -> {
            if (!player.isOnline()) {
                return;
            }
            visual.onViewerAdded(player, false);
        }).delay(tickedDelay, TaskerTime.TICKS).async().start();
    }

    private void softRemoveViewer(T visual, PlayerWrapper player, long tickedDelay) {
        Tasker.build(() -> {
            if (!player.isOnline()) {
                return;
            }
            visual.onViewerRemoved(player, false);
        }).delay(tickedDelay, TaskerTime.TICKS).async().start();
    }

    public abstract void fireVisualTouchEvent(PlayerWrapper sender, T visual, InteractType interactType);
}
