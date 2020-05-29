package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.screamingsandals.lib.scoreboards.holder.ScoreboardHolder;
import org.screamingsandals.lib.scoreboards.paint.LinePainter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
public abstract class ScreamingScoreboard {
    protected ScoreboardHolder scoreboardHolder;
    protected String identifier;
    @Getter(value = AccessLevel.PRIVATE)
    protected final List<Team> activeTeams;

    private LinePainter linePainter;
    private int latestUpdated;

    public ScreamingScoreboard() {
        scoreboardHolder = new ScoreboardHolder();
        activeTeams = new LinkedList<>();
        linePainter = new LinePainter(this);
    }

    public void paintLines() {
        linePainter.paintLines();
    }

    public void paintLines(Map<String, Object> available) {
        linePainter.paintLines(available);
    }

    public Scoreboard getBukkitScoreboard() {
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

    public Optional<Team> getTeam(String name) {
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

    //TODO: player placeholders and so on
}
