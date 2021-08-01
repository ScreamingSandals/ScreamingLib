package org.screamingsandals.lib.visuals;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.utils.nms.network.AutoPacketInboundListener;
import org.screamingsandals.lib.nms.accessors.ServerboundInteractPacketAccessor;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.reflect.Reflect;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VisualsTouchListener<T extends TouchableVisual<T> & LocatableVisual<T>> {
    private final Map<UUID, Long> coolDownMap = new HashMap<>();

    public VisualsTouchListener(AbstractVisualsManager<T> manager, Plugin plugin, Controllable controllable) {
        controllable.postEnable(() -> new AutoPacketInboundListener(plugin) {
            @Override
            protected Object handle(Player sender, Object packet) {
                if (ServerboundInteractPacketAccessor.getType().isInstance(packet)) {
                    final var entityId = Reflect.getFieldResulted(packet, ServerboundInteractPacketAccessor.getFieldEntityId()).as(int.class);
                    for (var entry : manager.getActiveVisuals().entrySet()) {
                        var visual = entry.getValue();
                        if (visual.hasId(entityId) && visual.isTouchable()) {
                            synchronized (coolDownMap) {
                                if (coolDownMap.containsKey(sender.getUniqueId())) {
                                    final var lastClick = coolDownMap.get(sender.getUniqueId());
                                    if (System.currentTimeMillis() - lastClick < 2L) {
                                        break;
                                    }
                                }
                                coolDownMap.put(sender.getUniqueId(), System.currentTimeMillis());
                            }
                            manager.fireVisualTouchEvent(PlayerMapper.wrapPlayer(sender), visual, packet);
                            break;
                        }
                    }
                }
                return packet;
            }
        });
    }
}
