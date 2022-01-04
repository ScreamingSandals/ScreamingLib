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

package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundSetObjectivePacket extends AbstractPacket {
    private String objectiveKey;
    private Component title;
    private Type criteriaType;
    private Mode mode;

    @Override
    public void write(PacketWriter writer) {
        writer.writeSizedString(objectiveKey);
        writer.writeByte((byte) mode.ordinal());

        if (mode == Mode.CREATE || mode == Mode.UPDATE) {
            if (writer.protocol() >= 390) {
                writer.writeComponent(title);
            } else {
                writer.writeSizedString(LegacyComponentSerializer.legacySection().serialize(title));
            }
            if (writer.protocol() >= 349) {
                writer.writeVarInt(criteriaType.ordinal());
            } else {
                writer.writeSizedString(criteriaType.name().toLowerCase());
            }
        }
    }

    public enum Type {
        INTEGER,
        HEARTS
    }

    public enum Mode {
        CREATE,
        DESTROY,
        UPDATE
    }
}
