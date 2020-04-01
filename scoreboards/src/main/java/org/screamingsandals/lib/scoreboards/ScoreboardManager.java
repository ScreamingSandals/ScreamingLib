package org.screamingsandals.lib.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.scoreboards.content.Content;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

public class ScoreboardManager extends BaseManager<Player> {

    @Override
    public void hideAllScoreboards() {
        getActiveScoreboards().keySet().forEach(player -> player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()));
    }

    @Override
    public void showScoreboard(Player player, Content content) {
        System.out.println("Showing scoreboard to player!");
        super.showScoreboard(player, content);
        player.setScoreboard(content.getScoreboard().getBukkitScoreboard());
    }

    @Override
    public void hideScoreboard(Player player) {
        super.hideScoreboard(player);
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }
}
