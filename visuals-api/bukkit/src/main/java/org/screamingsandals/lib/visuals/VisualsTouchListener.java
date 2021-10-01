package org.screamingsandals.lib.visuals;

import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.nms.accessors.ServerboundInteractPacketAccessor;
import org.screamingsandals.lib.packet.event.SPacketEvent;
import org.screamingsandals.lib.utils.PacketMethod;
import org.screamingsandals.lib.utils.reflect.Reflect;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VisualsTouchListener<T extends TouchableVisual<T>> {
    private final Map<UUID, Long> coolDownMap = new HashMap<>();
    private final AbstractVisualsManager<T> manager;

    public VisualsTouchListener(AbstractVisualsManager<T> manager) {
        this.manager = manager;
    }

    @OnEvent
    public void onInteract(SPacketEvent event) {
        if (event.getMethod() != PacketMethod.INBOUND) {
            return;
        }

        final var packet = event.getPacket();
        final var player = event.getPlayer();

        if (ServerboundInteractPacketAccessor.getType().isInstance(packet)) {
            final var entityId = (int) Reflect.getField(packet, ServerboundInteractPacketAccessor.getFieldEntityId());
            final var activeVisuals = manager.getActiveVisuals();

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
                    manager.fireVisualTouchEvent(player, visual, packet);
                    break;
                }
            }
        }
    }
}
