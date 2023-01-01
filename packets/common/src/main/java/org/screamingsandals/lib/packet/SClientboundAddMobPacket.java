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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundAddMobPacket extends AbstractPacket {
    private int entityId;
    private UUID uuid;
    private LocationHolder location;
    private Vector3D velocity;
    private int typeId;
    private final List<MetadataItem> metadata = new ArrayList<>();
    private byte headYaw;

    @Override
    public void write(PacketWriter writer) {
        if (writer.protocol() >= 759) {
            // 1.19: Switch to ClientboundAddEntityPacket
            writer.setCancelled(true);
            var packet = new SClientboundAddEntityPacket();
            packet.entityId(entityId);
            packet.uuid(uuid);
            packet.location(location);
            packet.velocity(velocity);
            packet.typeId(typeId);
            packet.headYaw(headYaw);
            writer.append(packet);
            var metadataPacket = new SClientboundSetEntityDataPacket();
            metadataPacket.entityId(entityId);
            metadataPacket.metadata().addAll(metadata);
            writer.append(metadataPacket);
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
                var packet = new SClientboundSetEntityDataPacket();
                packet.entityId(entityId);
                packet.metadata().addAll(metadata);
                writer.append(packet);
            }
        } else {
            writer.writeDataWatcherCollection(metadata);
        }
    }
}
