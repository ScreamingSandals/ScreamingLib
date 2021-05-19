package org.screamingsandals.lib.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.Collection;

public interface SPacketPlayOutScoreboardTeam {
    void setTeamName(Component teamName);

    void setMode(Mode mode);

    void setFriendlyFire(boolean friendlyFireEnabled);

    void setTagVisibility(TagVisibility visibility);

    void setTeamColor(TeamColor color);

    void setTeamPrefix(Component teamPrefix);

    void setTeamSuffix(Component teamSuffix);

    void setEntityCount(int entityCount);

    void setEntities(Collection<String> entities);

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

    @RequiredArgsConstructor
    enum TeamColor {
        BLACK(0),
        DARK_BLUE(1),
        DARK_GREEN(2),
        DARK_CYAN(3),
        DARK_RED(4),
        PURPLE(5),
        GOLD(6),
        GRAY(7),
        DARK_GRAY(8),
        BLUE(9),
        BRIGHT_GREEN(10),
        CYAN(11),
        RED(12),
        PINK(13),
        YELLOW(14),
        WHITE(15),
        OBFUSCATED(16),
        BOLD(17),
        STRIKETHROUGH(18),
        UNDERLINED(19),
        ITALIC(20),
        RESET(21);

        @Getter
        private final int color;
    }
}
