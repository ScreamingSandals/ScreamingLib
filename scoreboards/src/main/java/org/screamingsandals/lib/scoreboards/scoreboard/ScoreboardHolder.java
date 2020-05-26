package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.TreeMap;

@Data
public class ScoreboardHolder {
    private transient org.bukkit.scoreboard.Scoreboard bukkitScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private String displayedName;
    private DisplaySlot displaySlot;
    private TreeMap<Integer, String> lines = new TreeMap<>();
}
