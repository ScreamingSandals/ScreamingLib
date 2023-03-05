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
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.Location;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
@Builder(toBuilder = true)
public class ClientboundAddEntityPacket extends AbstractPacket {
    private final int entityId;
    private final @NotNull UUID uuid;
    private final @NotNull Location location;
    private final @NotNull Vector3D velocity;
    private final int typeId;
    private final int data;
    private final @Nullable Byte headYaw;

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
            writer.writeByte(headYaw != null ? headYaw : (byte) (location.getYaw() * 256 / 360));
            writer.writeVarInt(data);
        } else {
            writer.writeInt(data);
        }
        if (data != 0 || writer.protocol() >= 49) {
            writer.writeMotion(velocity);
        }
    }
}
