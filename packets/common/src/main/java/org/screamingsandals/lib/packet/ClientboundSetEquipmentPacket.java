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
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.slot.EquipmentSlot;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
@Builder(toBuilder = true)
public class ClientboundSetEquipmentPacket extends AbstractPacket {
    private final int entityId;
    @Builder.Default private final @NotNull Map<@NotNull EquipmentSlot, ItemStack> slots = Map.of();

    @Override
    public void write(@NotNull PacketWriter writer) {
        writer.writeVarInt(entityId);
        var iterator = slots.entrySet().iterator();
        boolean hasNext = iterator.hasNext();
        boolean first = false;
        while (hasNext) {
            var next = iterator.next();
            hasNext = iterator.hasNext();
            if (writer.protocol() >= 732) {
                if (hasNext) {
                    writer.writeByte((byte) (writer.getEquipmentSlotId(next.getKey()) | 0x80));
                } else {
                    writer.writeByte((byte) writer.getEquipmentSlotId(next.getKey()));
                }
                writer.writeItem(next.getValue());
            } else {
                if (!first) {
                    if (writer.protocol() >= 49) {
                        writer.writeVarInt(writer.getEquipmentSlotId(next.getKey()));
                    } else {
                        writer.writeShort(writer.getEquipmentSlotId(next.getKey()));
                    }
                    writer.writeItem(next.getValue());
                    first = true;
                } else {
                    writer.append(
                            ClientboundSetEquipmentPacket.builder()
                                    .entityId(entityId)
                                    .slots(Map.of(next.getKey(), next.getValue()))
                                    .build()
                    );
                }
            }
        }
    }
}
