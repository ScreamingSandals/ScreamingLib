package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
public class ScoreboardCreator {
    private Scoreboard scoreboard;

    public ScoreboardCreator() {
        scoreboard = new Scoreboard();
    }

    public static ScoreboardCreator create() {
        return new ScoreboardCreator();
    }

    public static ScoreboardCreator create(String scoreboardName) {
        final var scoreboardCreator = create();
        scoreboardCreator.getScoreboard().setName(scoreboardName);

        return scoreboardCreator;
    }

    public static ScoreboardCreator create(String scoreboardName, Map<String, ChatColor> teams) {
        final var scoreboardCreator = create(scoreboardName);
        teams.forEach(scoreboardCreator::addTeam);

        return scoreboardCreator;
    }

    public static ScoreboardCreator create(String scoreboardName, String displayedName, DisplaySlot displaySlot, TreeMap<Integer, String> lines) {
        final var scoreboardCreator = create(scoreboardName);
        final var scoreboard = scoreboardCreator.scoreboard;
        final var scoreboardHolder = scoreboard.getScoreboardHolder();

        scoreboardHolder.setDisplayedName(displayedName);
        scoreboardHolder.setDisplaySlot(displaySlot);
        scoreboardHolder.setLines(lines);

        return scoreboardCreator;
    }

    public ScoreboardCreator setScoreboardName(String scoreboardName) {
        scoreboard.setName(scoreboardName);
        return this;
    }

    public ScoreboardCreator addTeam(String name, ChatColor chatColor) {
        scoreboard.addTeam(name, chatColor);
        return this;
    }

    public ScoreboardCreator setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        return this;
    }

    public Scoreboard get() {
        scoreboard.paintAll();
        return scoreboard;
    }

    public static TreeMap<Integer, String> sortLines(List<String> fromConfig) {
        final var toReturn = new TreeMap<Integer, String>();

        for (int i = 0; i < fromConfig.size(); i++) {
            toReturn.put(i, fromConfig.get(i));
        }

        return toReturn;
    }

    public boolean isTeamExists(String name) {
        return scoreboard.isTeamExists(name);
    }
}
