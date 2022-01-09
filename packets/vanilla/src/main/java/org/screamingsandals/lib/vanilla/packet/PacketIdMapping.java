/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.vanilla.packet;

import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketIdMapping {
    private static final Map<Class<? extends AbstractPacket>, Class<?>> PACKET_CLASS_TRANSLATE = new ConcurrentHashMap<>();
    private static final Map<Class<? extends AbstractPacket>, Integer> ID_CACHE = new ConcurrentHashMap<>();

    static {
        PACKET_CLASS_TRANSLATE.put(SClientboundAddEntityPacket.class, ClientboundAddEntityPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundAddMobPacket.class, ClientboundAddMobPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundAddPlayerPacket.class, ClientboundAddPlayerPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundAnimatePacket.class, ClientboundAnimatePacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundBlockDestructionPacket.class, ClientboundBlockDestructionPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundBlockEventPacket.class, ClientboundBlockEventPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundBlockUpdatePacket.class, ClientboundBlockUpdatePacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundContainerClosePacket.class, ClientboundContainerClosePacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundDisconnectPacket.class, ClientboundDisconnectPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundEntityEventPacket.class, ClientboundEntityEventPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundExplodePacket.class, ClientboundExplodePacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundForgetLevelChunkPacket.class, ClientboundForgetLevelChunkPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundMoveEntityPacket.Rot.class, ClientboundMoveEntityPacket_i_RotAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundMoveEntityPacket.Pos.class, ClientboundMoveEntityPacket_i_PosAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundMoveEntityPacket.PosRot.class, ClientboundMoveEntityPacket_i_PosRotAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundPlayerAbilitiesPacket.class, ClientboundPlayerAbilitiesPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundPlayerInfoPacket.class, ClientboundPlayerInfoPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundRemoveEntitiesPacket.class, ClientboundRemoveEntitiesPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundRemoveMobEffectPacket.class, ClientboundRemoveMobEffectPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundRotateHeadPacket.class, ClientboundRotateHeadPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetCameraPacket.class, ClientboundSetCameraPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetCarriedItemPacket.class, ClientboundSetCarriedItemPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetDisplayObjectivePacket.class, ClientboundSetDisplayObjectivePacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetEntityDataPacket.class, ClientboundSetEntityDataPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetEntityLinkPacket.class, ClientboundSetEntityLinkPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetEntityMotionPacket.class, ClientboundSetEntityMotionPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetEquipmentPacket.class, ClientboundSetEquipmentPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetExperiencePacket.class, ClientboundSetExperiencePacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetObjectivePacket.class, ClientboundSetObjectivePacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetPlayerTeamPacket.class, ClientboundSetPlayerTeamPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundSetScorePacket.class, ClientboundSetScorePacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundTabListPacket.class, ClientboundTabListPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundTakeItemEntityPacket.class, ClientboundTakeItemEntityPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundTeleportEntityPacket.class, ClientboundTeleportEntityPacketAccessor.getType());
        PACKET_CLASS_TRANSLATE.put(SClientboundUpdateMobEffectPacket.class, ClientboundUpdateMobEffectPacketAccessor.getType());
    }

    public static Integer getPacketId(Class<? extends AbstractPacket> packetClass) {
        Preconditions.checkNotNull(packetClass, "Cannot get packet id of null class!");

        final var cachedId = ID_CACHE.get(packetClass);
        if (cachedId != null) {
            return cachedId;
        }

        var vanillaClass = PACKET_CLASS_TRANSLATE.get(packetClass);

        // all mapped packets are just from play protocol, we don't rly need to touch handshaking, status or login protocol
        var playProtocol = ConnectionProtocolAccessor.getFieldPLAY();

        // all our mapped packets are just client bound. server bound listener is not implemented yet
        var outgoing = PacketFlowAccessor.getFieldCLIENTBOUND();

        Integer packetId;

        if (ConnectionProtocolAccessor.getFieldFlows() == null) {
            // up to 1.14.4
            var outgoingMap = Reflect.getFieldResulted(playProtocol, ConnectionProtocolAccessor.getFieldPackets())
                    .as(Map.class)
                    .get(outgoing);

            packetId = (Integer) Reflect.fastInvokeResulted(outgoingMap, "inverse")
                    .as(Map.class)
                    .get(vanillaClass);
        } else {
            // 1.15+
            var outgoingMap = Reflect.getFieldResulted(playProtocol, ConnectionProtocolAccessor.getFieldFlows())
                    .as(Map.class)
                    .get(outgoing);

            packetId =  Reflect
                    .fastInvokeResulted(outgoingMap, ConnectionProtocol_i_PacketSetAccessor.getMethodGetId1(), vanillaClass)
                    .as(Integer.class);
        }

        ID_CACHE.put(packetClass, packetId);
        return packetId;
    }
}
