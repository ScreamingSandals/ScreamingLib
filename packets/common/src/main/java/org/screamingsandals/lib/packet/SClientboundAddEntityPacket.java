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

import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundAddEntityPacket extends AbstractPacket {
    private int entityId;
    private UUID uuid;
    private LocationHolder location;
    private Vector3D velocity;
    private int typeId;
    private int data;
    private byte headYaw;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean headYawSet;

    public void headYaw(byte headYaw) {
        this.headYaw = headYaw;
        this.headYawSet = true;
    }

    @Override
    public void write(@NotNull PacketWriter writer) {
        writer.writeVarInt(entityId);
        if (writer.protocol() >= 49) {
            writer.writeUuid(uuid);
        }
        if (writer.protocol() >= 458) {
            writer.writeVarInt(typeId);
        } else {
            writer.writeByte((byte) typeId); // TODO: Object ID was used, not EntityType id
        }
        if (writer.protocol() >= 100) {
            writer.writeVector(location);
        } else {
            writer.writeFixedPointVector(location);
        }
        writer.writeByteRotation(location);
        if (writer.protocol() >= 759) {
            writer.writeByte(headYawSet ? headYaw : (byte) (location.getYaw() * 256 / 360));
            writer.writeVarInt(data);
        } else {
            writer.writeInt(data);
        }
        if (data != 0 || writer.protocol() >= 49) {
            writer.writeMotion(velocity);
        }
    }
}
