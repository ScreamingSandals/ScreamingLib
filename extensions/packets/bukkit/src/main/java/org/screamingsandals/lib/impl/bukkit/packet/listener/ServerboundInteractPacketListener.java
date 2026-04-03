/*
 * Copyright 2026 ScreamingSandals
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

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.impl.bukkit.utils.Version;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ServerboundAttackPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ServerboundInteractPacket$ActionAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ServerboundInteractPacketAccessor;
import org.screamingsandals.lib.packet.event.SPacketEvent;
import org.screamingsandals.lib.packet.event.SPlayerServerboundInteractEvent;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.utils.PacketMethod;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.reflect.Reflect;

@Service
@ServiceDependencies(dependsOn = {
        EventManager.class,
        Players.class,
})
public class ServerboundInteractPacketListener {

    private final boolean supportsUseUnknownEntityEvent = Reflect.has("com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent");
    private final Object ATTACK_ACTION_FIELD;
    private final boolean is26;

    public ServerboundInteractPacketListener() {
        if (supportsUseUnknownEntityEvent) {
            ATTACK_ACTION_FIELD = null;
            is26 = false;
        } else {
            if (!Version.isVersion(26, 1)) {
                ATTACK_ACTION_FIELD = Version.isVersion(1, 17)
                        ? ServerboundInteractPacketAccessor.CONST_ATTACK_ACTION.get()
                        : ServerboundInteractPacket$ActionAccessor.CONST_ATTACK.get();
                is26 = false;
            } else {
                ATTACK_ACTION_FIELD = null;
                is26 = true;
            }
        }
    }

    @OnEvent
    public void onEnable(@NotNull Plugin plugin) {
        if (supportsUseUnknownEntityEvent) {
            plugin.getServer().getPluginManager().registerEvents(new PaperListener(), plugin);
        } else {
            EventManager.getDefaultEventManager().register(SPacketEvent.class, this::onServerboundInteract);
        }
    }

    public void onServerboundInteract(@NotNull SPacketEvent event) {
        if (event.getMethod() != PacketMethod.INBOUND) {
            return;
        }

        final var packet = event.getPacket();
        final var player = event.player();
        if (ServerboundInteractPacketAccessor.TYPE.get().isInstance(packet)) {
            final var entityId = (int) Reflect.getField(packet, ServerboundInteractPacketAccessor.FIELD_ENTITY_ID.get());
            final InteractType interactType;
            if (!is26) {
                final var actionField = Reflect.getField(packet, ServerboundInteractPacketAccessor.FIELD_ACTION.get());
                interactType = actionField == ATTACK_ACTION_FIELD ? InteractType.LEFT_CLICK : InteractType.RIGHT_CLICK;
            } else {
                interactType = InteractType.RIGHT_CLICK;
            }
            final var interactEvent = EventManager.fire(new SPlayerServerboundInteractEvent(player, entityId, interactType));
            event.cancelled(interactEvent.cancelled());
        } else if (is26 && ServerboundAttackPacketAccessor.TYPE.get().isInstance(packet)) {
            final var entityId = (int) Reflect.fastInvoke(packet, ServerboundAttackPacketAccessor.METHOD_ENTITY_ID.get());
            final var interactEvent = EventManager.fire(new SPlayerServerboundInteractEvent(player, entityId, InteractType.LEFT_CLICK));
            event.cancelled(interactEvent.cancelled());
        }
    }

    public static class PaperListener implements Listener {
        @EventHandler
        public void onPlayerUseUnknownEntityEvent(@NotNull PlayerUseUnknownEntityEvent event) {
            EventManager.fire(new SPlayerServerboundInteractEvent(
                    Players.wrapPlayer(event.getPlayer()),
                    event.getEntityId(),
                    event.isAttack() ? InteractType.LEFT_CLICK : InteractType.RIGHT_CLICK
            ));
        }
    }
}
