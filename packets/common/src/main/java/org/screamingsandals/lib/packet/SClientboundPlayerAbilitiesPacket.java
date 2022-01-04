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

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundPlayerAbilitiesPacket extends AbstractPacket {
    private boolean invulnerable;
    private boolean flying;
    private boolean canFly;
    private boolean canInstantlyBreak;
    private float flyingSpeed;
    private float walkingSpeed;

    @Override
    public void write(PacketWriter writer) {
        byte flags = 0;
        if (invulnerable) {
            flags |= 0x01;
        }
        if (flying) {
            flags |= 0x02;
        }
        if (canFly) {
            flags |= 0x04;
        }
        if (canInstantlyBreak) {
            flags |= 0x08;
        }

        writer.writeByte(flags);
        writer.writeFloat(flyingSpeed);
        writer.writeFloat(walkingSpeed);
    }
}
