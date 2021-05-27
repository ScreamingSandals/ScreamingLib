package org.screamingsandals.lib.common.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.Collection;

public interface SPacketPlayOutScoreboardTeam extends SPacket {
    void setTeamName(Component teamName);

    void setMode(Mode mode);

    void setFlags(boolean friendlyFire, boolean seeInvisible);

    void setTagVisibility(TagVisibility visibility);

    void setTeamColor(TextColor color);

    void setTeamPrefix(Component teamPrefix);

    void setTeamSuffix(Component teamSuffix);

    void setEntities(Collection<String> entities);

    void setDisplayName(Component displayName);

    void setCollisionRule(CollisionRule rule);

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
