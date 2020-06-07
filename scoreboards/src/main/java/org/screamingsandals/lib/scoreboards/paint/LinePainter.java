package org.screamingsandals.lib.scoreboards.paint;

import lombok.Data;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.screamingsandals.lib.scoreboards.scoreboard.ScreamingScoreboard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class LinePainter {
    private final ScreamingScoreboard scoreboard;
    private final Map<Integer, String> paintedLines = new HashMap<>();

    public void paintLines() {
        paintLines(Collections.emptyMap());
    }

    public void paintLines(Map<String, Object> available) {
        final var holder = scoreboard.getScoreboardHolder();
        final var lines = holder.getLines(available);
        final var bukkitScoreboard = holder.getBukkitScoreboard();

        bukkitScoreboard.getEntries().forEach(bukkitScoreboard::resetScores);

        for (var entry : lines.entrySet()) {
            var value = entry.getValue();
            final var valueCharCount = value.toCharArray().length;

            if (valueCharCount > 40) {
                value = "error!";
                System.out.println("Invalid value, longer than 40 characters!");
                System.out.println(entry.getValue());
            }

            paintLine(entry.getKey(), value);
        }
    }

    public void paintLine(int line, String content) {
        final var holder = scoreboard.getScoreboardHolder();
        final var objective = getObjective();

        objective.setDisplaySlot(holder.getDisplaySlot());
        final Score score = objective.getScore(content);
        score.setScore(line);

        paintedLines.put(line, content);
    }

    private Objective getObjective() {
        final var holder = scoreboard.getScoreboardHolder();
        final var objectiveName = "screamings";
        final var bukkitScoreboard = holder.getBukkitScoreboard();
        final var displayedName = holder.getDisplayedName();
        final var objective = bukkitScoreboard.getObjective(objectiveName);

        return Objects.requireNonNullElseGet(objective, () -> bukkitScoreboard.registerNewObjective(objectiveName, "dummy", displayedName));
    }
}
