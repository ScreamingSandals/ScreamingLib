package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.*;

@Data
public class Scoreboard {
    private final ScoreboardHolder scoreboardHolder;
    private String displayedName;
    private DisplaySlot displaySlot;
    private List<Map.Entry<String, Integer>> lines = new ArrayList<>();
    private ScoreboardAnimation scoreboardAnimation = new ScoreboardAnimation();

    public Scoreboard() {
        scoreboardHolder = new ScoreboardHolder();
    }

    public static List<Map.Entry<String, Integer>> sortLines(List<String> lines) {
        final List<Map.Entry<String, Integer>> sortedLines = new ArrayList<>();
        final int linesCount = lines.size();

        for (int i = 0; i < linesCount; i++) {
            sortedLines.add(Map.entry(lines.get(i), linesCount - i));
        }

        return sortedLines;
    }

    public void paintAll() {
        for (int i = 0; i < lines.size(); i++) {
           paintLine(i);
        }
    }

    public void paintLine(int line) {
        final Objective objective = getObjective();
        objective.setDisplaySlot(displaySlot);

        if (!lines.isEmpty()) {
            final Map.Entry<String, Integer> entry = lines.get(line);
            final Score score = objective.getScore(entry.getKey());
            score.setScore(entry.getValue());
        }
    }

    private Objective getObjective() {
        final org.bukkit.scoreboard.Scoreboard bukkitScoreboard = scoreboardHolder.getBukkitScoreboard();
        final String name = scoreboardHolder.getScoreboardName();
        Objective objective = bukkitScoreboard.getObjective(name);

        return Objects.requireNonNullElseGet(objective, () -> bukkitScoreboard.registerNewObjective(name, "dummy", displayedName));
    }

    public org.bukkit.scoreboard.Scoreboard getBukkitScoreboard() {
        return getScoreboardHolder().getBukkitScoreboard();
    }

    public String getScoreboardName() {
        return getScoreboardHolder().getScoreboardName();
    }
}
