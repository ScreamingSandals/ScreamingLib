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
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.spectator.Component;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundPlayerInfoPacket extends AbstractPacket {
    private Action action;
    private List<@NotNull PlayerInfoData> data;

    @Override
    public void write(@NotNull PacketWriter writer) {
        if (writer.protocol() >= 761) {
            if (action == Action.REMOVE_PLAYER) {
                writer.setCancelled(true);
                writer.append(new PlayerInfoRemovePacket1_19_3(data.stream().map(PlayerInfoData::uuid).collect(Collectors.toUnmodifiableList())));
                return;
            }

            var set = new BitSet(6);
            if (Objects.requireNonNull(action) == Action.ADD_PLAYER) {
                set.set(0, 6);
            } else {
                var actionOrdinal = action.ordinal();
                set.set(actionOrdinal == 1 ? actionOrdinal + 1 : actionOrdinal + 2);
            }
            writer.writeBytes(Arrays.copyOf(set.toByteArray(), 1));
        } else {
            writer.writeVarInt(action.ordinal());
        }
        writer.writeSizedCollection(data, playerInfoData -> {
            writer.writeUuid(playerInfoData.uuid());
            switch (action) {
                case ADD_PLAYER: {
                    writer.writeSizedString(playerInfoData.realName());
                    writer.writeSizedCollection(playerInfoData.properties(), property -> {
                        writer.writeSizedString(property.name());
                        writer.writeSizedString(property.value());
                        var signature = property.signature();
                        writer.writeBoolean(signature != null);
                        if (signature != null) {
                            writer.writeSizedString(signature);
                        }
                    });
                    if (writer.protocol() >= 761) {
                        writer.writeBoolean(false);
                    }
                    writer.writeVarInt(playerInfoData.gameMode().id());
                    if (writer.protocol() >= 761) {
                        writer.writeBoolean(playerInfoData.listed());
                    }
                    writer.writeVarInt(playerInfoData.latency());
                    writer.writeBoolean(playerInfoData.displayName() != null);
                    if (playerInfoData.displayName() != null) {
                        writer.writeComponent(playerInfoData.displayName());
                    }
                    if (writer.protocol() == 759 || writer.protocol() == 760) {
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
        private final @NotNull UUID uuid;
        private final @NotNull String realName;
        private final int latency;
        private final @NotNull GameModeHolder gameMode;
        private final @Nullable Component displayName;
        private final @NotNull List<@NotNull Property> properties;
        private final boolean listed;
    }

    @Data
    @RequiredArgsConstructor
    public static class Property {
        private final @NotNull String name;
        private final @NotNull String value;
        private final @Nullable String signature;

        public Property(@NotNull String name, @NotNull String value) {
            this(name, value, null);
        }

        public boolean hasSignature() {
            return signature != null;
        }
    }

    @ApiStatus.Internal
    @RequiredArgsConstructor
    public static class PlayerInfoRemovePacket1_19_3 extends AbstractPacket {
        private final @NotNull List<@NotNull UUID> uuids;

        @Override
        public void write(@NotNull PacketWriter writer) {
            writer.writeSizedCollection(uuids, writer::writeUuid);
        }
    }
}
