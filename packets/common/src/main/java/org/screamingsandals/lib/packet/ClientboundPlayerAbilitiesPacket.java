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

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
@Builder(toBuilder = true)
public class ClientboundPlayerAbilitiesPacket extends AbstractPacket {
    private final boolean invulnerable;
    private final boolean flying;
    private final boolean canFly;
    private final boolean canInstantlyBreak;
    private final float flyingSpeed;
    private final float walkingSpeed;

    @Override
    public void write(@NotNull PacketWriter writer) {
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
