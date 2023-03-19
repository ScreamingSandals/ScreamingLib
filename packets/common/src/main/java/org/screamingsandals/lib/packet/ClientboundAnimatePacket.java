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
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
@Builder(toBuilder = true)
public class ClientboundAnimatePacket extends AbstractPacket {
    private final int entityId;
    private final @NotNull Animation animation;

    @Override
    public void write(@NotNull PacketWriter writer) {
        if (writer.protocol() >= 762 && animation.ordinal() == 1) {
            // different packet for this version
            writer.setCancelled(true);
            writer.append(new ClientboundHurtAnimationPacket(entityId, 0));
            return;
        }
        writer.writeVarInt(entityId);
        writer.writeByte((byte) animation.ordinal());
    }

    public enum Animation {
        SWING_MAIN_ARM,
        TAKE_DAMAGE,
        LEAVE_BED,
        SWING_OFF_HAND,
        CRITICAL_EFFECT,
        MAGICAL_CRITICAL_EFFECT
    }

    @ApiStatus.Internal
    @RequiredArgsConstructor
    public static class ClientboundHurtAnimationPacket extends AbstractPacket {
        private final int entityId;
        private final float yaw;

        @Override
        public void write(@NotNull PacketWriter writer) {
            writer.writeVarInt(entityId);
            writer.writeFloat(yaw);
        }
    }
}
