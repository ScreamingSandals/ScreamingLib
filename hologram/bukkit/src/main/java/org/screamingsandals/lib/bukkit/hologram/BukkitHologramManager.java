package org.screamingsandals.lib.bukkit.hologram;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.network.AutoPacketInboundListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.hologram.AbstractHologram;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.hologram.HologramManager;
import org.screamingsandals.lib.hologram.event.HologramTouchEvent;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.nms.accessors.ServerboundInteractPacketAccessor;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.*;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service(dependsOn = {
        EventManager.class,
        PlayerMapper.class,
        LocationMapper.class,
        AbstractTaskInitializer.class,
        ItemFactory.class
})
public class BukkitHologramManager extends HologramManager {
    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    @Deprecated //INTERNAL USE ONLY!
    public static void init(Plugin plugin, Controllable controllable) {
        HologramManager.init(() -> new BukkitHologramManager(plugin, controllable));
    }

    protected BukkitHologramManager(Plugin plugin, Controllable controllable) {
        super(controllable);
        controllable.postEnable(() -> {
                new AutoPacketInboundListener(plugin) {
                    @Override
                    protected Object handle(Player sender, Object packet) {
                        if (ServerboundInteractPacketAccessor.getType().isInstance(packet)) {
                            final var entityId = (int) Reflect.getField(packet, ServerboundInteractPacketAccessor.getFieldEntityId());
                            for (var entry : getActiveHolograms().entrySet()) {
                                var id = entry.getKey();
                                var hologram = entry.getValue();
                                if (hologram instanceof BukkitHologram) {
                                    final var textHologram = (BukkitHologram) hologram;
                                    if (textHologram.hasId(entityId)) {
                                        synchronized (cooldownMap) {
                                            if (cooldownMap.containsKey(sender.getUniqueId())) {
                                                final var lastClick = cooldownMap.get(sender.getUniqueId());
                                                if (System.currentTimeMillis() - lastClick < 2L) {
                                                    break;
                                                }
                                            }
                                            cooldownMap.put(sender.getUniqueId(), System.currentTimeMillis());
                                        }
                                        // run synchronously
                                        Tasker.build(() -> EventManager.fire(new HologramTouchEvent(PlayerMapper.wrapPlayer(sender), hologram))).afterOneTick().start();
                                        break; // don't continue in iteration if we found the hologram
                                    }
                                }
                            };
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

    @Override
    protected Hologram hologram0(UUID uuid, LocationHolder holder, boolean touchable) {
        return new BukkitHologram(uuid, holder, touchable);
    }

    private void onLeave(SPlayerLeaveEvent event) {
        if (activeHolograms.isEmpty()) {
            return;
        }

        getActiveHolograms().forEach((key, hologram) -> {
            if (hologram.getViewers().contains(event.getPlayer())) {
                hologram.removeViewer(event.getPlayer());
            }
            if (!hologram.hasViewers()) {
                removeHologram(hologram);
            }
        });
    }

    private void onMove(SPlayerMoveEvent event) {
        if (activeHolograms.isEmpty()) {
            return;
        }

        getActiveHolograms().forEach((key, hologram) -> {
            try {
                if (!hologram.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = hologram.getViewers();
                final var hologramLocation = hologram.getLocation();
                if (hologramLocation == null) {
                    log.trace("Location is empty");
                    return;
                }

                final var castedHologram = (AbstractHologram) hologram;
                final var viewDistance = hologram.getViewDistance();
                if (viewers.contains(player)
                        && hologramLocation.getWorld().equals(player.getLocation().getWorld())) {
                    if (event.getNewLocation().getDistanceSquared(hologramLocation) < viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) >= viewDistance) {
                        log.trace("Adding player viewer to hologram.");
                        castedHologram.onViewerAdded(player, false);
                    } else if (event.getNewLocation().getDistanceSquared(hologramLocation) >= viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        castedHologram.onViewerRemoved(player, false);
                        log.trace("Removing player viewer from hologram");
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onRespawn(SPlayerRespawnEvent event) {
        if (activeHolograms.isEmpty()) {
            return;
        }

        getActiveHolograms().forEach((key, hologram) -> {
            try {
                if (!hologram.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = hologram.getViewers();
                final var hologramLocation = hologram.getLocation();
                if (hologramLocation == null) {
                    log.trace("Location is empty");
                    return;
                }

                final var castedHologram = (AbstractHologram) hologram;
                final var viewDistance = hologram.getViewDistance();
                if (viewers.contains(player)
                        && event.getLocation().getWorld().equals(hologramLocation.getWorld())) {
                    if (player.getLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        Tasker.build(() -> {
                            log.trace("Adding player viewer to hologram.");
                            castedHologram.onViewerAdded(player, false);
                        }).delay(20, TaskerTime.TICKS).async().start();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onWorldChange(SPlayerWorldChangeEvent event) {
        if (activeHolograms.isEmpty()) {
            return;
        }

        getActiveHolograms().forEach((key, hologram) -> {
            try {
                if (!hologram.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = hologram.getViewers();
                final var hologramLocation = hologram.getLocation();
                if (hologramLocation == null) {
                    log.trace("Location is empty");
                    return;
                }

                final var castedHologram = (AbstractHologram) hologram;
                final var viewDistance = hologram.getViewDistance();
                if (viewers.contains(player)
                        && event.getFrom().equals(hologramLocation.getWorld())) {
                    if (player.getLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        Tasker.build(() -> {
                            log.trace("Adding player viewer to hologram.");
                            castedHologram.onViewerAdded(player, false);
                        }).delay(20, TaskerTime.TICKS).async().start();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onTeleport(SPlayerTeleportEvent event) {
        if (activeHolograms.isEmpty()
                || !event.getCurrentLocation().getWorld().equals(event.getNewLocation().getWorld())) {
            return;
        }

        getActiveHolograms().forEach((key, hologram) -> {
            try {
                if (!hologram.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = hologram.getViewers();
                final var hologramLocation = hologram.getLocation();
                if (hologramLocation == null) {
                    log.trace("Location is empty");
                    return;
                }

                final var castedHologram = (AbstractHologram) hologram;
                final var viewDistance = hologram.getViewDistance();
                if (viewers.contains(player)
                        && hologramLocation.getWorld().equals(player.getLocation().getWorld())) {
                    if (event.getNewLocation().getDistanceSquared(hologramLocation) < viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) >= viewDistance) {
                        Tasker.build(() -> {
                            log.trace("Adding player viewer to hologram.");
                            castedHologram.onViewerAdded(player, false);
                        }).delay(10, TaskerTime.TICKS).async().start();
                    } else if (event.getNewLocation().getDistanceSquared(hologramLocation) >= viewDistance
                            && event.getCurrentLocation().getDistanceSquared(hologramLocation) < viewDistance) {
                        Tasker.build(() -> {
                            castedHologram.onViewerRemoved(player, false);
                            log.trace("Removing player viewer from hologram");
                        }).delay(10, TaskerTime.TICKS).async().start();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
