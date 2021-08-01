package org.screamingsandals.lib.visuals;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.utils.nms.network.AutoPacketInboundListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.player.*;
import org.screamingsandals.lib.nms.accessors.ServerboundInteractPacketAccessor;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.visual.AbstractVisualsManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public abstract class BukkitAbstractVisualsManager<T extends LocatableVisual<T> & TouchableVisual<T>> extends AbstractVisualsManager<T> {
    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    protected BukkitAbstractVisualsManager(Plugin plugin, Controllable controllable) {
        super(controllable);
        controllable.postEnable(() -> {
            new AutoPacketInboundListener(plugin) {
                @Override
                protected Object handle(Player sender, Object packet) {
                    if (ServerboundInteractPacketAccessor.getType().isInstance(packet)) {
                        final var entityId = Reflect.getFieldResulted(packet, ServerboundInteractPacketAccessor.getFieldEntityId()).as(int.class);
                        for (var entry : getActiveVisuals().entrySet()) {
                            var visual = entry.getValue();
                            if (visual.hasId(entityId) && visual.isTouchable()) {
                                synchronized (cooldownMap) {
                                    if (cooldownMap.containsKey(sender.getUniqueId())) {
                                        final var lastClick = cooldownMap.get(sender.getUniqueId());
                                        if (System.currentTimeMillis() - lastClick < 2L) {
                                            break;
                                        }
                                    }
                                    cooldownMap.put(sender.getUniqueId(), System.currentTimeMillis());
                                }
                                fireVisualTouchEvent(PlayerMapper.wrapPlayer(sender), visual, packet);
                                break;
                            }
                        }
                    }
                    return packet;
                }
            };

            EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, this::onLeave);
            EventManager.getDefaultEventManager().register(SPlayerMoveEvent.class, this::onMove);
            EventManager.getDefaultEventManager().register(SPlayerRespawnEvent.class, this::onRespawn);
            EventManager.getDefaultEventManager().register(SPlayerTeleportEvent.class, this::onTeleport);
            EventManager.getDefaultEventManager().register(SPlayerWorldChangeEvent.class, this::onWorldChange);
        });

        controllable.preDisable(this::destroy);
    }

    protected BukkitAbstractVisualsManager(Controllable controllable) {
        super(controllable);
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
                    log.trace("Location is empty");
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && hologramLocation.getWorld().equals(player.getLocation().getWorld())) {
                    if (event.getNewLocation().getDistanceSquared(hologramLocation) < viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) >= viewDistance) {
                        log.trace("Adding player viewer to visual.");
                        visual.onViewerAdded(player, false);
                    } else if (event.getNewLocation().getDistanceSquared(hologramLocation) >= viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        visual.onViewerRemoved(player, false);
                        log.trace("Removing player viewer from visual!");
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
                    log.trace("Location is empty");
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && event.getLocation().getWorld().equals(hologramLocation.getWorld())) {
                    if (player.getLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        Tasker.build(() -> {
                            log.trace("Adding player viewer to visual!.");
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
                    log.trace("Location is empty");
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && event.getFrom().equals(hologramLocation.getWorld())) {
                    if (player.getLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        Tasker.build(() -> {
                            log.trace("Adding player viewer to visual!.");
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
                    log.trace("Location is empty");
                    return;
                }

                final var viewDistance = visual.getViewDistance();
                if (viewers.contains(player)
                        && hologramLocation.getWorld().equals(player.getLocation().getWorld())) {
                    if (event.getNewLocation().getDistanceSquared(hologramLocation) < viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) >= viewDistance) {
                        Tasker.build(() -> {
                            log.trace("Adding player viewer to visual!.");
                            visual.onViewerAdded(player, false);
                        }).delay(10, TaskerTime.TICKS).async().start();
                    } else if (event.getNewLocation().getDistanceSquared(hologramLocation) >= viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        Tasker.build(() -> {
                            visual.onViewerRemoved(player, false);
                            log.trace("Removing player viewer from visual!");
                        }).delay(10, TaskerTime.TICKS).async().start();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public abstract void fireVisualTouchEvent(PlayerWrapper sender, T visual, Object packet);
}
