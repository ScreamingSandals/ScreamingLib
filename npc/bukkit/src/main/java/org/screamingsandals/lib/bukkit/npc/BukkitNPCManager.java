package org.screamingsandals.lib.bukkit.npc;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.network.AutoPacketInboundListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.player.SPlayerLeaveEvent;
import org.screamingsandals.lib.event.player.SPlayerMoveEvent;
import org.screamingsandals.lib.npc.AbstractNPC;
import org.screamingsandals.lib.npc.NPCManager;
import org.screamingsandals.lib.npc.event.NPCTouchEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

@Service(dependsOn = {
        EventManager.class,
        PlayerMapper.class,
        LocationMapper.class,
        AbstractTaskInitializer.class
})
public class BukkitNPCManager extends NPCManager {
    public static void init(Plugin plugin, Controllable controllable) {
        BukkitNPCManager.init(() -> new BukkitNPCManager(plugin, controllable));
    }

    protected BukkitNPCManager(Plugin plugin, Controllable controllable) {
        super();
        controllable.postEnable(() -> {
            new AutoPacketInboundListener(plugin) {
                @Override
                protected Object handle(Player p, Object packet) {
                    if (ClassStorage.NMS.PacketPlayInUseEntity.isInstance(packet)) {
                        final var entityId = (int) Reflect.getField(ClassStorage.NMS.PacketPlayInUseEntity, "a,field_149567_a", packet);
                        getActiveNPCS()
                                .values()
                                .stream()
                                .filter(npc -> npc.getEntityId() == entityId)
                                .findAny()
                                .ifPresent(npc -> EventManager.fire(new NPCTouchEvent(PlayerMapper.wrapPlayer(p), npc)));
                    }
                    return packet;
                }
            };

            EventManager.getDefaultEventManager().register(SPlayerMoveEvent.class, this::onMove);
            EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, this::onLeave);
            Tasker.build(() -> getActiveNPCS().values()
                    .forEach(npc -> {
                        if (npc.shouldLookAtPlayer()) {
                            npc.getViewers().forEach(viewer -> npc.lookAtPlayer(viewer.getLocation(), viewer));
                        }
                    })).async().repeat(1L, TaskerTime.TICKS)
                            .start();

            controllable.preDisable(this::destroy);
        });
    }

    private void onMove(SPlayerMoveEvent event) {
        if (activeNPCS.isEmpty()) {
            return;
        }

        getActiveNPCS().forEach((key, npc) -> {
            try {
                if (!npc.hasViewers()) {
                    return;
                }

                final var player = event.getPlayer();
                final var viewers = npc.getViewers();
                final var npcLocation = npc.getLocation();
                if (npcLocation == null) {
                    return;
                }
                final var viewDistance = npc.getViewDistance();
                final var castedNPC = (AbstractNPC) npc;

                if (viewers.contains(player)
                        && npcLocation.getWorld().equals(player.getLocation().getWorld())) {
                    if (event.getNewLocation().getDistanceSquared(npcLocation) < viewDistance
                            && event.getCurrentLocation().getDistanceSquared(npcLocation) >= viewDistance) {
                        castedNPC.onViewerAdded(player);
                    } else if (event.getNewLocation().getDistanceSquared(npcLocation) >= viewDistance
                            && event.getCurrentLocation().getDistanceSquared(npcLocation) < viewDistance) {
                        castedNPC.onViewerRemoved(player);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onLeave(SPlayerLeaveEvent event) {
        getActiveNPCS()
                .values()
                .forEach(npc -> npc.removeViewer(event.getPlayer()));
    }

    @Override
    public AbstractNPC npc0(LocationHolder location) {
        return new BukkitNPC(location);
    }
}
