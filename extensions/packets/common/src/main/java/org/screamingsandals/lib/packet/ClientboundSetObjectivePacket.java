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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
@Builder(toBuilder = true)
public class ClientboundSetObjectivePacket extends AbstractPacket {
    private final @NotNull String objectiveKey;
    private final @Nullable Component title;
    private final @Nullable Type criteriaType;
    private final @NotNull Mode mode;
    @LimitedVersionSupport(">= 1.20.3")
    private final @Nullable ClientboundSetScorePacket.NumberFormat numberFormat;
    @LimitedVersionSupport(">= 1.20.3")
    @ApiStatus.Experimental
    private final @Nullable Component numberFormatComponent;

    @Override
    public void write(@NotNull PacketWriter writer) {
        writer.writeSizedString(objectiveKey);
        writer.writeByte((byte) mode.ordinal());

        if (mode == Mode.CREATE || mode == Mode.UPDATE) {
            Preconditions.checkNotNull(title);
            Preconditions.checkNotNull(criteriaType);
            if (writer.protocol() >= 390) {
                writer.writeComponent(title);
            } else {
                writer.writeSizedString(title.toLegacy());
            }
            if (writer.protocol() >= 349) {
                writer.writeVarInt(criteriaType.ordinal());

                if (writer.protocol() >= 756) {
                    writer.writeBoolean(numberFormat != null);
                    if (numberFormat != null) {
                        writer.writeVarInt(numberFormat.ordinal());
                        if (numberFormat == ClientboundSetScorePacket.NumberFormat.STYLED) {
                            // TODO: implement style-only tags (would this even be accepted by the client?)
                            writer.writeComponent(numberFormatComponent);
                        } else if (numberFormat == ClientboundSetScorePacket.NumberFormat.FIXED) {
                            writer.writeComponent(numberFormatComponent);
                        }
                    }
                }
            } else {
                writer.writeSizedString(criteriaType.name().toLowerCase(Locale.ROOT));
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
