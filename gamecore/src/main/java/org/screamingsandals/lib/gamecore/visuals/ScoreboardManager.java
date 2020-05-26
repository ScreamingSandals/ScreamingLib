package org.screamingsandals.lib.gamecore.visuals;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.screamingsandals.lib.gamecore.GameCore;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.scoreboards.BaseManager;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
@ToString(exclude = "gameFrame")
public class ScoreboardManager extends BaseManager<UUID> {
    private final GameFrame gameFrame;

    @Override
    public void hideAllScoreboards() {
        getActiveScoreboards().keySet().forEach(uuid -> {
            final var gamePlayer = GameCore.getPlayerManager().getRegisteredPlayer(uuid);
            gamePlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        });
    }

    @Override
    public void showScoreboard(UUID uuid, Scoreboard scoreboard) {
        super.showScoreboard(uuid, scoreboard);

        final var gamePlayer = GameCore.getPlayerManager().getRegisteredPlayer(uuid);
        gamePlayer.getPlayer().setScoreboard(scoreboard.getBukkitScoreboard());
    }

    @Override
    public void hideScoreboard(UUID uuid) {
        super.hideScoreboard(uuid);

        final var gamePlayer = GameCore.getPlayerManager().getRegisteredPlayer(uuid);
        gamePlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    public void show(GamePlayer gamePlayer, Scoreboard scoreboard) {
        showScoreboard(gamePlayer.getUuid(), scoreboard);
    }

    public void hide(GamePlayer gamePlayer) {
        hideScoreboard(gamePlayer.getUuid());
    }
}
