package org.screamingsandals.lib.bukkit.npc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.network.AutoPacketInboundListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.player.*;
import org.screamingsandals.lib.hologram.AbstractHologram;
import org.screamingsandals.lib.npc.AbstractNPC;
import org.screamingsandals.lib.npc.NPCManager;
import org.screamingsandals.lib.npc.event.NPCInteractEvent;
import org.screamingsandals.lib.player.PlayerMapper;
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

@Service(dependsOn = {
        EventManager.class,
        PlayerMapper.class,
        LocationMapper.class,
        AbstractTaskInitializer.class
})
public class BukkitNPCManager extends NPCManager {
    private final Map<UUID, Long> cooldownMap = new HashMap<>();

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
                        final var entityId = (int) Reflect.getField(packet, "a,field_149567_a");
                        final var nmsEnum = Reflect.getField(packet, "b,action,field_149566_b");
                        final var attackEnum = Reflect.findEnumConstant(ClassStorage.NMS.PacketPlayInUseEntityActionType, "b,ATTACK");
                                                                                                                                        // temporary fix smh
                        NPCInteractEvent.InteractType interactType = nmsEnum == attackEnum  || nmsEnum.toString().equalsIgnoreCase("net.minecraft.network.protocol.game.PacketPlayInUseEntity$1@5327f6c8")
                                ?  NPCInteractEvent.InteractType.LEFT_CLICK : NPCInteractEvent.InteractType.RIGHT_CLICK;

                        for (var npc : getActiveNPCS().values()) {
                            final var id = npc.getEntityId();
                            if (id == entityId) {
                                synchronized (cooldownMap) {
                                    if (cooldownMap.containsKey(p.getUniqueId())) {
                                        final var lastClick = cooldownMap.get(p.getUniqueId());
                                        if (System.currentTimeMillis() - lastClick < npc.getClickCoolDown()) {
                                            break;
                                        }
                                    }
                                    cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                }
                                // use tasker to fire event synchronously
                                Tasker.build(() -> EventManager.fire(new NPCInteractEvent(PlayerMapper.wrapPlayer(p), npc, interactType))).afterOneTick().start();
                                break;
                            }
                        }
                    }
                    return packet;
                }
            };

            EventManager.getDefaultEventManager().register(SPlayerWorldChangeEvent.class, this::onWorldChange);
            EventManager.getDefaultEventManager().register(SPlayerTeleportEvent.class, this::onTeleport);
            EventManager.getDefaultEventManager().register(SPlayerMoveEvent.class, this::onMove);
            EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, this::onLeave);
            EventManager.getDefaultEventManager().register(SPlayerRespawnEvent.class, this::onRespawn);
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

    private void onRespawn(SPlayerRespawnEvent event) {
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

                final var castedNPC = (AbstractNPC) npc;
                final var viewDistance = npc.getViewDistance();
                if (viewers.contains(player)
                        && event.getLocation().getWorld().equals(npcLocation.getWorld())) {
                    if (player.getLocation().getDistanceSquared(npcLocation) < viewDistance) {
                        Tasker.build(() -> castedNPC.onViewerAdded(player)).delay(20, TaskerTime.TICKS).async().start();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onWorldChange(SPlayerWorldChangeEvent event) {
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
                final var npcLocation  = npc.getLocation();
                if (npcLocation == null) {
                    return;
                }

                final var castedNPC = (AbstractNPC) npc;
                final var viewDistance = npc.getViewDistance();
                if (viewers.contains(player)
                        && event.getFrom().equals(npcLocation.getWorld())) {
                    if (player.getLocation().getDistanceSquared(npcLocation) < viewDistance) {
                        Tasker.build(() -> castedNPC.onViewerAdded(player)).delay(20, TaskerTime.TICKS).async().start();
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onTeleport(SPlayerTeleportEvent event) {
        if (activeNPCS.isEmpty()
                || !event.getCurrentLocation().getWorld().equals(event.getNewLocation().getWorld())) {
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

                final var castedNPC = (AbstractNPC) npc;
                final var viewDistance = npc.getViewDistance();
                if (viewers.contains(player)
                        && npcLocation.getWorld().equals(player.getLocation().getWorld())) {
                    if (event.getNewLocation().getDistanceSquared(npcLocation) < viewDistance
                            && event.getCurrentLocation().getDistanceSquared(npcLocation) >= viewDistance) {
                        Tasker.build(() -> castedNPC.onViewerAdded(player)).delay(10, TaskerTime.TICKS).async().start();
                    } else if (event.getNewLocation().getDistanceSquared(npcLocation) >= viewDistance
                            && event.getCurrentLocation().getDistanceSquared(npcLocation) < viewDistance) {
                        Tasker.build(() -> castedNPC.onViewerRemoved(player)).delay(10, TaskerTime.TICKS).async().start();
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
