package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

@Data
public class Content {
    private final Scoreboard scoreboard;
    private String displayedName;
    private DisplaySlot displaySlot;
    private SortedMap<String, Integer> lines = new TreeMap<>();

    public void clearLines() {
        lines.clear();
    }

    public void addLine(String line, int value) {
        lines.put(line, value);
    }

    public void removeLine(String line) {
        lines.remove(line);
    }

    public void create() {
        final Objective objective = getObjective();

        lines.forEach((s, integer) -> {
            Score score = objective.getScore(s);
            score.setScore(integer);
        });
    }

    private Objective getObjective() {
        final org.bukkit.scoreboard.Scoreboard bukkitScoreboard = scoreboard.getBukkitScoreboard();
        Objective objective = bukkitScoreboard.getObjective("board");

        return Objects.requireNonNullElseGet(objective, () -> bukkitScoreboard.registerNewObjective("board", "dummy", displayedName));
    }
}
