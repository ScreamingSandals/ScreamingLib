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

package org.screamingsandals.lib.impl.vanilla.packet;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.nms.accessors.network.ConnectionProtocol$CodecDataAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.ConnectionProtocol$PacketSetAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.ConnectionProtocolAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.PacketFlowAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.common.ClientboundDisconnectPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundAddEntityPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundAddMobPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundAddPlayerPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundAnimatePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundBlockDestructionPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundBlockEventPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundBlockUpdatePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundContainerClosePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundEntityEventPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundExplodePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundForgetLevelChunkPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundHurtAnimationPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundMoveEntityPacket$PosAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundMoveEntityPacket$PosRotAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundMoveEntityPacket$RotAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundPlayerAbilitiesPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundPlayerInfoRemovePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundPlayerInfoUpdatePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundRemoveEntitiesPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundRemoveMobEffectPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundResetScorePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundRotateHeadPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetCameraPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetCarriedItemPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetDisplayObjectivePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetEntityDataPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetEntityLinkPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetEntityMotionPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetEquipmentPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetExperiencePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetObjectivePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetPlayerTeamPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundSetScorePacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundTabListPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundTakeItemEntityPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundTeleportEntityPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.network.protocol.game.ClientboundUpdateMobEffectPacketAccessor;
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
        putTranslateSafely(ClientboundAddEntityPacket.class, ClientboundAddEntityPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundAddMobPacket.class, ClientboundAddMobPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundAddPlayerPacket.class, ClientboundAddPlayerPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundAnimatePacket.class, ClientboundAnimatePacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundAnimatePacket.ClientboundHurtAnimationPacket.class, ClientboundHurtAnimationPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundBlockDestructionPacket.class, ClientboundBlockDestructionPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundBlockEventPacket.class, ClientboundBlockEventPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundBlockUpdatePacket.class, ClientboundBlockUpdatePacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundContainerClosePacket.class, ClientboundContainerClosePacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundDisconnectPacket.class, ClientboundDisconnectPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundEntityEventPacket.class, ClientboundEntityEventPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundExplodePacket.class, ClientboundExplodePacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundForgetLevelChunkPacket.class, ClientboundForgetLevelChunkPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundMoveEntityPacket.Rot.class, ClientboundMoveEntityPacket$RotAccessor.TYPE.get());
        putTranslateSafely(ClientboundMoveEntityPacket.Pos.class, ClientboundMoveEntityPacket$PosAccessor.TYPE.get());
        putTranslateSafely(ClientboundMoveEntityPacket.PosRot.class, ClientboundMoveEntityPacket$PosRotAccessor.TYPE.get());
        putTranslateSafely(ClientboundPlayerAbilitiesPacket.class, ClientboundPlayerAbilitiesPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundPlayerInfoPacket.class, ClientboundPlayerInfoUpdatePacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundPlayerInfoPacket.PlayerInfoRemovePacket1_19_3.class, ClientboundPlayerInfoRemovePacketAccessor.TYPE.get()); // 1.19.3+
        putTranslateSafely(ClientboundRemoveEntitiesPacket.class, ClientboundRemoveEntitiesPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundRemoveMobEffectPacket.class, ClientboundRemoveMobEffectPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundRotateHeadPacket.class, ClientboundRotateHeadPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetCameraPacket.class, ClientboundSetCameraPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetCarriedItemPacket.class, ClientboundSetCarriedItemPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetDisplayObjectivePacket.class, ClientboundSetDisplayObjectivePacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetEntityDataPacket.class, ClientboundSetEntityDataPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetEntityLinkPacket.class, ClientboundSetEntityLinkPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetEntityMotionPacket.class, ClientboundSetEntityMotionPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetEquipmentPacket.class, ClientboundSetEquipmentPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetExperiencePacket.class, ClientboundSetExperiencePacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetObjectivePacket.class, ClientboundSetObjectivePacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetPlayerTeamPacket.class, ClientboundSetPlayerTeamPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetScorePacket.class, ClientboundSetScorePacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundSetScorePacket.ClientboundResetScorePacket1_20_3.class, ClientboundResetScorePacketAccessor.TYPE.get()); // 1.20.3+
        putTranslateSafely(ClientboundTabListPacket.class, ClientboundTabListPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundTakeItemEntityPacket.class, ClientboundTakeItemEntityPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundTeleportEntityPacket.class, ClientboundTeleportEntityPacketAccessor.TYPE.get());
        putTranslateSafely(ClientboundUpdateMobEffectPacket.class, ClientboundUpdateMobEffectPacketAccessor.TYPE.get());
    }

    private static void putTranslateSafely(@NotNull Class<? extends AbstractPacket> packetClass, @Nullable Class<?> clazz) {
        if (clazz != null) {
            PACKET_CLASS_TRANSLATE.put(packetClass, clazz);
        }
    }

    private static void putTranslateSafely(@NotNull Class<? extends AbstractPacket> packetClass, @Nullable Class<?> @NotNull... classes) {
        for (var clazz : classes) {
            if (clazz != null) {
                PACKET_CLASS_TRANSLATE.put(packetClass, clazz);
                return;
            }
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
            return null; // sorry AddMobPacket and AddPlayerPacket :(
        }

        // all mapped packets are just from play protocol, we don't rly need to touch handshaking, status or login protocol
        var playProtocol = ConnectionProtocolAccessor.CONST_PLAY.get();

        // all our mapped packets are just client bound. server bound listener is not implemented yet
        var outgoing = PacketFlowAccessor.CONST_CLIENTBOUND.get();

        Integer packetId;

        if (ConnectionProtocolAccessor.FIELD_FLOWS.get() == null) {
            // up to 1.14.4
            var outgoingMap = Reflect.getFieldResulted(playProtocol, ConnectionProtocolAccessor.FIELD_PACKETS.get())
                    .as(Map.class)
                    .get(outgoing);

            packetId = (Integer) Reflect.fastInvokeResulted(outgoingMap, "inverse")
                    .as(Map.class)
                    .get(vanillaClass);
        } else {
            // 1.15+
            var outgoingMap = Reflect.getFieldResulted(playProtocol, ConnectionProtocolAccessor.FIELD_FLOWS.get())
                    .as(Map.class)
                    .get(outgoing);

            if (ConnectionProtocol$CodecDataAccessor.TYPE.get() != null && ConnectionProtocol$CodecDataAccessor.TYPE.get().isInstance(outgoingMap)) {
                outgoingMap = Reflect.getField(outgoingMap, ConnectionProtocol$CodecDataAccessor.FIELD_PACKET_SET.get());
            }

            packetId = Reflect
                    .fastInvokeResulted(outgoingMap, ConnectionProtocol$PacketSetAccessor.METHOD_GET_ID.get(), vanillaClass)
                    .as(Integer.class);

        }

        ID_CACHE.put(packetClass, packetId);
        return packetId;
    }
}
