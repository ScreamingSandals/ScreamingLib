/*
 * Copyright 2023 ScreamingSandals
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
import org.screamingsandals.lib.impl.nms.accessors.*;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class PacketIdMapping {
    private static final @NotNull Map<@NotNull Class<? extends AbstractPacket>, Class<?>> PACKET_CLASS_TRANSLATE = new ConcurrentHashMap<>();
    private static final @NotNull Map<@NotNull Class<? extends AbstractPacket>, Integer> ID_CACHE = new ConcurrentHashMap<>();

    static {
        putTranslateSafely(ClientboundAddEntityPacket.class, ClientboundAddEntityPacketAccessor.getType());
        putTranslateSafely(ClientboundAddMobPacket.class, ClientboundAddMobPacketAccessor.getType());
        putTranslateSafely(ClientboundAddPlayerPacket.class, ClientboundAddPlayerPacketAccessor.getType());
        putTranslateSafely(ClientboundAnimatePacket.class, ClientboundAnimatePacketAccessor.getType());
        putTranslateSafely(ClientboundAnimatePacket.ClientboundHurtAnimationPacket.class, ClientboundHurtAnimationPacketAccessor.getType());
        putTranslateSafely(ClientboundBlockDestructionPacket.class, ClientboundBlockDestructionPacketAccessor.getType());
        putTranslateSafely(ClientboundBlockEventPacket.class, ClientboundBlockEventPacketAccessor.getType());
        putTranslateSafely(ClientboundBlockUpdatePacket.class, ClientboundBlockUpdatePacketAccessor.getType());
        putTranslateSafely(ClientboundContainerClosePacket.class, ClientboundContainerClosePacketAccessor.getType());
        putTranslateSafely(ClientboundDisconnectPacket.class, ClientboundDisconnectPacketAccessor.getType());
        putTranslateSafely(ClientboundEntityEventPacket.class, ClientboundEntityEventPacketAccessor.getType());
        putTranslateSafely(ClientboundExplodePacket.class, ClientboundExplodePacketAccessor.getType());
        putTranslateSafely(ClientboundForgetLevelChunkPacket.class, ClientboundForgetLevelChunkPacketAccessor.getType());
        putTranslateSafely(ClientboundMoveEntityPacket.Rot.class, ClientboundMoveEntityPacket_i_RotAccessor.getType());
        putTranslateSafely(ClientboundMoveEntityPacket.Pos.class, ClientboundMoveEntityPacket_i_PosAccessor.getType());
        putTranslateSafely(ClientboundMoveEntityPacket.PosRot.class, ClientboundMoveEntityPacket_i_PosRotAccessor.getType());
        putTranslateSafely(ClientboundPlayerAbilitiesPacket.class, ClientboundPlayerAbilitiesPacketAccessor.getType());
        putTranslateSafely(ClientboundPlayerInfoPacket.class, ClientboundPlayerInfoPacketAccessor.getType()); // < 1.19.2
        putTranslateSafely(ClientboundPlayerInfoPacket.class, ClientboundPlayerInfoUpdatePacketAccessor.getType()); // 1.19.3+
        putTranslateSafely(ClientboundPlayerInfoPacket.PlayerInfoRemovePacket1_19_3.class, ClientboundPlayerInfoRemovePacketAccessor.getType()); // 1.19.3+
        putTranslateSafely(ClientboundRemoveEntitiesPacket.class, ClientboundRemoveEntitiesPacketAccessor.getType());
        putTranslateSafely(ClientboundRemoveMobEffectPacket.class, ClientboundRemoveMobEffectPacketAccessor.getType());
        putTranslateSafely(ClientboundRotateHeadPacket.class, ClientboundRotateHeadPacketAccessor.getType());
        putTranslateSafely(ClientboundSetCameraPacket.class, ClientboundSetCameraPacketAccessor.getType());
        putTranslateSafely(ClientboundSetCarriedItemPacket.class, ClientboundSetCarriedItemPacketAccessor.getType());
        putTranslateSafely(ClientboundSetDisplayObjectivePacket.class, ClientboundSetDisplayObjectivePacketAccessor.getType());
        putTranslateSafely(ClientboundSetEntityDataPacket.class, ClientboundSetEntityDataPacketAccessor.getType());
        putTranslateSafely(ClientboundSetEntityLinkPacket.class, ClientboundSetEntityLinkPacketAccessor.getType());
        putTranslateSafely(ClientboundSetEntityMotionPacket.class, ClientboundSetEntityMotionPacketAccessor.getType());
        putTranslateSafely(ClientboundSetEquipmentPacket.class, ClientboundSetEquipmentPacketAccessor.getType());
        putTranslateSafely(ClientboundSetExperiencePacket.class, ClientboundSetExperiencePacketAccessor.getType());
        putTranslateSafely(ClientboundSetObjectivePacket.class, ClientboundSetObjectivePacketAccessor.getType());
        putTranslateSafely(ClientboundSetPlayerTeamPacket.class, ClientboundSetPlayerTeamPacketAccessor.getType());
        putTranslateSafely(ClientboundSetScorePacket.class, ClientboundSetScorePacketAccessor.getType());
        putTranslateSafely(ClientboundTabListPacket.class, ClientboundTabListPacketAccessor.getType());
        putTranslateSafely(ClientboundTakeItemEntityPacket.class, ClientboundTakeItemEntityPacketAccessor.getType());
        putTranslateSafely(ClientboundTeleportEntityPacket.class, ClientboundTeleportEntityPacketAccessor.getType());
        putTranslateSafely(ClientboundUpdateMobEffectPacket.class, ClientboundUpdateMobEffectPacketAccessor.getType());
    }

    private static void putTranslateSafely(@NotNull Class<? extends AbstractPacket> packetClass, @Nullable Class<?> clazz) {
        if (clazz != null) {
            PACKET_CLASS_TRANSLATE.put(packetClass, clazz);
        }
    }

    public static Integer getPacketId(@NotNull Class<? extends AbstractPacket> packetClass) {
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
