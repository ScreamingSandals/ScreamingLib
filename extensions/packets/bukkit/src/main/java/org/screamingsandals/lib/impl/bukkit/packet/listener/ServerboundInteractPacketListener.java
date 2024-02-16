/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.bukkit.packet.listener;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.utils.Version;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.impl.nms.accessors.ServerboundInteractPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ServerboundInteractPacket$ActionAccessor;
import org.screamingsandals.lib.packet.event.SPacketEvent;
import org.screamingsandals.lib.packet.event.SPlayerServerboundInteractEvent;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.utils.PacketMethod;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;

@Service
public class ServerboundInteractPacketListener {

    private final Object ATTACK_ACTION_FIELD;

    public ServerboundInteractPacketListener() {
        ATTACK_ACTION_FIELD = Version.isVersion(1, 17)
                ? ServerboundInteractPacketAccessor.FIELD_ATTACK_ACTION.get()
                : ServerboundInteractPacket$ActionAccessor.FIELD_ATTACK.get();
    }

    @OnEvent
    public void onServerboundInteract(@NotNull SPacketEvent event) {
        if (event.getMethod() != PacketMethod.INBOUND) {
            return;
        }

        final var packet = event.getPacket();
        final var player = event.player();
        if (ServerboundInteractPacketAccessor.TYPE.get().isInstance(packet)) {
            final var actionField = Reflect.getField(packet, ServerboundInteractPacketAccessor.FIELD_ACTION.get());
            final var entityId = (int) Reflect.getField(packet, ServerboundInteractPacketAccessor.FIELD_ENTITY_ID.get());
            final var interactType = actionField == ATTACK_ACTION_FIELD ? InteractType.LEFT_CLICK : InteractType.RIGHT_CLICK;
            final var interactEvent = EventManager.fire(new SPlayerServerboundInteractEvent(player, entityId, interactType));
            event.cancelled(interactEvent.cancelled());
        }
    }
}
