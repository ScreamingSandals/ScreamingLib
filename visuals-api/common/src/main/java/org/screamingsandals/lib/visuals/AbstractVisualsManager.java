package org.screamingsandals.lib.visuals;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.player.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.world.LocationHolder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractVisualsManager<T extends LocatableVisual<T>> {
    private final Map<UUID, T> activeVisuals = new ConcurrentHashMap<>();

    protected AbstractVisualsManager(Controllable controllable) {
        controllable.postEnable(() -> {
            EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, this::onLeave);
            EventManager.getDefaultEventManager().register(SPlayerMoveEvent.class, this::onMove);
            EventManager.getDefaultEventManager().register(SPlayerRespawnEvent.class, this::onRespawn);
            EventManager.getDefaultEventManager().register(SPlayerTeleportEvent.class, this::onTeleport);
            EventManager.getDefaultEventManager().register(SPlayerWorldChangeEvent.class, this::onWorldChange);
        });
        controllable.preDisable(this::destroy);
    }

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

    protected void destroy() {
        Map.copyOf(getActiveVisuals())
                .values()
                .forEach(Visual::destroy);
        activeVisuals.clear();
    }

    private void onLeave(SPlayerLeaveEvent event) {
        if (getActiveVisuals().isEmpty()) {
            return;
        }

        getActiveVisuals().forEach((key, visual) -> {
            if (visual.getViewers().contains(event.getPlayer())) {
                visual.removeViewer(event.getPlayer());
            }
            if (!visual.hasViewers()) {
                removeVisual(visual);
            }
        });
    }

    private void onMove(SPlayerMoveEvent event) {
        if (getActiveVisuals().isEmpty()) {
            return;
        }

        getActiveVisuals().forEach((key, visual) -> {
            try {
                if (!visual.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = visual.getViewers();
                final var hologramLocation = visual.getLocation();
                if (hologramLocation == null) {
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && hologramLocation.getWorld().equals(player.getLocation().getWorld())) {
                    if (event.getNewLocation().getDistanceSquared(hologramLocation) < viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) >= viewDistance) {
                        visual.onViewerAdded(player, false);
                    } else if (event.getNewLocation().getDistanceSquared(hologramLocation) >= viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        visual.onViewerRemoved(player, false);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onRespawn(SPlayerRespawnEvent event) {
        if (getActiveVisuals().isEmpty()) {
            return;
        }

        getActiveVisuals().forEach((key, visual) -> {
            try {
                if (!visual.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = visual.getViewers();
                final var hologramLocation = visual.getLocation();
                if (hologramLocation == null) {
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && event.getLocation().getWorld().equals(hologramLocation.getWorld())) {
                    if (player.getLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        Tasker.build(() -> {
                            visual.onViewerAdded(player, false);
                        }).delay(20, TaskerTime.TICKS).async().start();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onWorldChange(SPlayerWorldChangeEvent event) {
        if (getActiveVisuals().isEmpty()) {
            return;
        }

        getActiveVisuals().forEach((key, visual) -> {
            try {
                if (!visual.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = visual.getViewers();
                final var hologramLocation = visual.getLocation();
                if (hologramLocation == null) {
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && event.getFrom().equals(hologramLocation.getWorld())) {
                    if (player.getLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        Tasker.build(() -> {
                            visual.onViewerAdded(player, false);
                        }).delay(20, TaskerTime.TICKS).async().start();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onTeleport(SPlayerTeleportEvent event) {
        if (getActiveVisuals().isEmpty()
                || !event.getCurrentLocation().getWorld().equals(event.getNewLocation().getWorld())) {
            return;
        }

        getActiveVisuals().forEach((key, visual) -> {
            try {
                if (!visual.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = visual.getViewers();
                final var hologramLocation = visual.getLocation();
                if (hologramLocation == null) {
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && hologramLocation.getWorld().equals(player.getLocation().getWorld())) {
                    if (event.getNewLocation().getDistanceSquared(hologramLocation) < viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) >= viewDistance) {
                        Tasker.build(() -> {
                            visual.onViewerAdded(player, false);
                        }).delay(10, TaskerTime.TICKS).async().start();
                    } else if (event.getNewLocation().getDistanceSquared(hologramLocation) >= viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        Tasker.build(() -> {
                            visual.onViewerRemoved(player, false);
                        }).delay(10, TaskerTime.TICKS).async().start();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public abstract void fireVisualTouchEvent(PlayerWrapper sender, T visual, Object packet);

    public abstract T createVisual(UUID uuid, LocationHolder holder, boolean touchable);
}
