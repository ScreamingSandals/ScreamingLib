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

package org.screamingsandals.lib.packet;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
@Builder(toBuilder = true)
public class ClientboundRemoveEntitiesPacket extends AbstractPacket {
    private final int @NotNull[] entityIds;

    @Override
    public void write(@NotNull PacketWriter writer) {
        if (writer.protocol() == 755) {
            // weird 1.17 version
            for (int i = 0; i < entityIds.length; i++) {
                if (i > 0) {
                    writer.append(new ClientboundRemoveEntitiesPacket(new int[] {entityIds[i]}));
                } else {
                    writer.writeVarInt(entityIds[i]);
                }
            }
        } else {
            writer.writeVarInt(entityIds.length);
            for (var entityId : entityIds) {
                writer.writeVarInt(entityId);
            }
        }
    }
}
