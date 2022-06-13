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
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.spectator.Component;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundPlayerInfoPacket extends AbstractPacket {
    private Action action;
    private List<PlayerInfoData> data;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(action.ordinal());
        writer.writeSizedCollection(data, playerInfoData -> {
            writer.writeUuid(playerInfoData.uuid());
            switch (action) {
                case ADD_PLAYER: {
                    writer.writeSizedString(playerInfoData.realName());
                    writer.writeSizedCollection(playerInfoData.properties(), property -> {
                        writer.writeSizedString(property.name());
                        writer.writeSizedString(property.value());
                        var hasSignature = property.hasSignature();
                        writer.writeBoolean(hasSignature);
                        if (hasSignature) {
                            writer.writeSizedString(property.signature());
                        }
                    });
                    writer.writeVarInt(playerInfoData.gameMode().id());
                    writer.writeVarInt(playerInfoData.latency());
                    writer.writeBoolean(playerInfoData.displayName() != null);
                    if (playerInfoData.displayName() != null) {
                        writer.writeComponent(playerInfoData.displayName());
                    }
                    if (writer.protocol() >= 759) {
                        writer.writeBoolean(false);
                    }
                    break;
                }
                case UPDATE_GAME_MODE: {
                    writer.writeVarInt(playerInfoData.gameMode().id());
                    break;
                }
                case UPDATE_LATENCY: {
                    writer.writeVarInt(playerInfoData.latency());
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    writer.writeBoolean(playerInfoData.displayName() != null);
                    if (playerInfoData.displayName() != null) {
                        writer.writeComponent(playerInfoData.displayName());
                    }
                    break;
                }
            }
        });
    }

    public enum Action {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    @Data
    public static class PlayerInfoData {
        private final UUID uuid;
        private final String realName;
        private final int latency;
        private final GameModeHolder gameMode;
        private final Component displayName;
        private final List<Property> properties;
    }

    @Data
    @RequiredArgsConstructor
    public static class Property {
        private final String name;
        private final String value;
        @Nullable
        private final String signature;

        public Property(String name, String value) {
            this(name, value, null);
        }

        public boolean hasSignature() {
            return signature != null;
        }
    }
}
