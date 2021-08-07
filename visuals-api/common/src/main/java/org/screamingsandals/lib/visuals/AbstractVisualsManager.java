package org.screamingsandals.lib.visuals;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractVisualsManager<T extends TouchableVisual<T>> {
    private final Map<UUID, T> activeVisuals = new ConcurrentHashMap<>();

    protected AbstractVisualsManager(Controllable controllable) {
        controllable.child().postEnable(() -> {
            EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, this::onLeave);
            EventManager.getDefaultEventManager().register(SPlayerMoveEvent.class, this::onMove);
            EventManager.getDefaultEventManager().register(SPlayerRespawnEvent.class, this::onRespawn);
            EventManager.getDefaultEventManager().register(SPlayerTeleportEvent.class, this::onTeleport);
            EventManager.getDefaultEventManager().register(SPlayerWorldChangeEvent.class, this::onWorldChange);
        }).preDisable(this::destroy);
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
        getActiveVisuals()
                .values()
                .forEach(Visual::destroy);
        activeVisuals.clear();
    }

    public void onLeave(SPlayerLeaveEvent event) {
        if (activeVisuals.isEmpty()) {
            return;
        }

        activeVisuals.forEach((key, visual) -> {
            if (visual.getViewers().contains(event.getPlayer())) {
                visual.removeViewer(event.getPlayer());
            }
            if (!visual.hasViewers()) {
                removeVisual(visual);
            }
        });
    }

    public void onMove(SPlayerMoveEvent event) {
        if (activeVisuals.isEmpty()) {
            return;
        }

        activeVisuals.forEach((key, visual) -> {
            try {
                if (!visual.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = visual.getViewers();
                final var visualLocation = visual.getLocation();
                if (visualLocation == null) {
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && visualLocation.getWorld().equals(player.getLocation().getWorld())) {
                    if (event.getNewLocation().getDistanceSquared(visualLocation) < viewDistance
                            && event.getCurrentLocation().getDistanceSquared(visualLocation) >= viewDistance) {
                        visual.onViewerAdded(player, false);
                    } else if (event.getNewLocation().getDistanceSquared(visualLocation) >= viewDistance
                            && event.getCurrentLocation().getDistanceSquared(visualLocation) < viewDistance) {
                        visual.onViewerRemoved(player, false);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void onRespawn(SPlayerRespawnEvent event) {
        if (activeVisuals.isEmpty()) {
            return;
        }

        activeVisuals.forEach((key, visual) -> {
            try {
                if (!visual.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = visual.getViewers();
                final var visualLocation = visual.getLocation();
                if (visualLocation == null) {
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && event.getLocation().getWorld().equals(visualLocation.getWorld())) {
                    if (player.getLocation().getDistanceSquared(visualLocation) < viewDistance) {
                        Tasker.build(() -> {
                            if (player.isOnline()) {
                                visual.onViewerAdded(player, false);
                            }
                        }).async().delay(1L, TaskerTime.SECONDS).start();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void onWorldChange(SPlayerWorldChangeEvent event) {
        if (activeVisuals.isEmpty()) {
            return;
        }

        activeVisuals.forEach((key, visual) -> {
            try {
                if (!visual.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = visual.getViewers();
                final var visualLocation = visual.getLocation();
                if (visualLocation == null) {
                    return;
                }

                final var viewDistance = visual.getViewDistance();

                if (viewers.contains(player) && player.getLocation().getWorld().equals(visualLocation.getWorld())
                        && !event.getFrom().equals(visualLocation.getWorld())) {
                    if (player.getLocation().getDistanceSquared(visualLocation) < viewDistance) {
                        Tasker.build(() -> {
                            if (player.isOnline()) {
                                visual.onViewerAdded(player, false);
                            }
                        }).delay(1L, TaskerTime.SECONDS).async().start();
                    }
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void onTeleport(SPlayerTeleportEvent event) {
        if (activeVisuals.isEmpty()
                || !event.getCurrentLocation().getWorld().equals(event.getNewLocation().getWorld())) {
            return;
        }

        activeVisuals.forEach((key, visual) -> {
            try {
                if (!visual.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = visual.getViewers();
                final var visualLocation = visual.getLocation();
                if (visualLocation == null) {
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && visualLocation.getWorld().equals(player.getLocation().getWorld())) {
                    if (event.getNewLocation().getDistanceSquared(visualLocation) < viewDistance
                            && event.getCurrentLocation().getDistanceSquared(visualLocation) >= viewDistance) {
                        Tasker.build(() -> {
                            if (player.isOnline()) {
                                visual.onViewerAdded(player, false);
                            }
                        }).async().delay(1L, TaskerTime.SECONDS).start();
                    } else if (event.getNewLocation().getDistanceSquared(visualLocation) >= viewDistance
                            && event.getCurrentLocation().getDistanceSquared(visualLocation) < viewDistance) {
                        Tasker.build(() -> {
                            if (player.isOnline()) {
                                visual.onViewerRemoved(player, false);
                            }
                        }).async().delay(1L, TaskerTime.SECONDS).start();
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
