package org.screamingsandals.lib.bukkit.hologram;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.network.AutoPacketInboundListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.hologram.AbstractHologram;
import org.screamingsandals.lib.hologram.HologramManager;
import org.screamingsandals.lib.hologram.TextHologram;
import org.screamingsandals.lib.hologram.event.HologramTouchEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerLeaveEvent;
import org.screamingsandals.lib.player.event.SPlayerMoveEvent;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.UUID;

@Slf4j
@Service(dependsOn = {
        EventManager.class,
        PlayerMapper.class,
        LocationMapper.class
})
public class BukkitHologramManager extends HologramManager {

    public static void init(Plugin plugin, Controllable controllable) {
        HologramManager.init(() -> new BukkitHologramManager(plugin, controllable));
    }

    public BukkitHologramManager(Plugin plugin, Controllable controllable) {
        super(controllable);
        controllable.postEnable(() -> {
                new AutoPacketInboundListener(plugin) {
                    @Override
                    protected Object handle(Player sender, Object packet) {
                        if (ClassStorage.NMS.PacketPlayInUseEntity.isInstance(packet)) {
                            final var entityId = (int) ClassStorage.getField(ClassStorage.NMS.PacketPlayInUseEntity, "a,field_149567_a", packet);
                            getActiveHolograms().forEach((id, hologram) -> {
                                if (hologram.getClass().isInstance(BukkitTextHologram.class)) {
                                    final var textHologram = (BukkitTextHologram) hologram;
                                    if (textHologram.hasId(entityId)) {
                                        EventManager.fire(new HologramTouchEvent(PlayerMapper.wrapPlayer(plugin), hologram));
                                    }
                                }
                            });
                        }
                        return packet;
                    }
                };

                EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, this::onLeave);
                EventManager.getDefaultEventManager().register(SPlayerMoveEvent.class, this::onMove);
        });
    }

    @Override
    protected TextHologram textHologram0(UUID uuid, LocationHolder holder, boolean touchable) {
        return new BukkitTextHologram(uuid, holder, touchable);
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
            log.trace("Active holograms are empty!");
            return;
        }

        getActiveHolograms().forEach((key, hologram) -> {
            try {
                final var player = event.getPlayer();
                final var viewers = hologram.getViewers();
                final var maybeLocation = hologram.getLocation();
                if (maybeLocation.isEmpty()) {
                    log.trace("Location is empty");
                    return;
                }

                final var castedHologram = (AbstractHologram) hologram;
                final var viewDistance = hologram.getViewDistance();
                final var hologramLocation = maybeLocation.get();
                if (viewers.contains(player)
                        && hologramLocation.getWorldId().equals(player.getLocation().getWorldId())) {
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
}
