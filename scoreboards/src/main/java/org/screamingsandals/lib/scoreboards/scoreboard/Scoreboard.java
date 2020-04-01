package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.Bukkit;
import org.screamingsandals.lib.scoreboards.content.Content;

@Data
public class Scoreboard {
    private String scoreboardName;
    private org.bukkit.scoreboard.Scoreboard bukkitScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
}
