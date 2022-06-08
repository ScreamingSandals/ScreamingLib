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
        putTranslateSafely(SClientboundAddEntityPacket.class, ClientboundAddEntityPacketAccessor.getType());
        putTranslateSafely(SClientboundAddMobPacket.class, ClientboundAddMobPacketAccessor.getType());
        putTranslateSafely(SClientboundAddPlayerPacket.class, ClientboundAddPlayerPacketAccessor.getType());
        putTranslateSafely(SClientboundAnimatePacket.class, ClientboundAnimatePacketAccessor.getType());
        putTranslateSafely(SClientboundBlockDestructionPacket.class, ClientboundBlockDestructionPacketAccessor.getType());
        putTranslateSafely(SClientboundBlockEventPacket.class, ClientboundBlockEventPacketAccessor.getType());
        putTranslateSafely(SClientboundBlockUpdatePacket.class, ClientboundBlockUpdatePacketAccessor.getType());
        putTranslateSafely(SClientboundContainerClosePacket.class, ClientboundContainerClosePacketAccessor.getType());
        putTranslateSafely(SClientboundDisconnectPacket.class, ClientboundDisconnectPacketAccessor.getType());
        putTranslateSafely(SClientboundEntityEventPacket.class, ClientboundEntityEventPacketAccessor.getType());
        putTranslateSafely(SClientboundExplodePacket.class, ClientboundExplodePacketAccessor.getType());
        putTranslateSafely(SClientboundForgetLevelChunkPacket.class, ClientboundForgetLevelChunkPacketAccessor.getType());
        putTranslateSafely(SClientboundMoveEntityPacket.Rot.class, ClientboundMoveEntityPacket_i_RotAccessor.getType());
        putTranslateSafely(SClientboundMoveEntityPacket.Pos.class, ClientboundMoveEntityPacket_i_PosAccessor.getType());
        putTranslateSafely(SClientboundMoveEntityPacket.PosRot.class, ClientboundMoveEntityPacket_i_PosRotAccessor.getType());
        putTranslateSafely(SClientboundPlayerAbilitiesPacket.class, ClientboundPlayerAbilitiesPacketAccessor.getType());
        putTranslateSafely(SClientboundPlayerInfoPacket.class, ClientboundPlayerInfoPacketAccessor.getType());
        putTranslateSafely(SClientboundRemoveEntitiesPacket.class, ClientboundRemoveEntitiesPacketAccessor.getType());
        putTranslateSafely(SClientboundRemoveMobEffectPacket.class, ClientboundRemoveMobEffectPacketAccessor.getType());
        putTranslateSafely(SClientboundRotateHeadPacket.class, ClientboundRotateHeadPacketAccessor.getType());
        putTranslateSafely(SClientboundSetCameraPacket.class, ClientboundSetCameraPacketAccessor.getType());
        putTranslateSafely(SClientboundSetCarriedItemPacket.class, ClientboundSetCarriedItemPacketAccessor.getType());
        putTranslateSafely(SClientboundSetDisplayObjectivePacket.class, ClientboundSetDisplayObjectivePacketAccessor.getType());
        putTranslateSafely(SClientboundSetEntityDataPacket.class, ClientboundSetEntityDataPacketAccessor.getType());
        putTranslateSafely(SClientboundSetEntityLinkPacket.class, ClientboundSetEntityLinkPacketAccessor.getType());
        putTranslateSafely(SClientboundSetEntityMotionPacket.class, ClientboundSetEntityMotionPacketAccessor.getType());
        putTranslateSafely(SClientboundSetEquipmentPacket.class, ClientboundSetEquipmentPacketAccessor.getType());
        putTranslateSafely(SClientboundSetExperiencePacket.class, ClientboundSetExperiencePacketAccessor.getType());
        putTranslateSafely(SClientboundSetObjectivePacket.class, ClientboundSetObjectivePacketAccessor.getType());
        putTranslateSafely(SClientboundSetPlayerTeamPacket.class, ClientboundSetPlayerTeamPacketAccessor.getType());
        putTranslateSafely(SClientboundSetScorePacket.class, ClientboundSetScorePacketAccessor.getType());
        putTranslateSafely(SClientboundTabListPacket.class, ClientboundTabListPacketAccessor.getType());
        putTranslateSafely(SClientboundTakeItemEntityPacket.class, ClientboundTakeItemEntityPacketAccessor.getType());
        putTranslateSafely(SClientboundTeleportEntityPacket.class, ClientboundTeleportEntityPacketAccessor.getType());
        putTranslateSafely(SClientboundUpdateMobEffectPacket.class, ClientboundUpdateMobEffectPacketAccessor.getType());
    }

    private static void putTranslateSafely(Class<? extends AbstractPacket> packetClass, Class<?> clazz) {
        if (clazz != null) {
            PACKET_CLASS_TRANSLATE.put(packetClass, clazz);
        }
    }

    public static Integer getPacketId(Class<? extends AbstractPacket> packetClass) {
        Preconditions.checkNotNull(packetClass, "Cannot get packet id of null class!");

        final var cachedId = ID_CACHE.get(packetClass);
        if (cachedId != null) {
            return cachedId;
        }

        var vanillaClass = PACKET_CLASS_TRANSLATE.get(packetClass);

        if (vanillaClass == null) {
            return null; // sorry AddMobPacket :(
        }

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
