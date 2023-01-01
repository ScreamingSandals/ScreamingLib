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

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundUpdateMobEffectPacket extends AbstractPacket {
    private int entityId;
    private byte effect;
    private byte amplifier;
    private int duration;
    private boolean ambient;
    private boolean showParticles;
    private boolean showIcons;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(entityId);
        writer.writeByte(effect);
        writer.writeByte(amplifier);
        writer.writeVarInt(duration);

        if (writer.protocol() >= 210) {
            byte flags = 0;
            if (ambient) {
                flags |= 0x01;
            }
            if (showParticles) {
                flags |= 0x02;
            }
            if (showIcons) { // I think client shouldn't crash if it doesn't know this flag exists
                flags |= 0x04;
            }
            writer.writeByte(flags);
        } else {
            writer.writeBoolean(!showParticles);
        }
    }
}
