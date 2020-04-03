package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.List;
import java.util.Map;

@Data
public class ScoreboardCreator {
    private Scoreboard scoreboard;

    public ScoreboardCreator() {
        scoreboard = new Scoreboard();
    }

    public static ScoreboardCreator get() {
        return new ScoreboardCreator();
    }

    public static ScoreboardCreator get(String scoreboardName) {
        ScoreboardCreator scoreboardCreator = get();
        scoreboardCreator.getScoreboard().getScoreboardHolder().setScoreboardName(scoreboardName);

        return scoreboardCreator;
    }

    public static ScoreboardCreator get(String scoreboardName, List<String> teams) {
        final ScoreboardCreator scoreboardCreator = get();
        final ScoreboardHolder scoreboardHolder = scoreboardCreator.getScoreboard().getScoreboardHolder();
        scoreboardHolder.setScoreboardName(scoreboardName);

        teams.forEach(scoreboardCreator::addTeam);
        return scoreboardCreator;
    }

    public Scoreboard create(String displayedName, DisplaySlot displaySlot, List<Map.Entry<String, Integer>> lines) {
        scoreboard.setDisplayedName(displayedName);
        scoreboard.setDisplaySlot(displaySlot);
        scoreboard.setLines(lines);

        scoreboard.paintAll();
        setScoreboard(scoreboard);

        return scoreboard;
    }

    public ScoreboardCreator setScoreboardName(String scoreboardName) {
        scoreboard.getScoreboardHolder().setScoreboardName(scoreboardName);
        return this;
    }

    public ScoreboardCreator addTeam(String name) {
        if (isTeamExists(name)) {
            scoreboard.getBukkitScoreboard().registerNewTeam(name);
        }
        return this;
    }

    public ScoreboardCreator setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        return this;
    }

    public boolean isTeamExists(String name) {
        return scoreboard.getBukkitScoreboard().getTeam(name) != null;
    }
}
