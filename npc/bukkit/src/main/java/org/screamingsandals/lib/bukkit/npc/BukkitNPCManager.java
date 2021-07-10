package org.screamingsandals.lib.bukkit.npc;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.network.AutoPacketInboundListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.npc.AbstractNPC;
import org.screamingsandals.lib.npc.NPCManager;
import org.screamingsandals.lib.npc.event.NPCTouchEvent;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerLeaveEvent;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;

@Service
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
                        NPCManager.getActiveNPCS()
                                .values()
                                .stream()
                                .filter(npc -> npc.getEntityId() == entityId)
                                .findAny()
                                .ifPresent(npc -> EventManager.fire(new NPCTouchEvent(PlayerMapper.wrapPlayer(p), npc)));
                    }
                    return packet;
                }
            };

            EventManager.getDefaultEventManager().register(SPlayerLeaveEvent.class, this::onLeave);
        });
    }

    private void onLeave(SPlayerLeaveEvent event) {
        NPCManager.getActiveNPCS()
                .values()
                .forEach(npc -> npc.removeViewer(event.getPlayer()));
    }

    @Override
    public AbstractNPC npc0(LocationHolder location) {
        return new BukkitNPC(location);
    }
}
