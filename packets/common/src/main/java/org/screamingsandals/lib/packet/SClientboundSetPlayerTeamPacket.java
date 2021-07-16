package org.screamingsandals.lib.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.Collection;

public interface SClientboundSetPlayerTeamPacket extends SPacket {

    SClientboundSetPlayerTeamPacket setTeamName(Component teamName);

    SClientboundSetPlayerTeamPacket setMode(Mode mode);

    SClientboundSetPlayerTeamPacket setFlags(boolean friendlyFire, boolean seeInvisible);

    SClientboundSetPlayerTeamPacket setTagVisibility(TagVisibility visibility);

    SClientboundSetPlayerTeamPacket setTeamColor(TextColor color);

    SClientboundSetPlayerTeamPacket setTeamPrefix(Component teamPrefix);

    SClientboundSetPlayerTeamPacket setTeamSuffix(Component teamSuffix);

    SClientboundSetPlayerTeamPacket setEntities(Collection<String> entities);

    SClientboundSetPlayerTeamPacket setDisplayName(Component displayName);

    SClientboundSetPlayerTeamPacket setCollisionRule(CollisionRule rule);

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
