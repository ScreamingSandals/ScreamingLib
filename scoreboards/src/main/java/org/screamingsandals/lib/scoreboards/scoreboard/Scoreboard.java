package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
public class Scoreboard implements Serializable {
    protected ScoreboardHolder scoreboardHolder;
    protected String name;
    protected ScoreboardAnimation scoreboardAnimation = new ScoreboardAnimation();
    @Getter(value =  AccessLevel.PRIVATE)
    private final List<Team> activeTeams;

    public Scoreboard() {
        scoreboardHolder = new ScoreboardHolder();
        activeTeams = new LinkedList<>();
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
            final var entry = lines.get(line);
            final Score score = objective.getScore(entry);
            score.setScore(line);
        }
    }

    private Objective getObjective() {
        final org.bukkit.scoreboard.Scoreboard bukkitScoreboard = scoreboardHolder.getBukkitScoreboard();
        final var displayedName = scoreboardHolder.getDisplayedName();
        Objective objective = bukkitScoreboard.getObjective(name);

        return Objects.requireNonNullElseGet(objective, () -> bukkitScoreboard.registerNewObjective(name, "dummy", displayedName));
    }

    public org.bukkit.scoreboard.Scoreboard getBukkitScoreboard() {
        return getScoreboardHolder().getBukkitScoreboard();
    }

    public boolean isTeamExists(String name) {
        return getBukkitScoreboard().getTeam(name) != null;
    }

    public void addTeam(String name, ChatColor color) {
        if (isTeamExists(name)) {
            final var team = getBukkitScoreboard().registerNewTeam(name);
            team.setColor(color);
            team.setDisplayName(name);

            activeTeams.add(team);
        }
    }

    public Optional<Team> getScoreboardTeam(String name) {
        for (var team : activeTeams) {
            if (team.getName().equals(name)) {
                return Optional.of(team);
            }
        }
        return Optional.empty();
    }

    public List<Team> getTeams() {
        return new LinkedList<>(activeTeams);
    }
}
