package org.screamingsandals.lib.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardManager extends BaseManager<Player> {

    @Override
    public void showToAllOwners() {
        getActiveScoreboards().forEach((player, scoreboard) -> {
            player.setScoreboard(scoreboard.getBukkitScoreboard());
        });
    }

    @Override
    public void hideFromAllOwners() {
        getActiveScoreboards().keySet().forEach(player -> player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()));
    }
}
