package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.Bukkit;

@Data
public class Scoreboard {
    private String scoreboardName;
    private org.bukkit.scoreboard.Scoreboard bukkitScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
}
