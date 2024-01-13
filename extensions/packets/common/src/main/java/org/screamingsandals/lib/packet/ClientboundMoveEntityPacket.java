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

import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.math.Vector3D;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public abstract class ClientboundMoveEntityPacket extends AbstractPacket {
    private final int entityId;
    private final boolean onGround;

    @Override
    public void write(@NotNull PacketWriter writer) {
        writer.writeVarInt(entityId);
        if (this instanceof Pos) {
            writer.writeMove(((Pos) this).delta);
        } else if (this instanceof Rot) {
            writer.writeByteRotation(((Rot) this).yaw, ((Rot) this).pitch);
        } else if (this instanceof PosRot) {
            writer.writeMove(((Pos) this).delta);
            writer.writeByteRotation(((PosRot) this).yaw, ((PosRot) this).pitch);
        }
        writer.writeBoolean(onGround);
    }

    @EqualsAndHashCode(callSuper = true)
    @Getter
    @ToString(callSuper = true)
    @Accessors(chain = true, fluent = true)
    public static final class Rot extends ClientboundMoveEntityPacket {
        private final float yaw;
        private final float pitch;

        @Builder
        private Rot(int entityId, boolean onGround, float yaw, float pitch) {
            super(entityId, onGround);
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Getter
    @ToString(callSuper = true)
    @Accessors(chain = true, fluent = true)
    public static final class Pos extends ClientboundMoveEntityPacket {
        private final @NotNull Vector3D delta;

        @Builder
        private Pos(int entityId, boolean onGround, @NotNull Vector3D delta) {
            super(entityId, onGround);
            this.delta = delta;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Getter
    @ToString(callSuper = true)
    @Accessors(chain = true, fluent = true)
    public static final class PosRot extends ClientboundMoveEntityPacket {
        private final @NotNull Vector3D delta;
        private final float yaw;
        private final float pitch;

        @Builder
        private PosRot(int entityId, boolean onGround, @NotNull Vector3D delta, float yaw, float pitch) {
            super(entityId, onGround);
            this.delta = delta;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }
}
