package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class Scoreboard implements Serializable {
    private final ScoreboardHolder scoreboardHolder;
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
        for (int i = 0; i < scoreboardHolder.getLines().size(); i++) {
           paintLine(i);
        }
    }

    public void paintLine(int line) {
        final Objective objective = getObjective();
        final var lines = scoreboardHolder.getLines();
        objective.setDisplaySlot(scoreboardHolder.getDisplaySlot());

        if (!lines.isEmpty()) {
            final Map.Entry<String, Integer> entry = lines.get(line);
            final Score score = objective.getScore(entry.getKey());
            score.setScore(entry.getValue());
        }
    }

    private Objective getObjective() {
        final org.bukkit.scoreboard.Scoreboard bukkitScoreboard = scoreboardHolder.getBukkitScoreboard();
        final var name = scoreboardHolder.getName();
        final var displayedName = scoreboardHolder.getDisplayedName();
        Objective objective = bukkitScoreboard.getObjective(name);

        return Objects.requireNonNullElseGet(objective, () -> bukkitScoreboard.registerNewObjective(name, "dummy", displayedName));
    }

    public org.bukkit.scoreboard.Scoreboard getBukkitScoreboard() {
        return getScoreboardHolder().getBukkitScoreboard();
    }
}
