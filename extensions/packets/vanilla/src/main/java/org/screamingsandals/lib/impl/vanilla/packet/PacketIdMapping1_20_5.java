/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl.vanilla.packet;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.impl.nms.accessors.network.ProtocolInfo$DetailsAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.ProtocolInfoAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.codec.IdDispatchCodecAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.PacketFlowAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.PacketTypeAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.SimpleUnboundProtocolAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.GameProtocolsAccessor;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.ClientboundAddEntityPacket;
import org.screamingsandals.lib.packet.ClientboundMoveEntityPacket;
import org.screamingsandals.lib.packet.ClientboundPlayerInfoPacket;
import org.screamingsandals.lib.packet.ClientboundRemoveEntitiesPacket;
import org.screamingsandals.lib.packet.ClientboundRemoveMobEffectPacket;
import org.screamingsandals.lib.packet.ClientboundRotateHeadPacket;
import org.screamingsandals.lib.packet.ClientboundSetDisplayObjectivePacket;
import org.screamingsandals.lib.packet.ClientboundSetEntityDataPacket;
import org.screamingsandals.lib.packet.ClientboundSetEntityMotionPacket;
import org.screamingsandals.lib.packet.ClientboundSetEquipmentPacket;
import org.screamingsandals.lib.packet.ClientboundSetExperiencePacket;
import org.screamingsandals.lib.packet.ClientboundSetObjectivePacket;
import org.screamingsandals.lib.packet.ClientboundSetPlayerTeamPacket;
import org.screamingsandals.lib.packet.ClientboundSetScorePacket;
import org.screamingsandals.lib.packet.ClientboundTeleportEntityPacket;
import org.screamingsandals.lib.packet.ClientboundUpdateMobEffectPacket;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@UtilityClass
public class PacketIdMapping1_20_5 {
    private static final @NotNull Map<@NotNull Class<? extends AbstractPacket>, String> PACKET_RESOURCE_LOCATION = new ConcurrentHashMap<>();
    private static final @NotNull Map<@NotNull Class<? extends AbstractPacket>, Integer> ID_CACHE = new ConcurrentHashMap<>();
    private static @Nullable Map<Object, Integer> map;

    // TODO: update the protocol to 1.21.2/3
    static {
        PACKET_RESOURCE_LOCATION.put(ClientboundAddEntityPacket.class, "minecraft:add_entity");
        PACKET_RESOURCE_LOCATION.put(ClientboundMoveEntityPacket.Rot.class, "minecraft:move_entity_rot");
        PACKET_RESOURCE_LOCATION.put(ClientboundMoveEntityPacket.Pos.class, "minecraft:move_entity_pos");
        PACKET_RESOURCE_LOCATION.put(ClientboundMoveEntityPacket.PosRot.class, "minecraft:move_entity_pos_rot");
        PACKET_RESOURCE_LOCATION.put(ClientboundPlayerInfoPacket.class, "minecraft:player_info_update");
        PACKET_RESOURCE_LOCATION.put(ClientboundPlayerInfoPacket.PlayerInfoRemovePacket1_19_3.class, "minecraft:player_info_remove");
        PACKET_RESOURCE_LOCATION.put(ClientboundRemoveEntitiesPacket.class, "minecraft:remove_entities");
        PACKET_RESOURCE_LOCATION.put(ClientboundRemoveMobEffectPacket.class, "minecraft:remove_mob_effect");
        PACKET_RESOURCE_LOCATION.put(ClientboundRotateHeadPacket.class, "minecraft:rotate_head");
        PACKET_RESOURCE_LOCATION.put(ClientboundSetDisplayObjectivePacket.class, "minecraft:set_display_objective");
        PACKET_RESOURCE_LOCATION.put(ClientboundSetEntityDataPacket.class, "minecraft:set_entity_data");
        PACKET_RESOURCE_LOCATION.put(ClientboundSetEntityMotionPacket.class, "minecraft:set_entity_motion");
        PACKET_RESOURCE_LOCATION.put(ClientboundSetEquipmentPacket.class, "minecraft:set_equipment");
        PACKET_RESOURCE_LOCATION.put(ClientboundSetExperiencePacket.class, "minecraft:set_experience");
        PACKET_RESOURCE_LOCATION.put(ClientboundSetObjectivePacket.class, "minecraft:set_objective");
        PACKET_RESOURCE_LOCATION.put(ClientboundSetPlayerTeamPacket.class, "minecraft:set_player_team");
        PACKET_RESOURCE_LOCATION.put(ClientboundSetScorePacket.class, "minecraft:set_score");
        PACKET_RESOURCE_LOCATION.put(ClientboundSetScorePacket.ClientboundResetScorePacket1_20_3.class, "minecraft:reset_score");
        PACKET_RESOURCE_LOCATION.put(ClientboundTeleportEntityPacket.class, Server.isVersion(1, 21, 2) ? "minecraft:entity_position_sync" : "minecraft:teleport_entity");
        PACKET_RESOURCE_LOCATION.put(ClientboundUpdateMobEffectPacket.class, "minecraft:update_mob_effect");
    }
    
    public static Integer getPacketId(@NotNull Class<? extends AbstractPacket> packetClass) {
        Preconditions.checkNotNull(packetClass, "Cannot get packet id of null class!");

        final var cachedId = ID_CACHE.get(packetClass);
        if (cachedId != null) {
            return cachedId;
        }

        var resourceLocation = PACKET_RESOURCE_LOCATION.get(packetClass);

        if (resourceLocation == null) {
            return null; // sorry outdated packets :(
        }

        if (map == null) {
            // all mapped packets are just from play protocol, we don't rly need to touch handshaking, status, login or configuration protocol
            @NotNull Object protocolInfo;
            if (GameProtocolsAccessor.CONST_CLIENTBOUND_TEMPLATE_1.get() != null) {
                // 1.21.5+
                var playProtocol = GameProtocolsAccessor.CONST_CLIENTBOUND_TEMPLATE_1.get();

                protocolInfo = Reflect.fastInvoke(playProtocol, SimpleUnboundProtocolAccessor.METHOD_BIND.get(), (Function<?, ?>) (input -> null));
            } else {
                var playProtocol = GameProtocolsAccessor.CONST_CLIENTBOUND_TEMPLATE.get();

                protocolInfo = Reflect.fastInvoke(playProtocol, ProtocolInfo$DetailsAccessor.METHOD_BIND.get(), (Function<?, ?>) (input -> null));
            }

            var codec = Reflect.fastInvoke(protocolInfo, ProtocolInfoAccessor.METHOD_CODEC.get());

            if (!IdDispatchCodecAccessor.TYPE.get().isInstance(codec)) {
                throw new UnsupportedOperationException("Unable to get Packet ID from object " + codec);
            }

            //noinspection unchecked
            map = (Map<Object, Integer>) Reflect.getFieldResulted(codec, IdDispatchCodecAccessor.FIELD_TO_ID.get()).as(Map.class);
        }

        var id = map.entrySet().stream()
                .filter(entry -> 
                        Reflect.fastInvoke(entry.getKey(), PacketTypeAccessor.METHOD_FLOW.get()) == PacketFlowAccessor.CONST_CLIENTBOUND.get() 
                                && resourceLocation.equals(Reflect.fastInvoke(entry.getKey(), PacketTypeAccessor.METHOD_ID.get()).toString())
                )
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new UnsupportedOperationException("Unable to get Packet ID for PLAY packet " + resourceLocation));

        ID_CACHE.put(packetClass, id);
        return id;
    }
}
