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
public class ClientboundTakeItemEntityPacket extends AbstractPacket {
    private final int entityId;
    private final int collectedEntityId;
    private final int amount;

    @Override
    public void write(@NotNull PacketWriter writer) {
        writer.writeVarInt(collectedEntityId);
        writer.writeVarInt(entityId);
        if (writer.protocol() >= 301) {
            writer.writeVarInt(amount);
        }
    }
}
