package org.screamingsandals.lib.bukkit.packet.listener;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.nms.accessors.ServerboundInteractPacketAccessor;
import org.screamingsandals.lib.nms.accessors.ServerboundInteractPacket_i_ActionTypeAccessor;
import org.screamingsandals.lib.packet.event.SPacketEvent;
import org.screamingsandals.lib.packet.event.SPlayerServerboundInteractEvent;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.utils.PacketMethod;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;

@Service
public class ServerboundInteractPacketListener {
    private static final Object ATTACK_ACTION_FIELD = Reflect.getField(ServerboundInteractPacketAccessor.getFieldATTACK_ACTION());
    private static final Object ATTACK_FIELD = Reflect.getField(ServerboundInteractPacket_i_ActionTypeAccessor.getFieldATTACK());

    @OnEvent
    public void onServerboundInteract(SPacketEvent event) {
        if (event.getMethod() != PacketMethod.INBOUND) {
            return;
        }

        final var packet = event.getPacket();
        final var player = event.getPlayer();
        if (ServerboundInteractPacketAccessor.getType().isInstance(packet)) {
            final var nmsEnum = Reflect.getField(packet, ServerboundInteractPacketAccessor.getFieldAction());
            final var entityId = (int) Reflect.getField(packet, ServerboundInteractPacketAccessor.getFieldEntityId());

            var interactType = (nmsEnum == ATTACK_FIELD  ||
                    nmsEnum == ATTACK_ACTION_FIELD || (nmsEnum != null && nmsEnum.toString().equals("ATTACK")))
                    ?  InteractType.LEFT_CLICK : InteractType.RIGHT_CLICK;

            final var completableFuture = EventManager.fireAsync(new SPlayerServerboundInteractEvent(player, entityId, interactType));
            completableFuture.thenAccept(sPlayerServerboundInteractEvent -> event.setCancelled(sPlayerServerboundInteractEvent.isCancelled()));
        }
    }
}
