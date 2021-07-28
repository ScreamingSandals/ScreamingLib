package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
    private NamedTextColor teamColor; // only named colors are allowed
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
                writer.writeSizedString(LegacyComponentSerializer.legacySection().serialize(displayName));
            }
            if (writer.protocol() < 352) {
                writer.writeSizedString(LegacyComponentSerializer.legacySection().serialize(teamPrefix));
                writer.writeSizedString(LegacyComponentSerializer.legacySection().serialize(teamSuffix));
            }
            writer.writeByte((byte) ((friendlyFire ? 0x01 : 0) | (seeInvisible ? 0x02 : 0)));
            writer.writeSizedString(tagVisibility.enumName());
            if (writer.protocol() > 70) {
                writer.writeSizedString(collisionRule.enumName());
            }
            if (writer.protocol() < 352) {
                writer.writeByte((byte) (int) AdventureHelper.NAMED_TEXT_COLOR_ID_MAP.get(teamColor));
            } else {
                writer.writeVarInt(AdventureHelper.NAMED_TEXT_COLOR_ID_MAP.get(teamColor));
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
    };

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
}
