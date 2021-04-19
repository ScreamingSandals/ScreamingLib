package org.screamingsandals.lib.sidebar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.sidebar.team.ScoreboardTeam;
import org.screamingsandals.lib.visuals.Visual;

import java.util.Collection;
import java.util.Optional;

public interface TeamedSidebar<T extends TeamedSidebar<T>> extends Visual<T> {

    Collection<ScoreboardTeam> getTeams();

    Optional<ScoreboardTeam> getTeam(String identifier);

    ScoreboardTeam team(String identifier);

    T removeTeam(String identifier);

    T removeTeam(ScoreboardTeam scoreboardTeam);

    T title(ComponentLike title);

    T title(Component title);
}
