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
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
@Builder(toBuilder = true)
public class ClientboundSetScorePacket extends AbstractPacket {
    private final @NotNull String entityName;
    private final @NotNull ScoreboardAction action;
    private final @NotNull String objectiveKey;
    private final int score;
    @LimitedVersionSupport(">= 1.20.3")
    private final @Nullable Component displayName;
    @LimitedVersionSupport(">= 1.20.3")
    private final @Nullable NumberFormat numberFormat;
    @LimitedVersionSupport(">= 1.20.3")
    @ApiStatus.Experimental
    private final @Nullable Component numberFormatComponent;


    @Override
    public void write(@NotNull PacketWriter writer) {
        if (writer.protocol() >= 765 && action == ScoreboardAction.REMOVE) {
            writer.setCancelled(true);
            writer.append(new ClientboundResetScorePacket1_20_3(entityName, objectiveKey));
            return;
        }

        writer.writeSizedString(entityName);
        if (writer.protocol() < 765) {
            writer.writeByte((byte) action.ordinal());
        }
        writer.writeSizedString(objectiveKey);
        if (action == ScoreboardAction.CHANGE) {
            writer.writeVarInt(score);
        }
        if (writer.protocol() >= 765) {
            writer.writeBoolean(displayName != null);
            if (displayName != null) {
                writer.writeComponent(displayName);
            }
            writer.writeBoolean(numberFormat != null);
            if (numberFormat != null) {
                writer.writeVarInt(numberFormat.ordinal());
                if (numberFormat == NumberFormat.STYLED) {
                    // TODO: implement style-only tags (would this even be accepted by the client?)
                    writer.writeComponent(numberFormatComponent);
                } else if (numberFormat == NumberFormat.FIXED) {
                    writer.writeComponent(numberFormatComponent);
                }
            }
        }
    }

    public enum ScoreboardAction {
        CHANGE,
        REMOVE
    }

    @LimitedVersionSupport(">= 1.20.3")
    public enum NumberFormat {
        BLANK,
        STYLED,
        FIXED
    }

    @ApiStatus.Internal
    @RequiredArgsConstructor
    @LimitedVersionSupport(">= 1.20.3")
    public static class ClientboundResetScorePacket1_20_3 extends AbstractPacket {
        private final @NotNull String entityName;
        private final @NotNull String objectiveKey;

        @Override
        public void write(@NotNull PacketWriter writer) {
            writer.writeSizedString(entityName);
            writer.writeBoolean(true);
            writer.writeSizedString(objectiveKey);
        }
    }
}
