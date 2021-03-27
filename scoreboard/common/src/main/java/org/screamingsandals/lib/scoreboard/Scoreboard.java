package org.screamingsandals.lib.scoreboard;

import org.screamingsandals.lib.scoreboard.team.ScoreboardTeam;
import org.screamingsandals.lib.visuals.DatableVisual;
import org.screamingsandals.lib.visuals.LinedVisual;

import java.util.*;

public interface Scoreboard extends LinedVisual<Scoreboard>, DatableVisual<Scoreboard> {

    /**
     * Creates new Scoreboard.
     *
     * @return created Scoreboard
     */
    static Scoreboard of() {
        return ScoreboardManager.scoreboard();
    }

    Collection<ScoreboardTeam> getTeams();

    Optional<ScoreboardTeam> getTeam(String identifier);
}