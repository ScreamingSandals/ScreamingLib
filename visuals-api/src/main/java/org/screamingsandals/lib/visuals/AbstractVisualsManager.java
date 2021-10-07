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
import org.screamingsandals.lib.world.LocationHolder;
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
        removeVisual(visual.getUuid());
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
        coolDownMap.remove(event.getPlayer().getUuid());
        if (activeVisuals.isEmpty()) {
            return;
        }

        final var player = event.getPlayer();
        final var iterator = activeVisuals.entrySet().iterator();
        while (iterator.hasNext()) {
            final var visual = iterator.next().getValue();
            if (visual.getViewers().contains(player)) {
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

        final var player = event.getPlayer();
        final var newLocation = event.getNewLocation();
        final var oldLocation = event.getCurrentLocation();

        for (final var visual : activeVisuals.values()) {
            if (!visual.hasViewers()) {
                continue;
            }

            final var visualLocation = visual.getLocation();
            final var viewDistance = visual.getViewDistance();
            if (!visual.getViewers().contains(player) || !visualLocation.isWorldSame(newLocation)) {
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

        final var player = event.getPlayer();
        final var respawnLocation = event.getLocation();

        for (final var visual : activeVisuals.values()) {
            if (!visual.hasViewers()) {
                continue;
            }

            final var viewers = visual.getViewers();
            final var visualLocation = visual.getLocation();
            final var viewDistance = visual.getViewDistance();

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

        final var player = event.getPlayer();
        final var from = event.getFrom();

        for (final var visual : activeVisuals.values()) {
            if (!visual.hasViewers()) {
                continue;
            }

            final var viewers = visual.getViewers();
            final var visualLocation = visual.getLocation();
            final var viewDistance = visual.getViewDistance();

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

        final var currentLocation = event.getCurrentLocation();
        final var newLocation = event.getNewLocation();

        // SPlayerWorldChangeEvent handler will handle this.
        if (!currentLocation.isWorldSame(newLocation)) {
            return;
        }

        final var player = event.getPlayer();
        for (final var visual : activeVisuals.values()) {
            if (!visual.hasViewers()) {
                continue;
            }

            final var viewers = visual.getViewers();
            if (!viewers.contains(player)) {
                continue;
            }

            final var visualLocation = visual.getLocation();
            if (!visualLocation.isWorldSame(player.getLocation())) {
                continue;
            }

            final var viewDistance = visual.getViewDistance();

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
            if (visual.hasId(entityId) && visual.isTouchable()) {
                synchronized (coolDownMap) {
                    if (coolDownMap.containsKey(player.getUuid())) {
                        final var lastClick = coolDownMap.get(player.getUuid());
                        if (System.currentTimeMillis() - lastClick < visual.getClickCoolDown()) {
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
        }).delay(tickedDelay, TaskerTime.TICKS).start();
    }

    private void softRemoveViewer(T visual, PlayerWrapper player, long tickedDelay) {
        Tasker.build(() -> {
            if (!player.isOnline()) {
                return;
            }
            visual.onViewerRemoved(player, false);
        }).delay(tickedDelay, TaskerTime.TICKS).start();
    }

    public abstract void fireVisualTouchEvent(PlayerWrapper sender, T visual, InteractType interactType);
}
