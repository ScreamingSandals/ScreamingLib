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

package org.screamingsandals.lib.packet;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
@Builder(toBuilder = true)
public class ClientboundAddMobPacket extends AbstractPacket {
    private final int entityId;
    private final @NotNull UUID uuid;
    private final @NotNull LocationHolder location;
    private final @NotNull Vector3D velocity;
    private final int typeId;
    @Builder.Default private final @NotNull List<@NotNull MetadataItem> metadata = List.of();
    private final byte headYaw;

    @Override
    public void write(@NotNull PacketWriter writer) {
        if (writer.protocol() >= 759) {
            // 1.19: Switch to ClientboundAddEntityPacket
            writer.setCancelled(true);
            var packet = ClientboundAddEntityPacket
                    .builder()
                    .entityId(entityId)
                    .uuid(uuid)
                    .location(location)
                    .velocity(velocity)
                    .typeId(typeId)
                    .headYaw(headYaw)
                    .build();
            writer.append(packet);
            if (!metadata.isEmpty()) {
                writer.append(ClientboundSetEntityDataPacket.builder().entityId(entityId).metadata(metadata).build());
            }
        }

        writer.writeVarInt(entityId);
        if (writer.protocol() >= 49) {
            writer.writeUuid(uuid);
        }
        if (writer.protocol() >= 301) {
            writer.writeVarInt(typeId);
        } else {
            writer.writeByte((byte) typeId);
        }
        if (writer.protocol() >= 100) {
            writer.writeVector(location);
        } else {
            writer.writeFixedPointVector(location);
        }
        writer.writeByteRotation(location);
        writer.writeByte(headYaw);
        writer.writeMotion(velocity);

        if (writer.protocol() >= 550) {
            if (!metadata.isEmpty()) {
                writer.append(ClientboundSetEntityDataPacket.builder().entityId(entityId).metadata(metadata).build());
            }
        } else {
            writer.writeDataWatcherCollection(metadata);
        }
    }
}
