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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.AdventureHelper;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundSetPlayerTeamPacket extends AbstractPacket {
    private String teamKey;
    private Mode mode;

    // Create or Update
    private Component displayName;
    private boolean friendlyFire;
    private boolean seeInvisible;
    private TagVisibility tagVisibility;
    private CollisionRule collisionRule;
    private TeamColor teamColor = TeamColor.BLACK; // only named colors are allowed
    private Component teamPrefix;
    private Component teamSuffix;

    // Create, Add Entities, Remove Entities
    private Collection<String> entities;

    @Override
    public void write(PacketWriter writer) {
        writer.writeSizedString(teamKey);
        writer.writeByte((byte) mode.ordinal());
        if (mode == Mode.CREATE || mode == Mode.UPDATE) {
            if (writer.protocol() >= 390) {
                writer.writeComponent(displayName);
            } else {
                writer.writeSizedString(displayName.toLegacy());
            }
            if (writer.protocol() < 352) {
                writer.writeSizedString(teamPrefix.toLegacy());
                writer.writeSizedString(teamSuffix.toLegacy());
            }
            writer.writeByte((byte) ((friendlyFire ? 0x01 : 0) | (seeInvisible ? 0x02 : 0)));
            writer.writeSizedString(tagVisibility.enumName());
            if (writer.protocol() > 70) {
                writer.writeSizedString(collisionRule.enumName());
            }
            if (writer.protocol() < 352) {
                writer.writeByte((byte) teamColor.ordinal());
            } else {
                writer.writeVarInt(teamColor.ordinal());
            }
            if (writer.protocol() >= 375) {
                writer.writeComponent(teamPrefix);
                writer.writeComponent(teamSuffix);
            }
        }
        if (mode == Mode.CREATE || mode == Mode.ADD_ENTITY || mode == Mode.REMOVE_ENTITY) {
            writer.writeStringCollection(entities);
        }
    }

    public enum Mode {
        CREATE,
        REMOVE,
        UPDATE,
        ADD_ENTITY,
        REMOVE_ENTITY
    }

    ;

    @RequiredArgsConstructor
    public enum TagVisibility {
        ALWAYS("always"),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam"),
        NEVER("never");

        @Getter
        private final String enumName;
    }

    @RequiredArgsConstructor
    public enum CollisionRule {
        ALWAYS("always"),
        PUSH_OTHER_TEAMS("pushOtherTeams"),
        PUSH_OWN_TEAM("pushOwnTeam"),
        NEVER("never");

        @Getter
        private final String enumName;
    }

    public enum TeamColor {
        BLACK,
        DARK_BLUE,
        DARK_GREEN,
        DARK_AQUA,
        DARK_RED,
        DARK_PURPLE,
        GOLD,
        GRAY,
        DARK_GRAY,
        BLUE,
        GREEN,
        AQUA,
        RED,
        LIGHT_PURPLE,
        YELLOW,
        WHITE
    }
}
