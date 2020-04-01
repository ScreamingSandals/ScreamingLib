package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.scoreboard.DisplaySlot;
import org.screamingsandals.lib.scoreboards.content.Content;

import java.util.List;
import java.util.Map;

@Data
public class ScoreboardCreator {
    private Content content;
    private Scoreboard scoreboard;

    public ScoreboardCreator() {
        scoreboard = new Scoreboard();
        content = new Content(scoreboard);
    }

    public static ScoreboardCreator get() {
        return new ScoreboardCreator();
    }

    public static ScoreboardCreator get(String scoreboardName) {
        ScoreboardCreator scoreboardCreator = get();
        scoreboardCreator.getScoreboard().setScoreboardName(scoreboardName);

        return scoreboardCreator;
    }

    public static ScoreboardCreator get(String scoreboardName, List<String> teams) {
        final ScoreboardCreator scoreboardCreator = get();
        final Scoreboard scoreboard = scoreboardCreator.getScoreboard();
        scoreboard.setScoreboardName(scoreboardName);

        teams.forEach(scoreboardCreator::addTeam);
        return scoreboardCreator;
    }

    public Content create(String displayedName, DisplaySlot displaySlot, List<Map.Entry<String, Integer>> lines) {
        Content content = new Content(scoreboard);
        content.setDisplayedName(displayedName);
        content.setDisplaySlot(displaySlot);
        content.setLines(lines);

        content.paintAll();
        setContent(content);

        return content;
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
