package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.List;
import java.util.SortedMap;

@Data
public class ScoreboardCreator {
    private Content content;
    private Scoreboard scoreboard;

    public ScoreboardCreator() {
        scoreboard = new Scoreboard();
        content = new Content(scoreboard);
    }

    public ScoreboardCreator get() {
        return new ScoreboardCreator();
    }

    public ScoreboardCreator get(String scoreboardName) {
        ScoreboardCreator scoreboardCreator = get();
        scoreboardCreator.getScoreboard().setScoreboardName(scoreboardName);

        return scoreboardCreator;
    }

    public ScoreboardCreator get(String scoreboardName, List<String> teams) {
        final ScoreboardCreator scoreboardCreator = get();
        final Scoreboard scoreboard = scoreboardCreator.getScoreboard();
        scoreboard.setScoreboardName(scoreboardName);

        teams.forEach(scoreboardCreator::addTeam);
        return this;
    }

    public Scoreboard create(Content content) {
        content.create();
        setContent(content);

        return scoreboard;
    }

    public Scoreboard create(String displayedName, DisplaySlot displaySlot, SortedMap<String, Integer> lines) {
        Content content = new Content(scoreboard);
        content.create();
        setContent(content);

        return scoreboard;
    }

    public ScoreboardCreator setScoreboardName(String scoreboardName) {
        scoreboard.setScoreboardName(scoreboardName);
        return this;
    }

    public ScoreboardCreator addTeam(String name) {
        if (isTeamExists(name)) {
            scoreboard.getBukkitScoreboard().registerNewTeam(name);
        }
        return this;
    }

    public ScoreboardCreator setContent(Content content) {
        this.content = content;
        return this;
    }

    public boolean isTeamExists(String name) {
        return scoreboard.getBukkitScoreboard().getTeam(name) != null;
    }
}
