package org.screamingsandals.lib.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.Collection;

public interface SPacketPlayOutScoreboardTeam extends SPacket {

    SPacketPlayOutScoreboardTeam setTeamName(Component teamName);

    SPacketPlayOutScoreboardTeam setMode(Mode mode);

    SPacketPlayOutScoreboardTeam setFlags(boolean friendlyFire, boolean seeInvisible);

    SPacketPlayOutScoreboardTeam setTagVisibility(TagVisibility visibility);

    SPacketPlayOutScoreboardTeam setTeamColor(TextColor color);

    SPacketPlayOutScoreboardTeam setTeamPrefix(Component teamPrefix);

    SPacketPlayOutScoreboardTeam setTeamSuffix(Component teamSuffix);

    SPacketPlayOutScoreboardTeam setEntities(Collection<String> entities);

    SPacketPlayOutScoreboardTeam setDisplayName(Component displayName);

    SPacketPlayOutScoreboardTeam setCollisionRule(CollisionRule rule);

    enum Mode {
        CREATE,
        REMOVE,
        UPDATE,
        ADD_ENTITY,
        REMOVE_ENTITY
    };

    @RequiredArgsConstructor
    enum TagVisibility {
        ALWAYS("always"),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam"),
        NEVER("never");

        @Getter
        private final String enumName;
    }

    @RequiredArgsConstructor
    enum CollisionRule {
        ALWAYS("always"),
        PUSH_OTHER_TEAMS("pushOtherTeams"),
        PUSH_OWN_TEAM("pushOwnTeam"),
        NEVER("never");

        @Getter
        private final String enumName;
    }
}
