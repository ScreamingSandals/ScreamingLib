package org.screamingsandals.gamecore.visuals;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.screamingsandals.gamecore.core.GameFrame;
import org.screamingsandals.gamecore.player.GamePlayer;
import org.screamingsandals.lib.scoreboards.BaseManager;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

@EqualsAndHashCode(callSuper = false)
@Data
public class ScoreboardManager extends BaseManager<GamePlayer> {
    private final GameFrame gameFrame;

    @Override
    public void hideAllScoreboards() {
        getActiveScoreboards().keySet().forEach(gamePlayer -> gamePlayer.getBukkitPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()));
    }

    @Override
    public void showScoreboard(GamePlayer gamePlayer, Scoreboard scoreboard) {
        super.showScoreboard(gamePlayer, scoreboard);
        gamePlayer.getBukkitPlayer().setScoreboard(scoreboard.getBukkitScoreboard());
    }

    @Override
    public void hideScoreboard(GamePlayer gamePlayer) {
        super.hideScoreboard(gamePlayer);
        gamePlayer.getBukkitPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }
}
